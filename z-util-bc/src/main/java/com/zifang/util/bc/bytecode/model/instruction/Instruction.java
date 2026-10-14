package com.zifang.util.bc.bytecode.model.instruction;

/**
 * 一条指令的视图
 * <p>
 * 不复制字节，只在原始 code 数组上定位，因此改动操作数会直接反映回方法体。
 */
public class Instruction {

    private final byte[] code;
    private final int pc;
    private final int opcode;
    private final int operandOffset;
    private final int operandSize;
    private final int nextPc;

    Instruction(byte[] code, int pc, int opcode, int operandOffset, int operandSize, int nextPc) {
        this.code = code;
        this.pc = pc;
        this.opcode = opcode;
        this.operandOffset = operandOffset;
        this.operandSize = operandSize;
        this.nextPc = nextPc;
    }

    public int getPc() {
        return pc;
    }

    public int getOpcode() {
        return opcode;
    }

    public String getMnemonic() {
        return Opcodes.mnemonic(opcode);
    }

    public int getOperandOffset() {
        return operandOffset;
    }

    public int getOperandSize() {
        return operandSize;
    }

    public int getNextPc() {
        return nextPc;
    }

    public int getLength() {
        return nextPc - pc;
    }

    /**
     * 操作数第 index 个字节（0 起）
     *
     * @param index 字节下标
     * @return int类型返回值
     */
    public int operandByte(int index) {
        return code[operandOffset + index] & 0xFF;
    }

    public void setOperandByte(int index, int value) {
        code[operandOffset + index] = (byte) value;
    }

    /**
     * 读取有符号单字节操作数
     *
     * @return int类型返回值
     */
    public int byteOperand() {
        return operandByte(0);
    }

    /**
     * 读取有符号双字节操作数
     *
     * @return int类型返回值
     */
    public int shortOperand() {
        return (short) ((operandByte(0) << 8) | operandByte(1));
    }

    /**
     * 读取无符号单字节操作数
     *
     * @return int类型返回值
     */
    public int u1Operand() {
        return operandByte(0);
    }

    /**
     * 读取无符号双字节操作数
     *
     * @return int类型返回值
     */
    public int u2Operand() {
        return (operandByte(0) << 8) | operandByte(1);
    }

    public void setU1Operand(int value) {
        setOperandByte(0, value);
    }

    public void setU2Operand(int value) {
        setOperandByte(0, value >>> 8);
        setOperandByte(1, value);
    }

    public void setShortOperand(int value) {
        setOperandByte(0, value >>> 8);
        setOperandByte(1, value);
    }

    /**
     * 跳转目标（相对当前指令起点的偏移）
     *
     * @return int类型返回值
     */
    public int branchTarget() {
        return operandSize == 4 ? (int) branchOffsetLong() : shortOperand();
    }

    private long branchOffsetLong() {
        long v = 0;
        for (int i = 0; i < 4; i++) {
            v = (v << 8) | operandByte(i);
        }
        return v;
    }

    @Override
    public String toString() {
        return pc + ":" + getMnemonic();
    }
}
