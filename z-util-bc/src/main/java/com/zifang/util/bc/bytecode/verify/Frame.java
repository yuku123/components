package com.zifang.util.bc.bytecode.verify;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 栈帧：局部变量 + 操作数栈的类型快照
 * <p>
 * 本类的 {@code locals} 用的是<b>栈帧紧凑表示</b>（long/double 各占一个元素），
 * 因为 StackMapTable 属性本身就是这个格式。而指令执行时 long/double 占两个局部变量槽，
 * 所以帧计算器内部维护槽位表示的数组，读写属性时再经 {@link #fromSlots} / {@link #toSlots} 换算。
 * <p>
 * 两处最容易写错的地方都集中在这条不变量上：紧凑表示的下标不等于槽号，
 * 写 append/chop 帧时比的是紧凑表示，写 istore 时用的是槽号。
 */
public final class Frame {

    private final List<VerificationType> locals;
    private final List<VerificationType> stack;

    /**
     * @param locals 紧凑表示的局部变量类型
     * @param stack  操作数栈类型（栈顶在末尾）
     */
    public Frame(List<VerificationType> locals, List<VerificationType> stack) {
        this.locals = locals;
        this.stack = stack;
    }

    /**
     * 空帧（静态方法的隐式初始帧就是空帧再补参数）
     *
     * @return Frame类型返回值
     */
    public static Frame empty() {
        return new Frame(new ArrayList<VerificationType>(), new ArrayList<VerificationType>());
    }

    /**
     * 由槽位表示构造紧凑帧
     *
     * @param slotLocals 槽位表示的局部变量，long/double 之后的槽必须已是 TOP
     * @param stack      操作数栈
     * @return Frame类型返回值
     */
    public static Frame fromSlots(List<VerificationType> slotLocals, List<VerificationType> stack) {
        List<VerificationType> compact = new ArrayList<>(slotLocals.size());
        for (int i = 0; i < slotLocals.size(); i++) {
            VerificationType t = slotLocals.get(i);
            if (t == null) {
                compact.add(VerificationType.top());
                continue;
            }
            compact.add(t);
            if (t.isWide()) {
                i++;
            }
        }
        return new Frame(compact, new ArrayList<>(stack));
    }

    /**
     * 展开成槽位表示
     *
     * @return List类型返回值
     */
    public List<VerificationType> toSlots() {
        List<VerificationType> slots = new ArrayList<>();
        for (VerificationType t : locals) {
            slots.add(t);
            if (t.isWide()) {
                slots.add(VerificationType.top());
            }
        }
        return slots;
    }

    /**
     * 深拷贝，帧计算里到处都要这个
     *
     * @return Frame类型返回值
     */
    public Frame copy() {
        return new Frame(new ArrayList<>(locals), new ArrayList<>(stack));
    }

    /**
     * 汇合两个到达同一 pc 的帧（JVMS §4.10.1.9）
     * <p>
     * 局部变量逐位合并：只要有一侧是 TOP 就取 TOP（说明这条路径没赋值），
     * 两侧相同则保留，不同且都不是 TOP 则取 TOP——可达性上宁可丢类型也不能编一个不存在的类型。
     * 操作数栈必须完全一致，否则字节码不可校验。
     *
     * @param other 另一条路径到达的帧
     * @param pc    汇合点偏移，仅用于报错
     * @return boolean类型返回值，true 表示本帧被修改过、需要重算后继
     */
    public boolean merge(Frame other, int pc) {
        if (stack.size() != other.stack.size()) {
            throw new ClassVerifyException("分支汇合处操作数栈高度不一致: " + stack.size()
                    + " vs " + other.stack.size(), pc);
        }
        for (int i = 0; i < stack.size(); i++) {
            if (!stack.get(i).equals(other.stack.get(i))) {
                throw new ClassVerifyException("分支汇合处操作数栈类型不一致: "
                        + stack.get(i) + " vs " + other.stack.get(i), pc);
            }
        }
        boolean changed = false;
        int size = Math.max(locals.size(), other.locals.size());
        while (locals.size() < size) {
            locals.add(VerificationType.top());
            changed = true;
        }
        for (int i = 0; i < size; i++) {
            VerificationType mine = locals.get(i);
            VerificationType theirs = i < other.locals.size() ? other.locals.get(i) : VerificationType.top();
            VerificationType merged = mergeType(mine, theirs);
            if (!merged.equals(mine)) {
                locals.set(i, merged);
                changed = true;
            }
        }
        return changed;
    }

    private static VerificationType mergeType(VerificationType a, VerificationType b) {
        if (a.getTag() == VerificationType.TOP || b.getTag() == VerificationType.TOP) {
            return VerificationType.top();
        }
        if (a.equals(b)) {
            return a;
        }
        return VerificationType.top();
    }

    /**
     * 把帧里所有指向某条 new 指令的未初始化类型替换成初始化后的引用类型
     * <p>
     * 只在 invokespecial &lt;init&gt; 之后调用：此时这条 new 产生的对象已经能当其声明类型用了。
     *
     * @param newPc      new 指令偏移
     * @param classIndex 初始化完成后应视作的类型
     */
    public void replaceUninitialized(int newPc, int classIndex) {
        VerificationType resolved = VerificationType.object(classIndex);
        for (int i = 0; i < locals.size(); i++) {
            VerificationType t = locals.get(i);
            if (t.getTag() == VerificationType.UNINITIALIZED && t.getValue() == newPc) {
                locals.set(i, resolved);
            }
        }
        for (int i = 0; i < stack.size(); i++) {
            VerificationType t = stack.get(i);
            if (t.getTag() == VerificationType.UNINITIALIZED && t.getValue() == newPc) {
                stack.set(i, resolved);
            }
        }
    }

    /**
     * 帧里是否还残留未初始化类型（<b>init 未被调用或已逃逸，属于校验错误）
     *
     * @return boolean类型返回值
     */
    public boolean hasUninitialized() {
        for (VerificationType t : locals) {
            if (t.getTag() == VerificationType.UNINITIALIZED) {
                return true;
            }
        }
        for (VerificationType t : stack) {
            if (t.getTag() == VerificationType.UNINITIALIZED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 紧凑局部变量表（append/chop 帧的比较基准）
     *
     * @return List类型返回值
     */
    public List<VerificationType> getLocals() {
        return locals;
    }

    /**
     * 操作数栈
     *
     * @return List类型返回值
     */
    public List<VerificationType> getStack() {
        return stack;
    }

    /**
     * 本帧编码后的字节数（不含 frame_type 与 offset_delta）
     *
     * @return int类型返回值
     */
    public int contentSize() {
        int n = 0;
        for (VerificationType t : locals) {
            n += t.contentSize();
        }
        for (VerificationType t : stack) {
            n += t.contentSize();
        }
        return n;
    }

    /**
     * 写出 locals 与 stack（full_frame 形态）
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        out.writeShort(locals.size());
        for (VerificationType t : locals) {
            t.write(out);
        }
        out.writeShort(stack.size());
        for (VerificationType t : stack) {
            t.write(out);
        }
    }

    /**
     * 只写 locals（append_frame 形态）
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void writeLocals(DataOutput out) throws IOException {
        for (VerificationType t : locals) {
            t.write(out);
        }
    }

    /**
     * 从第 from 项起写 locals（append_frame 只写追加的那几项）
     *
     * @param from 起始下标（紧凑表示）
     * @throws IOException 写出失败
     */
    public void writeLocalsFrom(DataOutput out, int from) throws IOException {
        for (int i = from; i < locals.size(); i++) {
            locals.get(i).write(out);
        }
    }

    /**
     * 只写栈顶（same_locals_1_stack_item 形态）
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void writeStackItem(DataOutput out) throws IOException {
        stack.get(stack.size() - 1).write(out);
    }

    /**
     * 截掉末尾 k 个紧凑局部变量项（chop_frame）
     *
     * @param k 删除项数
     */
    public void chopLocals(int k) {
        int size = locals.size() - k;
        while (locals.size() > size) {
            locals.remove(locals.size() - 1);
        }
    }

    /**
     * 末尾追加局部变量项（append_frame）
     *
     * @param extra 追加项
     */
    public void appendLocals(List<VerificationType> extra) {
        locals.addAll(extra);
    }

    /**
     * 与另一帧的紧凑局部变量表是否逐项相同
     *
     * @param other 另一帧
     * @return boolean类型返回值
     */
    public boolean sameLocals(Frame other) {
        return locals.equals(other.locals);
    }

    /**
     * 栈是否只有一个元素（same_locals_1_stack_item 可表达）
     *
     * @return boolean类型返回值
     */
    public boolean singleStackItem() {
        return stack.size() == 1;
    }

    /**
     * 栈是否为空
     *
     * @return boolean类型返回值
     */
    public boolean emptyStack() {
        return stack.isEmpty();
    }

    /**
     * 紧凑局部变量项数
     *
     * @return int类型返回值
     */
    public int localsSize() {
        return locals.size();
    }

    /**
     * 读一个 locals 数组
     *
     * @param count 项数
     * @param in    输入流
     * @return List类型返回值
     */
    public static List<VerificationType> readLocals(int count, java.io.InputStream in) {
        List<VerificationType> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(VerificationType.read(in));
        }
        return list;
    }

    /**
     * 读一个栈数组
     *
     * @param count 项数
     * @param in    输入流
     * @return List类型返回值
     */
    public static List<VerificationType> readStack(int count, java.io.InputStream in) {
        return readLocals(count, in);
    }

    /**
     * 读单个栈顶项
     *
     * @param in 输入流
     * @return VerificationType类型返回值
     */
    public static VerificationType readOneStackItem(java.io.InputStream in) {
        return VerificationType.read(in);
    }

    @Override
    public String toString() {
        return "Frame[locals=" + locals + ", stack=" + stack + "]";
    }
}