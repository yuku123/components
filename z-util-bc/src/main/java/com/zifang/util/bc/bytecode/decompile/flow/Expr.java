package com.zifang.util.bc.bytecode.decompile.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式节点
 * <p>
 * 栈机模拟把「压栈—出栈」配对成树之后得到的中间表示。只覆盖本仓能给出确定源码的
 * 运算；遇到成对不上的残留值退化成 {@link Unknown} 节点渲染成注释，不猜。
 */
public abstract class Expr {

    /**
     * 渲染成 Java 源码片段
     */
    public abstract String toJava();

    @Override
    public String toString() {
        return toJava();
    }

    /**
     * 字面量：数字、布尔、字符、字符串、null、类引用
     */
    public static class Literal extends Expr {
        private final String text;

        public Literal(String text) {
            this.text = text;
        }

        public static Literal of(boolean value) {
            return new Literal(value ? "true" : "false");
        }

        public static Literal ofNumber(long value) {
            return new Literal(String.valueOf(value));
        }

        @Override
        public String toJava() {
            return text;
        }
    }

    /**
     * 未初始化引用：null、this、类字面量、超级调用目标
     */
    public static class Ref extends Expr {
        private final String name;

        public Ref(String name) {
            this.name = name;
        }

        public static Ref nullRef() {
            return new Ref("null");
        }

        public static Ref thisRef() {
            return new Ref("this");
        }

        @Override
        public String toJava() {
            return name;
        }
    }

    /**
     * 局部变量（槽位或参数）
     */
    public static class Local extends Expr {
        private final String name;
        private final String type;

        public Local(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toJava() {
            return name;
        }
    }

    /**
     * 字段访问：宿主为 null 表示静态字段
     */
    public static class Field extends Expr {
        private final Expr owner;
        private final String name;

        public Field(Expr owner, String name) {
            this.owner = owner;
            this.name = name;
        }

        @Override
        public String toJava() {
            return owner == null ? name : owner.toJava() + "." + name;
        }
    }

    /**
     * 数组下标
     */
    public static class ArrayAccess extends Expr {
        private final Expr array;
        private final Expr index;

        public ArrayAccess(Expr array, Expr index) {
            this.array = array;
            this.index = index;
        }

        @Override
        public String toJava() {
            return array.toJava() + "[" + index.toJava() + "]";
        }
    }

    /**
     * 二元运算，中缀渲染；操作数自带括号信息由调用方负责
     */
    public static class Binary extends Expr {
        private final String operator;
        private final Expr left;
        private final Expr right;

        public Binary(String operator, Expr left, Expr right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toJava() {
            return left.toJava() + " " + operator + " " + right.toJava();
        }
    }

    /**
     * 一元运算
     */
    public static class Unary extends Expr {
        private final String operator;
        private final Expr operand;

        public Unary(String operator, Expr operand) {
            this.operator = operator;
            this.operand = operand;
        }

        @Override
        public String toJava() {
            return operator + operand.toJava();
        }
    }

    /**
     * 类型转换
     */
    public static class Cast extends Expr {
        private final String type;
        private final Expr operand;

        public Cast(String type, Expr operand) {
            this.type = type;
            this.operand = operand;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toJava() {
            return "(" + type + ") " + operand.toJava();
        }
    }

    /**
     * instanceof 判定
     */
    public static class InstanceOf extends Expr {
        private final Expr operand;
        private final String type;

        public InstanceOf(Expr operand, String type) {
            this.operand = operand;
            this.type = type;
        }

        @Override
        public String toJava() {
            return operand.toJava() + " instanceof " + type;
        }
    }

    /**
     * 方法调用；owner 为 null 表示静态调用
     */
    public static class Invoke extends Expr {
        private final Expr owner;
        private final String name;
        private final List<Expr> args;

        public Invoke(Expr owner, String name, List<Expr> args) {
            this.owner = owner;
            this.name = name;
            this.args = args == null ? new ArrayList<Expr>() : args;
        }

        public List<Expr> getArgs() {
            return args;
        }

        @Override
        public String toJava() {
            StringBuilder sb = new StringBuilder();
            if (owner != null) {
                sb.append(owner.toJava()).append('.');
            }
            sb.append(name).append('(');
            for (int i = 0; i < args.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(args.get(i).toJava());
            }
            return sb.append(')').toString();
        }
    }

    /**
     * 对象实例化。抽象类/接口走父类构造器，源码里看不到无参构造，但那是 Java 侧的事，
     * 这里只保证字节码语义可读。
     */
    public static class New extends Expr {
        private final String type;
        private final List<Expr> args;

        public New(String type, List<Expr> args) {
            this.type = type;
            this.args = args == null ? new ArrayList<Expr>() : args;
        }

        public String getType() {
            return type;
        }

        public List<Expr> getArgs() {
            return args;
        }

        @Override
        public String toJava() {
            StringBuilder sb = new StringBuilder("new ").append(type).append('(');
            for (int i = 0; i < args.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(args.get(i).toJava());
            }
            return sb.append(')').toString();
        }
    }

    /**
     * 数组创建：newarray / anewarray / multianewarray
     */
    public static class NewArray extends Expr {
        private final String type;
        private final List<Expr> dimensions;

        public NewArray(String type, List<Expr> dimensions) {
            this.type = type;
            this.dimensions = dimensions == null ? new ArrayList<Expr>() : dimensions;
        }

        @Override
        public String toJava() {
            StringBuilder sb = new StringBuilder("new ").append(type);
            for (Expr dim : dimensions) {
                sb.append('[').append(dim.toJava()).append(']');
            }
            return sb.toString();
        }
    }

    /**
     * 模拟不出来（或本仓暂不支持）的值，一律渲染成注释而不是编一个看起来对的表达式。
     */
    public static class Unknown extends Expr {
        private final String note;

        public Unknown(String note) {
            this.note = note;
        }

        @Override
        public String toJava() {
            return "/* " + note + " */";
        }
    }
}
