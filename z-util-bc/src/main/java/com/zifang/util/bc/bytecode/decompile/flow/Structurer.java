package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 结构化
 * <p>
 * 把基本块图还原成 if/else、while、do-while、for、switch。做法是沿控制流线性走，
 * 遇到控制转移就按形态套模板：
 * <ul>
 *   <li>条件块：顺序落下块当 then 分支（条件取反），跳转目标是汇合点或 else 分支</li>
 *   <li>回边（目标已在当前路径上）：头块带条件是 while，尾块带条件是 do-while</li>
 *   <li>无条件跳到条件块：javac 给 for 与 while 编的是同一形状；跳之前恰好一条声明、
 *       且体末尾是对该变量的纯更新时还原成 for，否则是 while</li>
 *   <li>switch 块：各 case 从目标块开始，到公共出口停止，出口渲染成 break</li>
 * </ul>
 * 套不上的形状（多入口、goto 拼出的状态机）不硬套，落到注释里。
 */
public class Structurer {

    private final ControlFlowGraph cfg;
    private final ExprBuilder exprs;
    /**
     * 方法体块数的两倍；结构化是递归下降，超出这个深度说明形状套不上，
     * 与其无限递归不如就地放弃，渲染成注释。
     */
    private static final int DEPTH_BUDGET = 512;

    private final Set<Integer> onPath = new HashSet<>();
    private int preStart = -1;
    private int depthBudget = DEPTH_BUDGET;

    public Structurer(ControlFlowGraph cfg, ExprBuilder exprs) {
        this.cfg = cfg;
        this.exprs = exprs;
    }

    /**
     * 结构化整个方法体
     *
     * @param entry 入口块
     * @return Stmt列表类型返回值
     */
    public List<Stmt> structure(BasicBlock entry) {
        depthBudget = Math.max(DEPTH_BUDGET, cfg.getBlocks().size() * 2);
        List<Stmt> out = new ArrayList<>();
        emitInto(out, entry, new LinkedHashSet<Integer>());
        return out;
    }

    private List<Stmt> emitFrom(BasicBlock start, Set<Integer> stopAt) {
        List<Stmt> out = new ArrayList<>();
        if (start != null) {
            emitInto(out, start, stopAt);
        }
        return out;
    }

    private void emitInto(List<Stmt> out, BasicBlock start, Set<Integer> stopAt) {
        if (depthBudget-- <= 0) {
            out.add(new Stmt.Comment("控制流过深，此处未展开"));
            return;
        }
        BasicBlock cur = start;
        // 本次线性走过的块；多后继块的后继取自 Set，顺序不定，可能兜回已走过的块
        Set<Integer> walked = new HashSet<>();
        while (cur != null && !stopAt.contains(cur.getStartPc())) {
            if (!walked.add(cur.getStartPc())) {
                out.add(new Stmt.Comment("控制流在此处成环，未展开"));
                return;
            }
            if (!onPath.add(cur.getStartPc())) {
                return;
            }
            switch (cur.getKind()) {
                case CONDITIONAL:
                    cur = emitConditional(out, cur, stopAt);
                    break;
                case GOTO:
                    cur = emitGoto(out, cur, stopAt);
                    break;
                case SWITCH:
                    emitSwitch(out, cur, stopAt);
                    onPath.remove(cur.getStartPc());
                    return;
                case RETURN:
                case THROW:
                    out.addAll(exprs.run(cur.getInstructions()));
                    onPath.remove(cur.getStartPc());
                    return;
                default:
                    preStart = out.size();
                    out.addAll(exprs.run(cur.getInstructions()));
                    BasicBlock next = cur.getSuccessors().isEmpty()
                            ? null : cfg.blockAt(singleSuccessor(cur));
                    onPath.remove(cur.getStartPc());
                    cur = next;
            }
        }
        if (start != null) {
            onPath.remove(start.getStartPc());
        }
    }

    /**
     * 直行块的后继：正常只有顺序落下的那一个。出现多个时取偏移最小且在块之后的那个，
     * 免得 Set 顺序不定把线性走带回了前面的块。
     */
    private static int singleSuccessor(BasicBlock block) {
        int best = -1;
        for (Integer succ : block.getSuccessors()) {
            if (succ > block.getStartPc() && (best < 0 || succ < best)) {
                best = succ;
            }
        }
        return best < 0 ? block.getSuccessors().iterator().next() : best;
    }

    /**
     * 模拟块内除尾指令外的部分：条件的比较值、switch 的选择值都是它们压上去的
     */
    private List<Stmt> runPrefix(BasicBlock block) {
        List<Instruction> insns = block.getInstructions();
        if (insns.size() <= 1) {
            return new ArrayList<>();
        }
        return exprs.run(insns.subList(0, insns.size() - 1));
    }

    // ------------------------------------------------------------ 条件分支

    /**
     * javac 把 if 编成「条件成立就跳过 then 体」，所以顺序落下块才是 then，条件要取反；
     * 跳转目标是汇合点（外层停在这里），不是汇合点时它就是 else 体。
     */
    private BasicBlock emitConditional(List<Stmt> out, BasicBlock block, Set<Integer> stopAt) {
        Instruction last = block.getLastInstruction();
        int taken = last.getPc() + last.branchTarget();
        int fallThrough = last.getNextPc();

        if (taken < last.getPc() && !stopAt.contains(taken)) {
            // 条件跳回更早的偏移，条件在体之后，这是 do-while 的尾
            return emitDoWhile(out, block, taken, runPrefix(block));
        }
        if (cfg.getBackEdgeTargets().contains(block.getStartPc())) {
            // 本块被更靠后的跳转指回来，是循环头；条件在体之前，这是 while
            return emitWhile(out, block, taken, runPrefix(block));
        }

        out.addAll(runPrefix(block));
        List<Expr> operands = exprs.popConditionOperands(last.getOpcode());
        onPath.remove(block.getStartPc());

        if (stopAt.contains(taken)) {
            // 条件成立就跳出当前结构，这里只剩顺序落下的一条路
            return cfg.blockAt(fallThrough);
        }

        Stmt.If ifStmt = new Stmt.If(
                ExprBuilder.condition(last.getOpcode(), operands, false), new Stmt.Block());
        BasicBlock thenBlock = cfg.blockAt(fallThrough);
        if (thenBlock != null) {
            Set<Integer> thenStop = new LinkedHashSet<>(stopAt);
            thenStop.add(taken);
            ifStmt.getThenBlock().getBody().addAll(emitFrom(thenBlock, thenStop));
        }
        onPath.add(block.getStartPc());
        BasicBlock elseBlock = cfg.blockAt(taken);
        if (elseBlock != null) {
            Stmt.Block elseBody = new Stmt.Block();
            Set<Integer> elseStop = new LinkedHashSet<>(stopAt);
            elseStop.remove(taken);
            elseBody.getBody().addAll(emitFrom(elseBlock, elseStop));
            if (!elseBody.getBody().isEmpty()) {
                ifStmt.setElseBlock(elseBody);
            }
        }
        onPath.remove(block.getStartPc());
        preStart = -1;
        out.add(ifStmt);
        return null;
    }

    /**
     * 条件在体之前的回边：while。条件块是头，顺序落下块是体，跳转目标是出口。
     */
    private BasicBlock emitWhile(List<Stmt> out, BasicBlock header, int exitPc,
                                 List<Stmt> prefix) {
        Instruction last = header.getLastInstruction();
        List<Expr> operands = exprs.popConditionOperands(last.getOpcode());
        // 体是顺序落下那条路，跳转目标是出口，所以循环条件是跳转条件的反面
        Expr condition = ExprBuilder.condition(last.getOpcode(), operands, false);
        BasicBlock body = cfg.blockAt(last.getNextPc());
        List<Stmt> bodyStmts = new ArrayList<>(prefix);
        if (body != null) {
            Set<Integer> stop = new LinkedHashSet<>();
            stop.add(header.getStartPc());
            bodyStmts.addAll(emitFrom(body, stop));
        }
        onPath.remove(header.getStartPc());
        Stmt.For forStmt = asForLoop(out, condition, bodyStmts);
        if (forStmt != null) {
            out.add(forStmt);
        } else {
            Stmt.While loop = new Stmt.While(condition);
            loop.getBody().getBody().addAll(bodyStmts);
            out.add(loop);
        }
        return cfg.blockAt(exitPc);
    }

    /**
     * 条件在体之后的回边：do-while。体就是本块跳之前的那些指令，顺序落下块是出口。
     */
    private BasicBlock emitDoWhile(List<Stmt> out, BasicBlock latch, int bodyPc,
                                   List<Stmt> prefix) {
        Instruction last = latch.getLastInstruction();
        List<Expr> operands = exprs.popConditionOperands(last.getOpcode());
        Stmt.DoWhile loop = new Stmt.DoWhile(
                ExprBuilder.condition(last.getOpcode(), operands, true));
        List<Stmt> body = new ArrayList<>(prefix);
        BasicBlock bodyStart = cfg.blockAt(bodyPc);
        if (bodyStart != null && bodyStart.getStartPc() != latch.getStartPc()) {
            Set<Integer> stop = new LinkedHashSet<>();
            stop.add(latch.getStartPc());
            body.addAll(emitFrom(bodyStart, stop));
        }
        loop.getBody().getBody().addAll(body);
        onPath.remove(latch.getStartPc());
        out.add(loop);
        return cfg.blockAt(last.getNextPc());
    }

    // ------------------------------------------------------------ 无条件跳转

    /**
     * 无条件跳转有三种身份：跳过一段（顺序落下去）、跳出当前结构（break/continue）、
     * 跳到条件块形成回边（for/while）。按目标块形态区分。
     */
    private BasicBlock emitGoto(List<Stmt> out, BasicBlock block, Set<Integer> stopAt) {
        List<Stmt> prefix = runPrefix(block);
        Instruction last = block.getLastInstruction();
        int target = last.getPc() + last.branchTarget();
        BasicBlock targetBlock = cfg.blockAt(target);
        onPath.remove(block.getStartPc());

        if (targetBlock == null || target == block.getStartPc() || stopAt.contains(target)) {
            out.addAll(prefix);
            return null;
        }
        if (onPath.contains(target)) {
            out.addAll(prefix);
            out.add(new Stmt.Jump("continue"));
            return null;
        }
        out.addAll(prefix);
        return targetBlock;
    }

    /**
     * javac 把 {@code while (c) {...}} 与 {@code for (init; c; upd) {...}} 编成同一形状，
     * 字节码里分不出来。这里按「循环前恰好一条声明 + 体末尾是对它的纯更新」还原成 for，
     * 两种写法语义等价，认错了也只是排版差异；信号不足时一律退回 while。
     *
     * @return 可用的 for 节点；信号不足返回 null
     */
    private Stmt.For asForLoop(List<Stmt> out, Expr condition, List<Stmt> bodyStmts) {
        if (preStart < 0 || out.size() - preStart != 1
                || !(out.get(preStart) instanceof Stmt.Decl)) {
            return null;
        }
        String loopVar = ((Stmt.Decl) out.get(preStart)).getName();
        int cut = bodyStmts.size();
        while (cut > 0) {
            Stmt stmt = bodyStmts.get(cut - 1);
            if (!(stmt instanceof Stmt.Assign)
                    || !updatesOnly(((Stmt.Assign) stmt).getTarget(), loopVar)) {
                break;
            }
            cut--;
        }
        if (cut == bodyStmts.size()) {
            return null;
        }
        Stmt.For forStmt = new Stmt.For();
        forStmt.getInit().getBody().add(out.remove(preStart));
        forStmt.setCondition(condition);
        forStmt.getUpdate().getBody().addAll(bodyStmts.subList(cut, bodyStmts.size()));
        forStmt.getBody().getBody().addAll(bodyStmts.subList(0, cut));
        return forStmt;
    }

    private static boolean updatesOnly(Expr target, String loopVar) {
        return target instanceof Expr.Local && loopVar.equals(((Expr.Local) target).getName());
    }

    // ------------------------------------------------------------ switch

    private void emitSwitch(List<Stmt> out, BasicBlock block, Set<Integer> stopAt) {
        out.addAll(runPrefix(block));
        Instruction last = block.getLastInstruction();
        Expr selector = exprs.popSelector();
        boolean table = last.getOpcode() == Opcodes.TABLESWITCH;
        Stmt.Switch switchStmt = new Stmt.Switch(selector, table);
        onPath.remove(block.getStartPc());

        SwitchCases cases = SwitchCases.of(cfg, last);
        int follow = commonFollow(cases.allTargets);

        for (Integer key : cases.keys) {
            Stmt.Block body = new Stmt.Block();
            BasicBlock caseBlock = cfg.blockAt(cases.target(key));
            if (caseBlock != null && cases.target(key) != follow) {
                Set<Integer> stop = new LinkedHashSet<>(stopAt);
                if (follow >= 0) {
                    stop.add(follow);
                }
                // 别的 case 目标也是停靠点，避免 case 体跳进相邻 case
                stop.addAll(cases.allTargets);
                stop.remove(cases.target(key));
                body.getBody().addAll(emitFrom(caseBlock, stop));
            }
            switchStmt.addCase(key, body);
        }
        if (cases.defaultTarget != follow) {
            BasicBlock defaultBlock = cfg.blockAt(cases.defaultTarget);
            if (defaultBlock != null) {
                Stmt.Block body = new Stmt.Block();
                Set<Integer> stop = new LinkedHashSet<>(stopAt);
                if (follow >= 0) {
                    stop.add(follow);
                }
                body.getBody().addAll(emitFrom(defaultBlock, stop));
                switchStmt.setDefaultBlock(body);
            }
        }
        out.add(switchStmt);
        if (follow >= 0) {
            onPath.remove(follow);
        }
    }

    /**
     * switch 各分支的公共出口：所有 case 块后继里出现次数最多的那个；认不出返回 -1
     */
    private int commonFollow(Set<Integer> targets) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Integer target : targets) {
            BasicBlock caseBlock = cfg.blockAt(target);
            if (caseBlock == null) {
                continue;
            }
            for (int succ : caseBlock.getSuccessors()) {
                if (targets.contains(succ)) {
                    continue;
                }
                Integer prev = counts.get(succ);
                counts.put(succ, prev == null ? 1 : prev + 1);
            }
        }
        int best = 0;
        int follow = -1;
        for (Map.Entry<Integer, Integer> e : counts.entrySet()) {
            if (e.getValue() > best) {
                best = e.getValue();
                follow = e.getKey();
            }
        }
        return follow;
    }

    /**
     * switch 变长操作数里的 case 键值对；{@link com.zifang.util.bc.bytecode.model.instruction.InstructionSequence}
     * 只给了跳转目标，这里补出键
     */
    static final class SwitchCases {
        final List<Integer> keys = new ArrayList<>();
        final Map<Integer, Integer> targets = new HashMap<>();
        final Set<Integer> allTargets = new LinkedHashSet<>();
        int defaultTarget = -1;

        int target(int key) {
            Integer t = targets.get(key);
            return t == null ? -1 : t;
        }

        static SwitchCases of(ControlFlowGraph cfg, Instruction insn) {
            SwitchCases result = new SwitchCases();
            byte[] code = cfg.getSequence().code();
            int pc = insn.getPc();
            int aligned = insn.getOperandOffset();
            result.defaultTarget = pc + readInt(code, aligned);
            result.allTargets.add(result.defaultTarget);
            if (insn.getOpcode() == Opcodes.TABLESWITCH) {
                int low = readInt(code, aligned + 4);
                int high = readInt(code, aligned + 8);
                for (int i = 0; i <= high - low; i++) {
                    int key = low + i;
                    int target = pc + readInt(code, aligned + 12 + i * 4);
                    result.keys.add(key);
                    result.targets.put(key, target);
                    result.allTargets.add(target);
                }
            } else {
                int pairs = readInt(code, aligned + 4);
                for (int i = 0; i < pairs; i++) {
                    int key = readInt(code, aligned + 8 + i * 8);
                    int target = pc + readInt(code, aligned + 12 + i * 8);
                    result.keys.add(key);
                    result.targets.put(key, target);
                    result.allTargets.add(target);
                }
            }
            return result;
        }

        private static int readInt(byte[] code, int offset) {
            return (code[offset] & 0xFF) << 24
                    | (code[offset + 1] & 0xFF) << 16
                    | (code[offset + 2] & 0xFF) << 8
                    | (code[offset + 3] & 0xFF);
        }
    }
}
