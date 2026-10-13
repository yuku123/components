package com.zifang.util.strategy.rule;

/**
 * 单参内置函数判定，目前只用于空值判定（ISNULL / IS_NOT_NULL）。
 */
public class FuncCondition extends Condition {

    private final String function;
    private final String field;

    public FuncCondition(String function, String field) {
        if (!"ISNULL".equals(function) && !"IS_NOT_NULL".equals(function)) {
            throw new IllegalArgumentException("unsupported condition function: " + function);
        }
        this.function = function;
        this.field = Conditions.field(field);
    }

    @Override
    public String toDsl() {
        return function + "(" + field + ")";
    }
}
