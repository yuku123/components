package com.zifang.util.bc.bytecode.builder;

import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;
import com.zifang.util.bc.bytecode.writer.ConstantPoolBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法体指令构造器
 * <p>
 * 直接手写字节数组没法表达「跳到后面还没出现的位置」，本类引入标签：
 * 先占位发出跳转指令，全部字节排完后再回填相对偏移。
 * <p>
 * 只覆盖最常用的一批指令。tableswitch / lookupswitch 的操作数要按 4 字节对齐、
 * 宽度依赖 low/high 与匹配对数，没法在这里边写边定，所以不在支持范围内，
 * 需要时用 {@link #raw(int, int...)} 自行拼好偏移再发。
 * <p>
 * 操作数宽度一律对着 JVMS §6.5 走：常量池下标类（new/checkcast/field/ invoke 系列）
 * 都是 u2，只有 ldc 系列是 u1。写错一个字节会让后续指令整体错位。
 */
public class InstructionBuilder {

    /** 方法体中的一个位置标记 */
    public static final class Label {
        private int offset = -1;
    }

    private static final class Patch {
        private final int operandOffset;
        private final Label label;

        private Patch(int operandOffset, Label label) {
            this.operandOffset = operandOffset;
            this.label = label;
        }
    }

    private final List<Byte> buffer = new ArrayList<>();
    private final List<Patch> patches = new ArrayList<>();
    private final Map<Label, Integer> resolved = new LinkedHashMap<>();
    private final List<int[]> exceptionRanges = new ArrayList<>();
    private final List<Integer> exceptionTypes = new ArrayList<>();
    private final List<Label[]> exceptionLabels = new ArrayList<>();

    /**
     * 取一个新标签
     *
     * @return Label类型返回值
     */
    public Label newLabel() {
        return new Label();
    }

    /**
     * 把标签钉在当前位置
     *
     * @param label 标签
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder mark(Label label) {
        if (resolved.containsKey(label)) {
            throw new IllegalStateException("标签被标记了两次");
        }
        resolved.put(label, buffer.size());
        return this;
    }

    /**
     * 发出无操作数指令
     *
     * @param opcode 操作码
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder op(int opcode) {
        buffer.add((byte) opcode);
        return this;
    }

    /**
     * 发出「操作码 + 一个字节操作数」
     *
     * @param opcode  操作码
     * @param operand 操作数
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder op(int opcode, int operand) {
        buffer.add((byte) opcode);
        buffer.add((byte) operand);
        return this;
    }

    /**
     * 发出「操作码 + 两个字节大端操作数」
     *
     * @param opcode  操作码
     * @param operand 操作数
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder op2(int opcode, int operand) {
        buffer.add((byte) opcode);
        buffer.add((byte) (operand >>> 8));
        buffer.add((byte) operand);
        return this;
    }

    /**
     * 按原始字节追加（switch 等需要自行拼好的片段）
     *
     * @param opcode  操作码
     * @param operands 紧随其后的字节
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder raw(int opcode, int... operands) {
        buffer.add((byte) opcode);
        for (int o : operands) {
            buffer.add((byte) o);
        }
        return this;
    }

    // ---------------------------------------------------------------- 常量与引用

    public InstructionBuilder aconstNull() {
        return op(Opcodes.ACONST_NULL);
    }

    /**
     * 压一个 int 常量，按值大小选 iconst / bipush / sipush / ldc
     *
     * @param value 常量值
     * @param pool  常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder iconst(int value, ConstantPoolBuilder pool) {
        if (value >= -1 && value <= 5) {
            return op(Opcodes.ICONST_0 + value);
        }
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return op(Opcodes.BIPUSH, value);
        }
        if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            buffer.add((byte) Opcodes.SIPUSH);
            buffer.add((byte) (value >>> 8));
            buffer.add((byte) value);
            return this;
        }
        return ldc(pool.integer(value));
    }

    public InstructionBuilder lconst0() {
        return op(Opcodes.LCONST_0);
    }

    public InstructionBuilder lconst1() {
        return op(Opcodes.LCONST_1);
    }

    /**
     * 压一个 long 常量
     *
     * @param value 常量值
     * @param pool  常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder lconst(long value, ConstantPoolBuilder pool) {
        return op2(Opcodes.LDC2_W, pool.longConstant(value));
    }

    /**
     * 压一个 String 常量
     *
     * @param value 字符串
     * @param pool  常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder ldcString(String value, ConstantPoolBuilder pool) {
        return ldc(pool.string(value));
    }

    /**
     * 压一个 Class 常量
     *
     * @param internalName 内部名
     * @param pool         常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder ldcClass(String internalName, ConstantPoolBuilder pool) {
        return ldc(pool.clazz(internalName));
    }

    /**
     * 压一个单字节常量（int / String / Class / float 等），下标超 255 自动改用 ldc_w
     * <p>
     * ldc 的下标字段只有一字节，直接截断会指向别的常量而且不报错，
     * 常量池一旦超过 255 项就必踩，因此这里按宽度分流。
     *
     * @param poolIndex 常量池下标
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder ldc(int poolIndex) {
        if (poolIndex < 0 || poolIndex > 0xFF) {
            return op2(Opcodes.LDC_W, poolIndex);
        }
        return op(Opcodes.LDC, poolIndex);
    }

    // ---------------------------------------------------------------- 局部变量

    public InstructionBuilder aload(int slot) {
        return slot <= 3 ? op(Opcodes.ALOAD_0 + slot) : op(Opcodes.ALOAD, slot);
    }

    public InstructionBuilder iload(int slot) {
        return slot <= 3 ? op(Opcodes.ILOAD_0 + slot) : op(Opcodes.ILOAD, slot);
    }

    public InstructionBuilder lload(int slot) {
        return slot <= 3 ? op(Opcodes.LLOAD_0 + slot) : op(Opcodes.LLOAD, slot);
    }

    public InstructionBuilder astore(int slot) {
        return slot <= 3 ? op(Opcodes.ASTORE_0 + slot) : op(Opcodes.ASTORE, slot);
    }

    public InstructionBuilder istore(int slot) {
        return slot <= 3 ? op(Opcodes.ISTORE_0 + slot) : op(Opcodes.ISTORE, slot);
    }

    public InstructionBuilder lstore(int slot) {
        return slot <= 3 ? op(Opcodes.LSTORE_0 + slot) : op(Opcodes.LSTORE, slot);
    }

    public InstructionBuilder iinc(int slot, int increment) {
        buffer.add((byte) Opcodes.IINC);
        buffer.add((byte) slot);
        buffer.add((byte) increment);
        return this;
    }

    // ---------------------------------------------------------------- 对象与字段

    /**
     * new 一个类
     *
     * @param internalName 内部名
     * @param pool         常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder newClass(String internalName, ConstantPoolBuilder pool) {
        return op2(Opcodes.NEW, pool.clazz(internalName));
    }

    public InstructionBuilder dup() {
        return op(Opcodes.DUP);
    }

    public InstructionBuilder pop() {
        return op(Opcodes.POP);
    }

    public InstructionBuilder getStatic(String owner, String name, String descriptor, ConstantPoolBuilder pool) {
        return op2(Opcodes.GETSTATIC, pool.fieldRef(owner, name, descriptor));
    }

    public InstructionBuilder putStatic(String owner, String name, String descriptor, ConstantPoolBuilder pool) {
        return op2(Opcodes.PUTSTATIC, pool.fieldRef(owner, name, descriptor));
    }

    /**
     * 调构造器
     *
     * @param owner       内部名
     * @param descriptor  构造器描述符
     * @param pool        常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder invokespecialInit(String owner, String descriptor, ConstantPoolBuilder pool) {
        return op2(Opcodes.INVOKESPECIAL, pool.methodRef(owner, "<init>", descriptor));
    }

    public InstructionBuilder invokevirtual(String owner, String name, String descriptor,
                                            ConstantPoolBuilder pool) {
        return op2(Opcodes.INVOKEVIRTUAL, pool.methodRef(owner, name, descriptor));
    }

    public InstructionBuilder invokestatic(String owner, String name, String descriptor,
                                           ConstantPoolBuilder pool) {
        return op2(Opcodes.INVOKESTATIC, pool.methodRef(owner, name, descriptor));
    }

    /**
     * 调用 super/this 的构造器
     *
     * @param owner      内部名
     * @param descriptor 描述符
     * @param pool       常量池
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder invokespecial(String owner, String name, String descriptor,
                                            ConstantPoolBuilder pool) {
        return op2(Opcodes.INVOKESPECIAL, pool.methodRef(owner, name, descriptor));
    }

    public InstructionBuilder checkcast(String internalName, ConstantPoolBuilder pool) {
        return op2(Opcodes.CHECKCAST, pool.clazz(internalName));
    }

    /**
     * newarray
     *
     * @param atype 元素类型码（4=boolean 10=int 11=long）
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder newarray(int atype) {
        return op(Opcodes.NEWARRAY, atype);
    }

    public InstructionBuilder arrayLength() {
        return op(Opcodes.ARRAYLENGTH);
    }

    public InstructionBuilder aastore() {
        return op(Opcodes.AASTORE);
    }

    public InstructionBuilder aaload() {
        return op(Opcodes.AALOAD);
    }

    // ---------------------------------------------------------------- 控制流

    /**
     * 无条件跳转
     *
     * @param label 目标标签
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder goTo(Label label) {
        buffer.add((byte) Opcodes.GOTO);
        patches.add(new Patch(buffer.size(), label));
        buffer.add((byte) 0);
        buffer.add((byte) 0);
        return this;
    }

    /**
     * 条件跳转
     *
     * @param opcode IFEQ / IFNE / IFLT … / IFNULL / IFNONNULL
     * @param label 目标标签
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder jump(int opcode, Label label) {
        buffer.add((byte) opcode);
        patches.add(new Patch(buffer.size(), label));
        buffer.add((byte) 0);
        buffer.add((byte) 0);
        return this;
    }

    /**
     * 注册一段异常处理区间
     *
     * @param start           受保护起始
     * @param end             受保护结束（不含）
     * @param handler         处理器入口
     * @param catchTypeIndex  捕获类型的常量池下标，0 表示任意异常
     * @return InstructionBuilder类型返回值
     */
    public InstructionBuilder tryCatch(Label start, Label end, Label handler, int catchTypeIndex) {
        exceptionLabels.add(new Label[]{start, end, handler});
        exceptionTypes.add(catchTypeIndex);
        return this;
    }

    // ---------------------------------------------------------------- 收尾

    /**
     * 当前已发出的字节数
     *
     * @return int类型返回值
     */
    public int currentOffset() {
        return buffer.size();
    }

    /**
     * 回填跳转偏移并产出指令字节
     *
     * @return byte[]类型返回值
     */
    public byte[] toByteArray() {
        byte[] bytes = new byte[buffer.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buffer.get(i);
        }
        for (Patch p : patches) {
            Integer target = resolved.get(p.label);
            if (target == null) {
                throw new IllegalStateException("跳转目标标签从未被标记");
            }
            int delta = target - (p.operandOffset - 1);
            if (delta < Short.MIN_VALUE || delta > Short.MAX_VALUE) {
                throw new IllegalStateException("跳转距离超出 2 字节偏移范围: " + delta);
            }
            bytes[p.operandOffset] = (byte) (delta >>> 8);
            bytes[p.operandOffset + 1] = (byte) delta;
        }
        return bytes;
    }

    /**
     * 异常表的四项偏移，写进 Code 属性用
     *
     * @return int[]类型返回值，每项依次是 start/end/handler/catchType
     */
    public List<int[]> resolveExceptionTable() {
        List<int[]> table = new ArrayList<>(exceptionTypes.size());
        for (int i = 0; i < exceptionTypes.size(); i++) {
            Label[] triple = exceptionLabels.get(i);
            table.add(new int[]{offsetOf(triple[0]), offsetOf(triple[1]), offsetOf(triple[2]),
                    exceptionTypes.get(i)});
        }
        return table;
    }

    private int offsetOf(Label label) {
        Integer offset = resolved.get(label);
        if (offset == null) {
            throw new IllegalStateException("异常表引用的标签从未被标记");
        }
        return offset;
    }

    /**
     * 直接改写已发出字节（指令级手术用）
     *
     * @param offset 字节偏移
     * @param values 新字节
     */
    public void patch(int offset, int... values) {
        for (int i = 0; i < values.length; i++) {
            buffer.set(offset + i, (byte) values[i]);
        }
    }

    /**
     * 往已有的 Code 属性写入指令与异常表
     *
     * @param code   Code 属性
     * @param bytes  指令字节
     * @param table  {@link #resolveExceptionTable()} 的结果
     */
    public static void applyTo(Code code, byte[] bytes, List<int[]> table) {
        com.zifang.util.bc.bytecode.writer.ClassWriter.setCodeBytes(code, bytes);
        code.getExceptionTable().clear();
        for (int[] row : table) {
            code.getExceptionTable().add(new Code.ExceptionInfo(
                    new com.zifang.util.bc.bytecode.model.readtype.U2(row[0]),
                    new com.zifang.util.bc.bytecode.model.readtype.U2(row[1]),
                    new com.zifang.util.bc.bytecode.model.readtype.U2(row[2]),
                    new com.zifang.util.bc.bytecode.model.readtype.U2(row[3])));
        }
    }
}