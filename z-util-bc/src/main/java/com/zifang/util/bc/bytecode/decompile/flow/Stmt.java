package com.zifang.util.bc.bytecode.decompile.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 语句节点
 * <p>
 * 结构化阶段的产物：把基本块图还原成 if/else、while、do-while、for、switch、
 * try/catch 这些 Java 语法形态，之后渲染器只负责排版。
 */
public abstract class Stmt {

    /**
     * 渲染成 Java 源码（不含缩进，缩进由渲染器按嵌套深度加）
     */
    public abstract void render(StringBuilder out, int indent);

    protected static void indent(StringBuilder out, int indent) {
        for (int i = 0; i < indent; i++) {
            out.append('\t');
        }
    }

    protected static void renderList(List<Stmt> body, StringBuilder out, int indent) {
        if (body.isEmpty()) {
            indent(out, indent);
            out.append("// (empty)\n");
            return;
        }
        for (Stmt stmt : body) {
            stmt.render(out, indent);
        }
    }

    protected static String joinExprs(List<Expr> exprs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exprs.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(exprs.get(i).toJava());
        }
        return sb.toString();
    }

    /**
     * 顺序块
     */
    public static class Block extends Stmt {
        private final List<Stmt> body = new ArrayList<>();

        public List<Stmt> getBody() {
            return body;
        }

        public Block add(Stmt stmt) {
            body.add(stmt);
            return this;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("{\n");
            renderList(body, out, indent + 1);
            indent(out, indent);
            out.append("}\n");
        }
    }

    /**
     * 表达式语句（方法调用、自增等）
     */
    public static class ExprStmt extends Stmt {
        private final Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append(expr.toJava()).append(";\n");
        }
    }

    /**
     * 带类型的局部变量声明，可同时初始化
     */
    public static class Decl extends Stmt {
        private final String type;
        private final String name;
        private Expr init;

        public Decl(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public Decl(String type, String name, Expr init) {
            this(type, name);
            this.init = init;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public Expr getInit() {
            return init;
        }

        public void setInit(Expr init) {
            this.init = init;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append(type).append(' ').append(name);
            if (init != null) {
                out.append(" = ").append(init.toJava());
            }
            out.append(";\n");
        }
    }

    /**
     * 赋值。operator 为 "=" 或 "+=" 这类复合赋值，直接接在目标后面
     */
    public static class Assign extends Stmt {
        private final Expr target;
        private final String operator;
        private final Expr value;

        public Assign(Expr target, Expr value) {
            this(target, "=", value);
        }

        public Assign(Expr target, String operator, Expr value) {
            this.target = target;
            this.operator = operator;
            this.value = value;
        }

        public Expr getTarget() {
            return target;
        }

        public Expr getValue() {
            return value;
        }

        public String getOperator() {
            return operator;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append(target.toJava()).append(' ').append(operator).append(' ')
                    .append(value.toJava()).append(";\n");
        }
    }

    /**
     * if / else
     */
    public static class If extends Stmt {
        private final Expr condition;
        private final Block thenBlock;
        private Block elseBlock;

        public If(Expr condition, Block thenBlock) {
            this.condition = condition;
            this.thenBlock = thenBlock;
        }

        public Expr getCondition() {
            return condition;
        }

        public Block getThenBlock() {
            return thenBlock;
        }

        public Block getElseBlock() {
            return elseBlock;
        }

        public void setElseBlock(Block elseBlock) {
            this.elseBlock = elseBlock;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("if (").append(condition.toJava()).append(") {\n");
            renderList(thenBlock.getBody(), out, indent + 1);
            indent(out, indent);
            if (elseBlock == null || elseBlock.getBody().isEmpty()) {
                out.append("}\n");
            } else if (elseBlock.getBody().size() == 1 && elseBlock.getBody().get(0) instanceof If) {
                out.append("} else ");
                elseBlock.getBody().get(0).render(out, indent);
            } else {
                out.append("} else {\n");
                renderList(elseBlock.getBody(), out, indent + 1);
                indent(out, indent);
                out.append("}\n");
            }
        }
    }

    /**
     * while（回边指向头块）
     */
    public static class While extends Stmt {
        private final Expr condition;
        private final Block body = new Block();

        public While(Expr condition) {
            this.condition = condition;
        }

        public Block getBody() {
            return body;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("while (").append(condition.toJava()).append(") {\n");
            renderList(body.getBody(), out, indent + 1);
            indent(out, indent);
            out.append("}\n");
        }
    }

    /**
     * do-while（头块条件在体末）
     */
    public static class DoWhile extends Stmt {
        private final Expr condition;
        private final Block body = new Block();

        public DoWhile(Expr condition) {
            this.condition = condition;
        }

        public Block getBody() {
            return body;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("do {\n");
            renderList(body.getBody(), out, indent + 1);
            indent(out, indent);
            out.append("} while (").append(condition.toJava()).append(");\n");
        }
    }

    /**
     * for：init 是块级语句，update 是表达式列表
     */
    public static class For extends Stmt {
        private final Block init = new Block();
        private final Block update = new Block();
        private Expr condition;
        private final Block body = new Block();

        public Block getInit() {
            return init;
        }

        public Block getUpdate() {
            return update;
        }

        public Expr getCondition() {
            return condition;
        }

        public void setCondition(Expr condition) {
            this.condition = condition;
        }

        public Block getBody() {
            return body;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            StringBuilder head = new StringBuilder("for (");
            head.append(initText());
            head.append("; ").append(condition == null ? "" : condition.toJava());
            head.append("; ").append(updateText()).append(") {\n");
            out.append(head);
            renderList(body.getBody(), out, indent + 1);
            indent(out, indent);
            out.append("}\n");
        }

        private String initText() {
            StringBuilder sb = new StringBuilder();
            List<Stmt> stmts = init.getBody();
            for (int i = 0; i < stmts.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(stripSemicolon(stmts.get(i)));
            }
            return sb.toString();
        }

        private String updateText() {
            StringBuilder sb = new StringBuilder();
            List<Stmt> stmts = update.getBody();
            for (int i = 0; i < stmts.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(stripSemicolon(stmts.get(i)));
            }
            return sb.toString();
        }

        private String stripSemicolon(Stmt stmt) {
            StringBuilder sb = new StringBuilder();
            stmt.render(sb, 0);
            String text = sb.toString();
            return text.endsWith(";\n") ? text.substring(0, text.length() - 2) : text.trim();
        }
    }

    /**
     * switch：cases 保持字节码里的出现次序，defaultCases 为 null 时不渲染 default 标号
     */
    public static class Switch extends Stmt {
        private final Expr selector;
        private final Map<Integer, Block> cases = new java.util.LinkedHashMap<>();
        private Block defaultBlock;
        private List<Integer> order = new ArrayList<>();
        private boolean tableSwitch;

        public Switch(Expr selector, boolean tableSwitch) {
            this.selector = selector;
            this.tableSwitch = tableSwitch;
        }

        public Expr getSelector() {
            return selector;
        }

        public boolean isTableSwitch() {
            return tableSwitch;
        }

        public void addCase(int key, Block block) {
            cases.put(key, block);
            order.add(key);
        }

        public Map<Integer, Block> getCases() {
            return cases;
        }

        public List<Integer> getOrder() {
            return order;
        }

        public Block getDefaultBlock() {
            return defaultBlock;
        }

        public void setDefaultBlock(Block defaultBlock) {
            this.defaultBlock = defaultBlock;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("switch (").append(selector.toJava()).append(") {\n");
            for (Integer key : order) {
                indent(out, indent + 1);
                out.append("case ").append(key).append(":\n");
                renderList(cases.get(key).getBody(), out, indent + 2);
            }
            if (defaultBlock != null) {
                indent(out, indent + 1);
                out.append("default:\n");
                renderList(defaultBlock.getBody(), out, indent + 2);
            }
            indent(out, indent);
            out.append("}\n");
        }
    }

    /**
     * return
     */
    public static class Return extends Stmt {
        private final Expr value;

        public Return(Expr value) {
            this.value = value;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("return");
            if (value != null) {
                out.append(' ').append(value.toJava());
            }
            out.append(";\n");
        }
    }

    /**
     * throw
     */
    public static class Throw extends Stmt {
        private final Expr value;

        public Throw(Expr value) {
            this.value = value;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("throw ").append(value.toJava()).append(";\n");
        }
    }

    /**
     * break / continue
     */
    public static class Jump extends Stmt {
        private final String keyword;

        public Jump(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append(keyword).append(";\n");
        }
    }

    /**
     * catch 子句
     */
    public static class CatchClause {
        private final String type;
        private final String variable;
        private final Block body = new Block();

        public CatchClause(String type, String variable) {
            this.type = type;
            this.variable = variable;
        }

        public String getType() {
            return type;
        }

        public String getVariable() {
            return variable;
        }

        public Block getBody() {
            return body;
        }
    }

    /**
     * try/catch/finally
     */
    public static class Try extends Stmt {
        private final Block body = new Block();
        private final List<CatchClause> catches = new ArrayList<>();
        private Block finallyBlock;

        public Block getBody() {
            return body;
        }

        public List<CatchClause> getCatches() {
            return catches;
        }

        public Block getFinallyBlock() {
            return finallyBlock;
        }

        public void setFinallyBlock(Block finallyBlock) {
            this.finallyBlock = finallyBlock;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("try {\n");
            renderList(body.getBody(), out, indent + 1);
            for (CatchClause clause : catches) {
                indent(out, indent);
                out.append("} catch (").append(clause.getType()).append(' ')
                        .append(clause.getVariable()).append(") {\n");
                renderList(clause.getBody().getBody(), out, indent + 1);
            }
            if (finallyBlock != null) {
                indent(out, indent);
                out.append("} finally {\n");
                renderList(finallyBlock.getBody(), out, indent + 1);
                indent(out, indent);
                out.append("}\n");
            } else {
                indent(out, indent);
                out.append("}\n");
            }
        }
    }

    /**
     * 结构化阶段兜不住的部分，原样渲染成注释，不假装读懂
     */
    public static class Comment extends Stmt {
        private final String text;

        public Comment(String text) {
            this.text = text;
        }

        @Override
        public void render(StringBuilder out, int indent) {
            indent(out, indent);
            out.append("// ").append(text).append('\n');
        }
    }
}
