package com.zifang.util.strategy.rule;

/**
 * 对象化的条件表达。
 * <p>
 * 业务方用 {@link Conditions} 的工厂方法拼一棵 Java 对象树，
 * 执行时由 {@link #toDsl()} 翻译成 DSL 文本，交给 {@code z-util-expr-sql} 求值。
 * 这样条件有类型检查和 IDE 补全，同时不引第二套表达式引擎。
 */
public abstract class Condition {

    /** 翻译成 DSL 文本 */
    public abstract String toDsl();

    public final Condition and(Condition other) {
        return Conditions.and(this, other);
    }

    public final Condition or(Condition other) {
        return Conditions.or(this, other);
    }

    public final Condition not() {
        return Conditions.not(this);
    }

    /** 常真条件，用于条件可动态决定的场景（如整包开关） */
    public static Condition alwaysTrue() {
        return new RawCondition("1 = 1");
    }

    @Override
    public String toString() {
        return toDsl();
    }
}
