package com.zifang.util.bc.bytecode;

import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.InstructionSequence;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 指令序列展开
 * <p>
 * 旧实现用「助记符字符串 → 操作数字节数」的 switch 来定位操作数，
 * 其中 if_acmp&lt;cond&gt;、jsr_wbranch 这类占位 key 永远匹配不上，
 * invokeinterface 与 invokedynamic 的操作数长度也算错。改成按 opcode 直接查表后钉死。
 */
class InstructionSequenceTest {

    @Test
    void computesOperandSizeForInvokeinterfaceIncludingCountAndZero() {
        assertEquals(4, Opcodes.operandSize(Opcodes.INVOKEINTERFACE));
        assertEquals(4, Opcodes.operandSize(Opcodes.INVOKEDYNAMIC));
        assertEquals(2, Opcodes.operandSize(Opcodes.INVOKEVIRTUAL));
    }

    @Test
    void marksConstantIndexBearingOpcodes() {
        assertTrue(Opcodes.hasConstantIndex(Opcodes.LDC));
        assertTrue(Opcodes.hasConstantIndex(Opcodes.LDC_W));
        assertTrue(Opcodes.hasConstantIndex(Opcodes.GETSTATIC));
        assertTrue(Opcodes.hasConstantIndex(Opcodes.INVOKESTATIC));
        assertTrue(Opcodes.hasConstantIndex(Opcodes.MULTIANEWARRAY));
    }

    @Test
    void negatesConditionalJump() {
        assertEquals(Opcodes.IFEQ, Opcodes.negateCondition(0x9a));
        assertEquals(0xa6, Opcodes.negateCondition(Opcodes.IF_ACMPEQ));
        assertEquals(Opcodes.IFNONNULL, Opcodes.negateCondition(Opcodes.IFNULL));
        assertEquals(-1, Opcodes.negateCondition(Opcodes.RETURN));
    }

    @Test
    void expandsVariableLengthSwitchOperands() {
        // tableswitch：操作数对齐到 4 字节边界，default + low + high + 3 个 case
        byte[] code = new byte[64];
        int pc = 1;
        code[0] = (byte) Opcodes.NOP;
        code[pc] = (byte) Opcodes.TABLESWITCH;
        int aligned = (pc + 4) & ~3;
        code[aligned] = 0;      // default 高位
        code[aligned + 3] = 16; // default 低位 = 16
        code[aligned + 4] = 0;  // low 高位
        code[aligned + 7] = 1;  // low 低位
        code[aligned + 8] = 0;  // high 高位
        code[aligned + 11] = 3; // high 低位
        for (int i = 0; i < 3; i++) {
            code[aligned + 12 + i * 4 + 3] = (byte) (20 + i);
        }

        InstructionSequence seq = new InstructionSequence(code);
        Instruction tableswitch = seq.at(pc);

        assertEquals(Opcodes.TABLESWITCH, tableswitch.getOpcode());
        // 操作数 = 4(default) + 4(low/high) + 12(3 个 case)，起点为对齐后的 aligned
        assertEquals(aligned + 20 - pc, tableswitch.getLength());

        // 尾部其余字节是 nop，这里只校验前两条的边界
        List<Instruction> all = seq.instructions();
        assertEquals(0, all.get(0).getPc());
        assertEquals(Opcodes.NOP, all.get(0).getOpcode());
        assertEquals(pc, all.get(1).getPc());
        assertEquals(Opcodes.TABLESWITCH, all.get(1).getOpcode());
    }

    @Test
    void collectsSwitchBranchTargets() {
        byte[] code = new byte[64];
        int pc = 0;
        code[pc] = (byte) Opcodes.TABLESWITCH;
        int aligned = (pc + 4) & ~3;
        code[aligned + 3] = 10;                 // default
        code[aligned + 7] = 1;                  // low
        code[aligned + 11] = 2;                 // high
        code[aligned + 12 + 3] = 11;            // case 1
        code[aligned + 12 + 4 + 3] = 12;        // case 2

        int[] targets = new InstructionSequence(code).branchTargets();
        assertEquals(3, targets.length);
        assertEquals(10, targets[0]);
        assertEquals(11, targets[1]);
        assertEquals(12, targets[2]);
    }
}
