package com.zifang.util.strategy.rule;

/**
 * 直接以 DSL 文本书写的条件。
 * <p>
 * 用于两种情况：JSON/YAML 载入时 {@code condition: "amount > 1000"} 的文本形态，
 * 以及对象树表达不了的表达式（临时收编一条存量规则）。文本形态不受
 * {@link Conditions} 的字段名校验保护，只应来自可信的规则定义源。
 */
public class RawCondition extends Condition {

    private final String dsl;

    public RawCondition(String dsl) {
        if (dsl == null || dsl.trim().isEmpty()) {
            throw new IllegalArgumentException("condition dsl must not be blank");
        }
        this.dsl = dsl.trim();
    }

    @Override
    public String toDsl() {
        return "(" + dsl + ")";
    }
}
