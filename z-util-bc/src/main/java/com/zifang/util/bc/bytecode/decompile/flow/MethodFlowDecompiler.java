package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.instruction.Instruction;

import java.util.List;

/**
 * 控制流反编译门面
 * <p>
 * 一次调用走完：Code 属性 → {@link ControlFlowGraph} → {@link ExprBuilder} 栈模拟
 * → {@link Structurer} 结构化 → Java 源码。
 */
public class MethodFlowDecompiler {

    private MethodFlowDecompiler() {
    }

    /**
     * 把方法体还原成 Java 源码（只含方法体，不含签名）
     *
     * @param code        方法体 Code 属性；无实现时返回空串
     * @param thisClassName 当前类内部名；静态方法传 null
     * @return Java 源码字符串
     */
    public static String decompileBody(Code code, String thisClassName) {
        Stmt.Block block = structure(code, thisClassName);
        StringBuilder sb = new StringBuilder();
        block.render(sb, 0);
        return sb.toString();
    }

    /**
     * 结构化整个方法体
     *
     * @param code          方法体 Code 属性
     * @param thisClassName 当前类内部名；静态方法传 null
     * @return Stmt.Block类型返回值，方法无实现时是空块
     */
    public static Stmt.Block structure(Code code, String thisClassName) {
        Stmt.Block result = new Stmt.Block();
        ControlFlowGraph cfg = ControlFlowGraph.of(code);
        if (cfg == null) {
            return result;
        }
        List<Instruction> insns = cfg.getSequence().instructions();
        LocalVarNames locals = LocalVarNames.of(code, insns, thisClassName);
        ExprBuilder exprs = new ExprBuilder(cfg, locals);
        result.getBody().addAll(new Structurer(cfg, exprs).structure(cfg.entry()));
        return result;
    }

    /**
     * 只建控制流图，供图分析类用途
     *
     * @param code 方法体 Code 属性
     * @return ControlFlowGraph类型返回值，方法无实现时返回 null
     */
    public static ControlFlowGraph graph(Code code) {
        return ControlFlowGraph.of(code);
    }

    /**
     * 取方法体 Code 属性
     *
     * @param attributes 方法的属性表
     * @return Code类型返回值，没有实现时返回 null
     */
    public static Code findCode(List<AbstractAttribute> attributes) {
        if (attributes == null) {
            return null;
        }
        for (AbstractAttribute attribute : attributes) {
            if (attribute instanceof Code) {
                return (Code) attribute;
            }
        }
        return null;
    }
}
