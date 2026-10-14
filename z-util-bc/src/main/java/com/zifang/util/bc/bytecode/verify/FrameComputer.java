package com.zifang.util.bc.bytecode.verify;

import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantDoubleInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantFloatInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantIntegerInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantInvokeDynamicInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantLongInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantStringInfo;
import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.InstructionSequence;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;
import com.zifang.util.bc.bytecode.writer.ClassWriter;
import com.zifang.util.bc.bytecode.writer.ConstantPoolBuilder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 栈帧计算器
 * <p>
 * 给被改动过的方法体重算 StackMapTable。这是「改字节码」与「改完还能跑」之间唯一的硬门槛：
 * major ≥ 50 的类必须自带帧，而帧错一位只是 VerifyError，不会告诉你错在哪条指令。
 * <p>
 * 算法是经典的数据流不动点：从隐式初始帧沿控制流前推，汇合处做 merge
 * （操作数栈必须逐项一致，局部变量任一侧为 TOP 就取 TOP），帧一变就重排后继，直到队列排空。
 * 指令级语义在 {@link OpcodeEffects}，本类只管传播与合并。
 * <p>
 * 真正会咬人的三件事都显式处理了：
 * <ul>
 * <li>long/double 在局部变量里占两个槽、在栈帧里只占一项，两套下标不能混用</li>
 * <li>{@code new} 压的是 UNINITIALIZED(pc)，直到 invokespecial &lt;init&gt; 才成为普通引用类型；
 *     构造器里 this 的 UNINITIALIZED_THIS 同理，且替换必须覆盖已收敛的所有帧并让它们重算</li>
 * <li>异常处理器能接住区间内任意指令，处理器入口的栈只有异常对象，局部变量沿用异常发生处</li>
 * </ul>
 */
public class FrameComputer {

    private final Code code;
    private final InstructionSequence sequence;
    private final ConstantPoolHelper helper;
    private final ConstantPoolBuilder poolBuilder;
    private final int thisClassIndex;

    /** pc → 槽位表示的局部变量类型 */
    private final TreeMap<Integer, List<VerificationType>> frames = new TreeMap<>();
    private final Deque<Integer> worklist = new ArrayDeque<>();
    private final Set<Integer> queued = new HashSet<>();
    /**
     * pc → 到达该 pc 时的操作数栈
     * <p>
     * 栈必须和局部变量一样沿控制流传播，不能靠「上一条被执行指令的栈」隐式继承：
     * 工作队列的处理顺序与源码顺序无关，落到某条指令时队列里排着的可能是另一条前驱。
     */
    private final TreeMap<Integer, List<VerificationType>> stackFrames = new TreeMap<>();

    /**
     * 需要写帧的偏移：跳转落点、switch 各 case、异常处理器入口
     * <p>
     * 只有这些偏移后面才可能出现「从别处跳进来」，其余指令的帧由前一条指令的输出唯一确定，
     * 写进去既冗余又容易和编码规则打架。
     */
    private final java.util.SortedSet<Integer> frameOffsets = new java.util.TreeSet<>();

    /** 当前指令的输入帧（槽位表示） */
    private List<VerificationType> locals = new ArrayList<>();
    /** 当前指令的操作数栈，末尾为栈顶 */
    private final List<VerificationType> stack = new ArrayList<>();

    private int maxStack;
    private int maxLocals;

    /**
     * @param code           方法体
     * @param descriptor     方法描述符
     * @param isStatic       是否静态方法
     * @param isConstructor  是否构造器（决定 this 初始为 UNINITIALIZED_THIS）
     * @param thisClassIndex 本类的 CONSTANT_Class_info 下标，无则传 -1
     * @param poolList       常量池
     * @param poolBuilder    常量池构造器，帧计算补记的类型会追加进去；为 null 时不允许缺项
     */
    public FrameComputer(Code code, String descriptor, boolean isStatic, boolean isConstructor, int thisClassIndex,
                         List<AbstractConstantPool> poolList, ConstantPoolBuilder poolBuilder) {
        this.code = code;
        this.sequence = new InstructionSequence(ClassWriter.codeBytes(code));
        this.helper = new ConstantPoolHelper(poolList);
        this.poolBuilder = poolBuilder;
        this.thisClassIndex = thisClassIndex;
        this.locals = padToSlots(initialLocals(descriptor, isStatic, isConstructor),
                code.getMaxLocals().intValue());
        this.maxLocals = locals.size();
        // 方法入口的操作数栈恒为空
        stackFrames.put(0, new ArrayList<VerificationType>());
        enqueue(0);
    }

    /**
     * 算出一个方法体需要的全部栈帧
     *
     * @param code           方法体
     * @param descriptor     方法描述符
     * @param isStatic       是否静态方法
     * @param isConstructor  是否构造器
     * @param thisClassIndex 本类的 CONSTANT_Class_info 下标
     * @param poolList       常量池
     * @param poolBuilder    常量池构造器
     * @return List类型返回值
     */
    public static List<StackMapFrameEntry> compute(Code code, String descriptor, boolean isStatic,
                                                   boolean isConstructor, int thisClassIndex,
                                                   List<AbstractConstantPool> poolList,
                                                   ConstantPoolBuilder poolBuilder) {
        return new FrameComputer(code, descriptor, isStatic, isConstructor, thisClassIndex, poolList, poolBuilder)
                .run();
    }

    /**
     * 算出的 max_stack
     *
     * @return int类型返回值
     */
    public int getMaxStack() {
        return maxStack;
    }

    /**
     * 算出的 max_locals
     *
     * @return int类型返回值
     */
    public int getMaxLocals() {
        return maxLocals;
    }

    // ---------------------------------------------------------------- 不动点迭代

    /**
     * 迭代到不动点，收集所有非零偏移处的帧
     *
     * @return List类型返回值
     */
    public List<StackMapFrameEntry> run() {
        while (!worklist.isEmpty()) {
            int pc = worklist.poll();
            queued.remove(pc);

            // 帧只在 arrive 里合并并入队；出队时若再拿「上一条指令的 locals」合一次，
            // 等于凭空多出一条边，而合并只会变宽不会收窄，那点 top 会永久留在帧里
            if (frames.get(pc) == null) {
                frames.put(pc, new ArrayList<>(locals));
            }
            locals = new ArrayList<>(frames.get(pc));
            stack.clear();
            stack.addAll(stackFrames.get(pc));

            if (pc >= sequence.length()) {
                continue;
            }
            Instruction insn = sequence.at(pc);
            OpcodeEffects.apply(insn.getOpcode(), insn, pc, new Ops());
            maxStack = Math.max(maxStack, slotsOf(stack));
            maxLocals = Math.max(maxLocals, locals.size());

            propagate(insn, pc);
            feedExceptionHandlers(pc);
        }

        List<StackMapFrameEntry> entries = new ArrayList<>(frameOffsets.size());
        for (Integer offset : frameOffsets) {
            List<VerificationType> slotLocals = frames.get(offset);
            if (slotLocals == null) {
                continue;
            }
            // 帧里记的是「到达该 pc 时」的栈：处理器入口那条异常对象也必须在栈上
            List<VerificationType> arrived = stackFrames.get(offset);
            entries.add(new StackMapFrameEntry(offset, Frame.fromSlots(slotLocals,
                    arrived == null ? new ArrayList<VerificationType>() : new ArrayList<>(arrived))));
        }
        return entries;
    }

    private void push(VerificationType type) {
        stack.add(type);
    }

    private void enqueue(int pc) {
        if (pc < 0 || pc > sequence.length() || !queued.add(pc)) {
            return;
        }
        worklist.add(pc);
    }

    private static boolean mergeInto(List<VerificationType> target, List<VerificationType> newFrame) {
        boolean changed = false;
        int size = Math.max(target.size(), newFrame.size());
        while (target.size() < size) {
            target.add(VerificationType.top());
            changed = true;
        }
        for (int i = 0; i < size; i++) {
            VerificationType merged = mergeSlot(target.get(i), newFrame.get(i));
            if (!merged.equals(target.get(i))) {
                target.set(i, merged);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * 局部变量的汇合：只有两侧都赋值且类型一致才保留，否则退化成不可用
     * <p>
     * 反过来写就错了——处理器入口尤其明显：受保护区里某条指令刚存进去的局部变量，
     * 若让那一侧胜出，处理器开头那条 astore 之前就会声称该槽已有类型，
     * 而从别的路径（该槽还是 top）跳进来时并不满足，JVM 直接 VerifyError。
     */
    private static VerificationType mergeSlot(VerificationType a, VerificationType b) {
        if (a == null || b == null
                || a.getTag() == VerificationType.TOP || b.getTag() == VerificationType.TOP) {
            return VerificationType.top();
        }
        return a.equals(b) ? a : VerificationType.top();
    }

    private void propagate(Instruction insn, int pc) {
        int opcode = insn.getOpcode();
        int next = insn.getNextPc();
        if (Opcodes.isTerminator(opcode) || opcode == Opcodes.RET) {
            return;
        }
        if (opcode == Opcodes.GOTO || opcode == Opcodes.GOTO_W) {
            arriveBranch(pc + insn.branchTarget(), stack);
            return;
        }
        if (Opcodes.isConditionalJump(opcode)) {
            arriveBranch(pc + insn.branchTarget(), stack);
            arrive(next, stack);
            return;
        }
        if (opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH) {
            for (int target : sequence.switchTargets(insn)) {
                arriveBranch(target, stack);
            }
            return;
        }
        if (opcode == Opcodes.JSR || opcode == Opcodes.JSR_W) {
            // 现代 javac 不再生成 jsr，但解析老字节码会遇到；落点栈上多一个 returnAddress
            arriveBranch(pc + insn.branchTarget(), stack);
            List<VerificationType> withReturnAddress = new ArrayList<>(stack);
            withReturnAddress.add(VerificationType.object(returnAddressClassIndex()));
            arrive(next, withReturnAddress);
            return;
        }
        arrive(next, stack);
    }

    /**
     * 跳转移入：这些偏移后面必须有帧
     */
    private void arriveBranch(int target, List<VerificationType> arrivedStack) {
        if (target > 0 && target <= sequence.length()) {
            frameOffsets.add(target);
        }
        arrive(target, arrivedStack);
    }

    /**
     * 记录一条到达边：局部变量取汇合，操作数栈必须一致
     */
    private void arrive(int target, List<VerificationType> arrivedStack) {
        if (target < 0 || target > sequence.length()) {
            return;
        }
        List<VerificationType> mergedStack = stackFrames.get(target);
        if (mergedStack == null) {
            stackFrames.put(target, new ArrayList<>(arrivedStack));
        } else if (!sameStack(mergedStack, arrivedStack)) {
            // 分支汇合处栈不一致说明字节码不可校验，但多个 catch 处理器共用一个 handler_pc 是合法的
            if (!compatibleHandlerStacks(mergedStack, arrivedStack)) {
                throw new ClassVerifyException("分支汇合处操作数栈不一致: " + mergedStack
                        + " vs " + arrivedStack, target);
            }
        }

        List<VerificationType> targetLocals = frames.get(target);
        if (targetLocals == null) {
            frames.put(target, new ArrayList<>(locals));
            enqueue(target);
            return;
        }
        if (mergeInto(targetLocals, locals)) {
            enqueue(target);
        }
    }

    private static boolean sameStack(List<VerificationType> a, List<VerificationType> b) {
        return a.size() == b.size() && a.equals(b);
    }

    /**
     * 同一 handler_pc 上多个异常处理器时，栈里那个引用类型以哪个为准都可以校验
     */
    private static boolean compatibleHandlerStacks(List<VerificationType> a, List<VerificationType> b) {
        return a.size() == 1 && b.size() == 1 && isReferenceLike(a.get(0)) && isReferenceLike(b.get(0));
    }

    private static boolean isReferenceLike(VerificationType t) {
        int tag = t.getTag();
        return tag == VerificationType.OBJECT || tag == VerificationType.NULL
                || tag == VerificationType.TOP || tag == VerificationType.UNINITIALIZED
                || tag == VerificationType.UNINITIALIZED_THIS;
    }

    private void feedExceptionHandlers(int pc) {
        for (Code.ExceptionInfo handler : code.getExceptionTable()) {
            int start = handler.getStartPc().intValue();
            int end = handler.getEndPc().intValue();
            if (pc < start || pc >= end) {
                continue;
            }
            int handlerPc = handler.getHandlerPc().intValue();
            int catchType = handler.getCatchPc().intValue();
            List<VerificationType> handlerStack = new ArrayList<>(1);
            // catch_type 为 0 表示任意异常，栈上的类型是 java/lang/Throwable（JVMS §4.10.1.6）
            handlerStack.add(catchType == 0 ? referenceType("java/lang/Throwable")
                    : VerificationType.object(catchType));
            arriveBranch(handlerPc, handlerStack);
        }
    }

    // ---------------------------------------------------------------- 初始帧

    private List<VerificationType> initialLocals(String descriptor, boolean isStatic, boolean isConstructor) {
        List<VerificationType> compact = new ArrayList<>();
        if (!isStatic) {
            // 构造器里 super() 之前的 this 还不是本类类型
            compact.add(isConstructor || thisClassIndex < 0 ? VerificationType.uninitializedThis()
                    : VerificationType.object(thisClassIndex));
        }
        for (String param : parseParameterDescriptors(descriptor)) {
            compact.add(typeOf(param));
        }
        return compact;
    }

    private static List<VerificationType> padToSlots(List<VerificationType> compact, int slots) {
        List<VerificationType> result = new ArrayList<>(slots);
        for (VerificationType t : compact) {
            result.add(t);
            if (t.isWide()) {
                result.add(VerificationType.top());
            }
        }
        while (result.size() < slots) {
            result.add(VerificationType.top());
        }
        return result;
    }

    private static int slotsOf(List<VerificationType> compact) {
        int n = 0;
        for (VerificationType t : compact) {
            n += t.slotSize();
        }
        return n;
    }

    /**
     * 解析方法描述符的参数类型
     *
     * @param descriptor 方法描述符
     * @return List类型返回值
     */
    public static List<String> parseParameterDescriptors(String descriptor) {
        List<String> params = new ArrayList<>();
        if (descriptor == null) {
            return params;
        }
        int i = descriptor.indexOf('(') + 1;
        while (i < descriptor.length() && descriptor.charAt(i) != ')') {
            int[] end = new int[1];
            params.add(readType(descriptor, i, end));
            i = end[0];
        }
        return params;
    }

    /**
     * 解析方法描述符的返回类型
     *
     * @param descriptor 方法描述符
     * @return String类型返回值
     */
    public static String parseReturnDescriptor(String descriptor) {
        if (descriptor == null) {
            return "V";
        }
        int close = descriptor.lastIndexOf(')');
        if (close < 0 || close + 1 >= descriptor.length()) {
            return "V";
        }
        int[] end = new int[1];
        return readType(descriptor, close + 1, end);
    }

    private static String readType(String descriptor, int start, int[] end) {
        int i = start;
        while (i < descriptor.length() && descriptor.charAt(i) == '[') {
            i++;
        }
        if (i >= descriptor.length()) {
            end[0] = i;
            return "V";
        }
        if (descriptor.charAt(i) == 'L') {
            int semi = descriptor.indexOf(';', i);
            int to = semi < 0 ? descriptor.length() : semi + 1;
            end[0] = to;
            return descriptor.substring(start, to);
        }
        end[0] = i + 1;
        return descriptor.substring(start, i + 1);
    }

    // ---------------------------------------------------------------- 类型解析

    /**
     * JVM 描述符类型 → 校验类型
     *
     * @param descriptor 类型描述符
     * @return VerificationType类型返回值
     */
    VerificationType typeOf(String descriptor) {
        switch (descriptor.charAt(0)) {
            case 'I':
            case 'B':
            case 'C':
            case 'S':
            case 'Z':
                return VerificationType.integer();
            case 'F':
                return VerificationType.floatType();
            case 'J':
                return VerificationType.longType();
            case 'D':
                return VerificationType.doubleType();
            case 'V':
                return VerificationType.top();
            case 'L':
                return referenceType(descriptor.substring(1, descriptor.length() - 1));
            case '[':
                return referenceType(descriptor);
            default:
                throw new ClassVerifyException("无法识别的类型描述符: " + descriptor, -1);
        }
    }

    private VerificationType referenceType(String internalOrArray) {
        int index = classIndexOf(internalOrArray);
        return index < 0 ? VerificationType.nullType() : VerificationType.object(index);
    }

    /**
     * 取类型对应的 CONSTANT_Class_info 下标，原池没有就追加一条
     */
    private int classIndexOf(String internalOrArray) {
        Integer existing = helper.classIndexOf(internalOrArray);
        if (existing != null) {
            return existing;
        }
        if (poolBuilder == null) {
            throw new ClassVerifyException("常量池缺少类型项 " + internalOrArray + " 且没有可追加的构造器", -1);
        }
        return poolBuilder.clazz(internalOrArray);
    }

    private int returnAddressClassIndex() {
        Integer index = helper.classIndexOf("java/lang/Object");
        if (index != null) {
            return index;
        }
        if (poolBuilder == null) {
            throw new ClassVerifyException("常量池缺少 java/lang/Object 且没有可追加的构造器", -1);
        }
        return poolBuilder.clazz("java/lang/Object");
    }

    /**
     * ldc 系列压入的类型
     */
    private VerificationType constantTypeOf(int cpIndex, int pc) {
        AbstractConstantPool entry = helper.get(cpIndex);
        if (entry instanceof ConstantIntegerInfo) {
            return VerificationType.integer();
        }
        if (entry instanceof ConstantFloatInfo) {
            return VerificationType.floatType();
        }
        if (entry instanceof ConstantLongInfo) {
            return VerificationType.longType();
        }
        if (entry instanceof ConstantDoubleInfo) {
            return VerificationType.doubleType();
        }
        if (entry instanceof ConstantStringInfo) {
            // 栈上类型是 java/lang/String 的引用，而不是字符串常量本身
            return VerificationType.object(classIndexOf("java/lang/String"));
        }
        if (entry instanceof ConstantInvokeDynamicInfo) {
            com.zifang.util.bc.bytecode.model.constantpool.ConstantNameAndTypeInfo nat = helper
                    .nameAndType(((ConstantInvokeDynamicInfo) entry).getNameAndTypeIndex().intValue());
            if (nat == null) {
                throw new ClassVerifyException("invokedynamic 常量的 NameAndType 无法解析: #" + cpIndex, pc);
            }
            return typeOf(helper.utf8(nat.getDescriptorIndex().intValue()));
        }
        throw new ClassVerifyException("ldc 引用了无法确定类型的常量池项 #" + cpIndex, pc);
    }

    // ---------------------------------------------------------------- 栈端口

    /**
     * 把本类的栈与局部变量接成 {@link OpcodeEffects} 需要的端口
     */
    private final class Ops implements OpcodeEffects.StackOps {

        @Override
        public void push(VerificationType type) {
            stack.add(type);
        }

        @Override
        public VerificationType pop(int pc) {
            if (stack.isEmpty()) {
                throw new ClassVerifyException("操作数栈下溢", pc);
            }
            return stack.remove(stack.size() - 1);
        }

        @Override
        public VerificationType popCat1(int pc) {
            VerificationType t = pop(pc);
            if (t.isWide()) {
                throw new ClassVerifyException("期望 category 1 类型，实际是 " + t, pc);
            }
            return t;
        }

        @Override
        public void popSlots(int slots, int pc) {
            int width = 0;
            while (width < slots) {
                width += pop(pc).slotSize();
            }
        }

        @Override
        public List<VerificationType> popValues(int slots, int pc) {
            List<VerificationType> reversed = new ArrayList<>();
            int width = 0;
            while (width < slots) {
                reversed.add(pop(pc));
                width += reversed.get(reversed.size() - 1).slotSize();
            }
            Collections.reverse(reversed);
            return reversed;
        }

        @Override
        public boolean topIsWide() {
            return !stack.isEmpty() && stack.get(stack.size() - 1).isWide();
        }

        @Override
        public boolean secondIsWide() {
            return stack.size() >= 2 && stack.get(stack.size() - 2).isWide();
        }

        @Override
        public int stackDepth() {
            return stack.size();
        }

        @Override
        public VerificationType loadLocal(int slot, int pc) {
            if (slot < 0 || slot >= locals.size()) {
                throw new ClassVerifyException("读取越界的局部变量槽 " + slot, pc);
            }
            VerificationType t = locals.get(slot);
            if (t == null || t.getTag() == VerificationType.TOP) {
                throw new ClassVerifyException("读取尚未赋值的局部变量槽 " + slot
                        + "（局部变量共 " + locals.size() + " 槽）", pc);
            }
            return t;
        }

        @Override
        public void storeLocal(int slot, VerificationType type) {
            while (locals.size() < slot + type.slotSize()) {
                locals.add(VerificationType.top());
            }
            locals.set(slot, type);
            for (int i = slot + 1; i < slot + type.slotSize(); i++) {
                locals.set(i, VerificationType.top());
            }
        }

        @Override
        public VerificationType typeOf(String descriptor) {
            return FrameComputer.this.typeOf(descriptor);
        }

        @Override
        public VerificationType arrayType(int atype) {
            return referenceType(ARRAY_DESCRIPTORS[atype] == null
                    ? throwUnknownArrayType(atype) : ARRAY_DESCRIPTORS[atype]);
        }

        @Override
        public VerificationType constantType(int cpIndex, int pc) {
            return constantTypeOf(cpIndex, pc);
        }

        @Override
        public void pushFieldType(int cpIndex, int pc) {
            String descriptor = helper.fieldDescriptor(cpIndex);
            if (descriptor == null) {
                throw new ClassVerifyException("无法解析字段常量池项 #" + cpIndex, pc);
            }
            push(typeOf(descriptor));
        }

        @Override
        public void popFieldType(int cpIndex, int pc) {
            String descriptor = helper.fieldDescriptor(cpIndex);
            if (descriptor == null) {
                throw new ClassVerifyException("无法解析字段常量池项 #" + cpIndex, pc);
            }
            popSlots(typeOf(descriptor).slotSize(), pc);
        }

        @Override
        public void invoke(int opcode, int cpIndex, int pc) {
            String descriptor = invokeDescriptorOf(cpIndex);
            if (descriptor == null) {
                throw new ClassVerifyException("无法解析调用常量池项 #" + cpIndex, pc);
            }
            int slots = 0;
            for (String param : parseParameterDescriptors(descriptor)) {
                slots += typeOf(param).slotSize();
            }
            popSlots(slots, pc);

            if (opcode != Opcodes.INVOKESTATIC && opcode != Opcodes.INVOKEDYNAMIC) {
                VerificationType receiver = popCat1(pc);
                if (opcode == Opcodes.INVOKESPECIAL && "<init>".equals(helper.methodName(cpIndex))) {
                    resolveInitialized(receiver, cpIndex, pc);
                }
            }

            String ret = parseReturnDescriptor(descriptor);
            if (!"V".equals(ret)) {
                push(typeOf(ret));
            }
        }
    }

    private static final String[] ARRAY_DESCRIPTORS = {
            null, null, null, null,
            "[Z", "[C", "[F", "[D", "[B", "[S", "[I", "[J"};

    private static String throwUnknownArrayType(int atype) {
        throw new ClassVerifyException("newarray 的 atype 非法: " + atype, -1);
    }

    /**
     * 调用的描述符；invokedynamic 不走 Methodref，得从 NameAndType 里取
     */
    private String invokeDescriptorOf(int cpIndex) {
        AbstractConstantPool entry = helper.get(cpIndex);
        if (entry instanceof ConstantInvokeDynamicInfo) {
            com.zifang.util.bc.bytecode.model.constantpool.ConstantNameAndTypeInfo nat = helper
                    .nameAndType(((ConstantInvokeDynamicInfo) entry).getNameAndTypeIndex().intValue());
            return nat == null ? null : helper.utf8(nat.getDescriptorIndex().intValue());
        }
        return helper.methodDescriptor(cpIndex);
    }

    /**
     * &lt;init&gt; 执行完毕，未初始化的 new 对象与 this 变成普通引用类型
     * <p>
     * 替换要覆盖所有已收敛的帧（未初始化的对象可能已经存进局部变量、被别的分支用到），
     * 改完把受影响 pc 重新排队，否则它们的帧会停在旧类型上。
     * <p>
     * 但只改 {@code <init> } 调用点之后的帧：调用之前（比如 dup）栈上必须仍是
     * UNINITIALIZED，全局替换会让那些帧提前变成已初始化类型，校验直接失败。
     */
    private void resolveInitialized(VerificationType receiver, int cpIndex, int pc) {
        int resolvedIndex;
        if (receiver.getTag() == VerificationType.UNINITIALIZED_THIS) {
            resolvedIndex = thisClassIndex;
        } else if (receiver.getTag() == VerificationType.UNINITIALIZED) {
            String owner = helper.methodOwner(cpIndex);
            resolvedIndex = owner == null ? thisClassIndex : classIndexOf(owner);
        } else {
            return;
        }
        if (resolvedIndex < 0) {
            throw new ClassVerifyException("无法确定 <init> 完成后的类型: #" + cpIndex, pc);
        }
        VerificationType resolved = VerificationType.object(resolvedIndex);
        int tag = receiver.getTag();
        int value = tag == VerificationType.UNINITIALIZED_THIS ? -1 : receiver.getValue();
        // 未初始化的对象既可能躺在局部变量里，也可能还压在栈上没存，两处都得换；
        // 当前这条指令的栈也得换——new 之后往往还留着一份 dup 出来的引用
        boolean touched = replaceAll(stack, tag, value, resolved);
        touched |= replaceAll(locals, tag, value, resolved);
        for (Map.Entry<Integer, List<VerificationType>> e : frames.entrySet()) {
            if (e.getKey() > pc) {
                touched |= replaceAll(e.getValue(), tag, value, resolved);
            }
        }
        for (Map.Entry<Integer, List<VerificationType>> e : stackFrames.entrySet()) {
            if (e.getKey() > pc) {
                touched |= replaceAll(e.getValue(), tag, value, resolved);
            }
        }
        // 没换到东西就别重新排队，否则每轮都会把全部 pc 再走一遍，永远收敛不了
        if (!touched) {
            return;
        }
        for (Integer queuedPc : new ArrayList<>(frames.tailMap(pc, false).keySet())) {
            enqueue(queuedPc);
        }
    }

    private static boolean replaceAll(List<VerificationType> types, int tag, int value,
                                      VerificationType resolved) {
        boolean touched = false;
        for (int i = 0; i < types.size(); i++) {
            VerificationType t = types.get(i);
            if (t != null && t.getTag() == tag && t.getValue() == value) {
                types.set(i, resolved);
                touched = true;
            }
        }
        return touched;
    }
}