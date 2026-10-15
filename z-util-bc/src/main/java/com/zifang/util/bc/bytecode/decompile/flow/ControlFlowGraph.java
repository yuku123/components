package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.InstructionSequence;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;
import com.zifang.util.bc.bytecode.model.readtype.U1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 控制流图
 * <p>
 * 块划分按 JVMS §4.10.1 的引导指令规则：方法体首指令、任何控制转移的目标、
 * 任何控制转移/异常处理边界之后的指令、以及异常处理器的入口。
 * 构建只做划分与连边，语义还原交给 {@link ExprBuilder} 与 {@link Structurer}。
 */
public class ControlFlowGraph {

    private final Code code;
    private final InstructionSequence sequence;
    private final Map<Integer, BasicBlock> blocks = new LinkedHashMap<>();
    private final Set<Integer> backEdgeTargets = new java.util.HashSet<>();

    public ControlFlowGraph(Code code) {
        this.code = code;
        this.sequence = new InstructionSequence(toByteArray(code.getCode()));
        build();
    }

    /**
     * 从方法体 code 属性建图
     *
     * @param code 方法体 Code 属性；方法无实现（抽象/原生）时返回 null
     * @return ControlFlowGraph类型返回值
     */
    public static ControlFlowGraph of(Code code) {
        return code == null || code.getCode().isEmpty() ? null : new ControlFlowGraph(code);
    }

    private static byte[] toByteArray(List<U1> bytes) {
        byte[] out = new byte[bytes.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = bytes.get(i).value;
        }
        return out;
    }

    private void build() {
        List<Instruction> insns = sequence.instructions();
        if (insns.isEmpty()) {
            return;
        }
        Set<Integer> leaders = collectLeaders(insns);
        int nextId = 0;
        BasicBlock current = null;
        for (Instruction insn : insns) {
            if (current == null || leaders.contains(insn.getPc())) {
                if (current != null) {
                    classify(current);
                }
                current = new BasicBlock(nextId++, insn.getPc(), insn.getPc());
                blocks.put(insn.getPc(), current);
            }
            current.addInstruction(insn);
            current.setEndPc(insn.getPc());
        }
        classify(current);
        link();
    }

    /**
     * 收集引导指令 pc：入口、跳转目标、转移之后的下一条、异常处理器入口
     */
    private Set<Integer> collectLeaders(List<Instruction> insns) {
        Set<Integer> leaders = new java.util.LinkedHashSet<>();
        leaders.add(0);
        for (Instruction insn : insns) {
            int opcode = insn.getOpcode();
            if (Opcodes.isConditionalJump(opcode)) {
                int target = insn.getPc() + insn.branchTarget();
                leaders.add(target);
                leaders.add(insn.getNextPc());
                if (target < insn.getPc()) {
                    backEdgeTargets.add(target);
                }
            } else if (Opcodes.isUnconditionalJump(opcode)) {
                int target = insn.getPc() + insn.branchTarget();
                leaders.add(target);
                if (target < insn.getPc()) {
                    backEdgeTargets.add(target);
                }
                if (insn.getNextPc() < sequence.length()) {
                    leaders.add(insn.getNextPc());
                }
            } else if (opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH) {
                for (int target : sequence.switchTargets(insn)) {
                    leaders.add(target);
                }
                if (insn.getNextPc() < sequence.length()) {
                    leaders.add(insn.getNextPc());
                }
            }
        }
        for (Code.ExceptionInfo ei : code.getExceptionTable()) {
            leaders.add(ei.getHandlerPc().value & 0xFFFF);
        }
        leaders.removeIf(pc -> pc < 0 || pc >= sequence.length());
        return leaders;
    }

    private void classify(BasicBlock block) {
        Instruction last = block.getLastInstruction();
        if (last == null) {
            return;
        }
        int opcode = last.getOpcode();
        if (opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH) {
            block.setKind(BasicBlock.Kind.SWITCH);
        } else if (Opcodes.isConditionalJump(opcode)) {
            block.setKind(BasicBlock.Kind.CONDITIONAL);
        } else if (Opcodes.isUnconditionalJump(opcode)) {
            block.setKind(BasicBlock.Kind.GOTO);
        } else if (opcode == Opcodes.ATHROW) {
            block.setKind(BasicBlock.Kind.THROW);
        } else if (Opcodes.isTerminator(opcode)) {
            block.setKind(BasicBlock.Kind.RETURN);
        } else {
            block.setKind(BasicBlock.Kind.STRAIGHT);
        }
    }

    /**
     * 连边：正常控制转移 + 顺序落块 + 异常边（受保护区内的块连到处理器）
     */
    private void link() {
        List<BasicBlock> all = getBlocks();
        for (BasicBlock block : all) {
            Instruction last = block.getLastInstruction();
            if (last == null) {
                continue;
            }
            int opcode = last.getOpcode();
            if (Opcodes.isConditionalJump(opcode) || Opcodes.isUnconditionalJump(opcode)) {
                addEdge(block, last.getPc() + last.branchTarget());
                if (Opcodes.isConditionalJump(opcode) && last.getNextPc() < sequence.length()) {
                    addEdge(block, last.getNextPc());
                }
            } else if (opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH) {
                for (int target : sequence.switchTargets(last)) {
                    addEdge(block, target);
                }
            } else if (last.getNextPc() < sequence.length()) {
                addEdge(block, last.getNextPc());
            }
        }
        for (Code.ExceptionInfo ei : code.getExceptionTable()) {
            BasicBlock handler = blocks.get(ei.getHandlerPc().value);
            if (handler == null) {
                continue;
            }
            int start = ei.getStartPc().value & 0xFFFF;
            int end = ei.getEndPc().value & 0xFFFF;
            for (BasicBlock block : all) {
                if (block.getStartPc() >= start && block.getEndPc() < end) {
                    handler.getPredecessors().add(block.getStartPc());
                }
            }
        }
    }

    private void addEdge(BasicBlock from, int targetPc) {
        BasicBlock target = blocks.get(targetPc);
        if (target == null || target == from) {
            return;
        }
        from.addSuccessor(targetPc);
        target.addPredecessor(from.getStartPc());
    }

    public Code getCode() {
        return code;
    }

    public InstructionSequence getSequence() {
        return sequence;
    }

    public int length() {
        return sequence.length();
    }

    /**
     * 全部基本块，按 pc 升序
     */
    public List<BasicBlock> getBlocks() {
        List<BasicBlock> list = new ArrayList<>(blocks.values());
        list.sort((a, b) -> Integer.compare(a.getStartPc(), b.getStartPc()));
        return list;
    }

    /**
     * 回边目标集合：被更靠后的跳转指到的 pc，即循环头
     */
    public Set<Integer> getBackEdgeTargets() {
        return backEdgeTargets;
    }

    public BasicBlock blockAt(int pc) {
        return blocks.get(pc);
    }

    /**
     * 入口块（方法体首指令所在块）
     */
    public BasicBlock entry() {
        return blocks.get(0);
    }
}
