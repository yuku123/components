package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 基本块
 * <p>
 * 块内指令线性可达、只有一个入口；块尾是一条控制转移指令或顺序落到下一块。
 * 起止 pc 均为闭区间，{@link #getEndPc()} 是块内最后一条指令的起点。
 */
public class BasicBlock {

    /**
     * 块的控制流形态，结构化时按形态选模板。
     */
    public enum Kind {
        /** 顺序块，尾后直接接下一块 */
        STRAIGHT,
        /** 条件分支块，尾是 if* 系列 */
        CONDITIONAL,
        /** 无条件跳转块，尾是 goto */
        GOTO,
        /** switch 分发块 */
        SWITCH,
        /** 返回块 */
        RETURN,
        /** 抛异常块 */
        THROW
    }

    private final int id;
    private final int startPc;
    private int endPc;
    private final List<Instruction> instructions = new ArrayList<>();
    private final Set<Integer> successors = new LinkedHashSet<>();
    private final Set<Integer> predecessors = new LinkedHashSet<>();
    private Kind kind = Kind.STRAIGHT;

    public BasicBlock(int id, int startPc, int endPc) {
        this.id = id;
        this.startPc = startPc;
        this.endPc = endPc;
    }

    public int getId() {
        return id;
    }

    public int getStartPc() {
        return startPc;
    }

    public int getEndPc() {
        return endPc;
    }

    public void setEndPc(int endPc) {
        this.endPc = endPc;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void addInstruction(Instruction insn) {
        instructions.add(insn);
    }

    public Instruction getLastInstruction() {
        return instructions.isEmpty() ? null : instructions.get(instructions.size() - 1);
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    /**
     * 块尾之后的目标 pc 集合（已换算成绝对偏移）
     */
    public Set<Integer> getSuccessors() {
        return successors;
    }

    public void addSuccessor(int pc) {
        successors.add(pc);
    }

    public Set<Integer> getPredecessors() {
        return predecessors;
    }

    public void addPredecessor(int pc) {
        predecessors.add(pc);
    }

    /**
     * 块内指令总字节数
     */
    public int size() {
        return instructions.isEmpty() ? 0
                : instructions.get(instructions.size() - 1).getNextPc() - startPc;
    }

    /**
     * 尾指令是否把控制权交给本块（回边由结构化阶段识别）
     */
    public boolean fallsThrough() {
        if (instructions.isEmpty()) {
            return false;
        }
        int opcode = getLastInstruction().getOpcode();
        return !Opcodes.isJump(opcode) && !Opcodes.isTerminator(opcode);
    }

    @Override
    public String toString() {
        return "B" + id + "[" + startPc + ".." + endPc + "/" + kind + "]";
    }
}
