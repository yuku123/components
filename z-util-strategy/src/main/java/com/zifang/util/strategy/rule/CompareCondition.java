package com.zifang.util.strategy.rule;

/**
 * 字段与字面量的比较。op 走白名单，避免任意串拼进 DSL 改变表达式结构。
 */
public class CompareCondition extends Condition {

    private static final String[] ALLOWED_OPS = {"=", "<>", "!=", "<", ">", "<=", ">=", "LIKE", "REGEXP"};

    private final String field;
    private final String operator;
    private final Object value;

    public CompareCondition(String field, String operator, Object value) {
        this.field = Conditions.field(field);
        this.operator = requireAllowed(operator);
        this.value = value;
    }

    static String requireAllowed(String operator) {
        if (operator == null) {
            throw new IllegalArgumentException("condition operator must not be null");
        }
        String normalized = operator.toUpperCase().equals("==") ? "=" : operator.toUpperCase();
        for (String allowed : ALLOWED_OPS) {
            if (allowed.equals(normalized)) {
                return normalized;
            }
        }
        throw new IllegalArgumentException("unsupported condition operator: " + operator);
    }

    public String field() {
        return field;
    }

    public String operator() {
        return operator;
    }

    public Object value() {
        return value;
    }

    @Override
    public String toDsl() {
        return "(" + field + " " + operator + " " + Conditions.literal(value) + ")";
    }
}
