package com.zifang.util.bc.bytecode.model.instruction;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法体指令序列
 * <p>
 * 把扁平的 code 字节数组解释成指令流，是写出、帧计算、反编译三处的共同入口。
 * switch 指令的变长操作数按 JVMS §6.5.tableswitch/lookupswitch 展开：
 * 操作数起点对齐到 4 字节边界（相对方法体首字节），之后是 default 与各 case 的 s4。
 */
public class InstructionSequence {

    private final byte[] code;

    public InstructionSequence(byte[] code) {
        this.code = code;
    }

    public byte[] code() {
        return code;
    }

    public int length() {
        return code.length;
    }

    /**
     * 展开整条指令流
     *
     * @return ListInstruction类型返回值
     */
    public List<Instruction> instructions() {
        List<Instruction> list = new ArrayList<>();
        int pc = 0;
        while (pc < code.length) {
            Instruction insn = at(pc);
            list.add(insn);
            pc = insn.getNextPc();
        }
        return list;
    }

    /**
     * 取 pc 处的指令
     *
     * @param pc 方法体内偏移
     * @return Instruction类型返回值
     */
    public Instruction at(int pc) {
        int opcode = code[pc] & 0xFF;
        int operandOffset = pc + 1;
        int operandSize = Opcodes.operandSize(opcode);
        if (operandSize == -1) {
            // tableswitch / lookupswitch：操作数起点对齐到 4 字节边界
            int aligned = (pc + 4) & ~3;
            operandOffset = aligned;
            operandSize = switchOperandSize(aligned, opcode);
        } else if (operandSize == -2) {
            // wide：紧随其后的被修饰操作码决定是 u2 还是 u2+u4
            int widened = code[pc + 1] & 0xFF;
            operandOffset = pc + 2;
            operandSize = (widened == Opcodes.IINC || widened == Opcodes.RET) ? 4 : 2;
        }
        return new Instruction(code, pc, opcode, operandOffset, operandSize,
                operandOffset + operandSize);
    }

    private int switchOperandSize(int aligned, int opcode) {
        // default s4 恒占 4 字节，其后按指令类型展开
        int n = 4;
        if (opcode == Opcodes.TABLESWITCH) {
            n += 4;
            int low = readInt(aligned + 4);
            int high = readInt(aligned + 8);
            n += (high - low + 1) * 4;
        } else {
            n += 4;
            int npairs = readInt(aligned + 4);
            n += npairs * 8;
        }
        return n;
    }

    private int readInt(int offset) {
        return (code[offset] & 0xFF) << 24
                | (code[offset + 1] & 0xFF) << 16
                | (code[offset + 2] & 0xFF) << 8
                | (code[offset + 3] & 0xFF);
    }

    /**
     * 取某条 switch 指令的全部跳转目标（default + 各 case 的绝对偏移）
     *
     * @param insn switch 指令
     * @return int[]类型返回值
     */
    public int[] switchTargets(Instruction insn) {
        int pc = insn.getPc();
        int aligned = insn.getOperandOffset();
        List<Integer> targets = new ArrayList<>();
        targets.add(pc + readInt(aligned));
        if (insn.getOpcode() == Opcodes.TABLESWITCH) {
            int low = readInt(aligned + 4);
            int high = readInt(aligned + 8);
            for (int i = 0; i <= high - low; i++) {
                targets.add(pc + readInt(aligned + 12 + i * 4));
            }
        } else {
            int npairs = readInt(aligned + 4);
            for (int i = 0; i < npairs; i++) {
                targets.add(pc + readInt(aligned + 8 + i * 8 + 4));
            }
        }
        int[] result = new int[targets.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = targets.get(i);
        }
        return result;
    }

    /**
     * 收集所有跳转目标（含 switch 的 default 与各 case）
     *
     * @return int[]类型返回值
     */
    public int[] branchTargets() {
        List<Integer> targets = new ArrayList<>();
        int pc = 0;
        while (pc < code.length) {
            Instruction insn = at(pc);
            if (Opcodes.isConditionalJump(insn.getOpcode())
                    || (insn.getOpcode() == Opcodes.GOTO || insn.getOpcode() == Opcodes.GOTO_W)) {
                targets.add(pc + insn.branchTarget());
            } else if (insn.getOpcode() == Opcodes.TABLESWITCH) {
                int aligned = insn.getOperandOffset();
                targets.add(pc + readInt(aligned));
                int low = readInt(aligned + 4);
                int high = readInt(aligned + 8);
                for (int i = 0; i <= high - low; i++) {
                    targets.add(pc + readInt(aligned + 12 + i * 4));
                }
            } else if (insn.getOpcode() == Opcodes.LOOKUPSWITCH) {
                int aligned = insn.getOperandOffset();
                targets.add(pc + readInt(aligned));
                int npairs = readInt(aligned + 4);
                for (int i = 0; i < npairs; i++) {
                    targets.add(pc + readInt(aligned + 8 + i * 8 + 4));
                }
            }
            pc = insn.getNextPc();
        }
        int[] result = new int[targets.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = targets.get(i);
        }
        return result;
    }
}
