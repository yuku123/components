package com.zifang.util.strategy.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 逻辑组合：AND / OR / NOT。
 */
public class LogicalCondition extends Condition {

    private final String operator;
    private final List<Condition> conditions;

    public LogicalCondition(String operator, Condition... conditions) {
        this(operator, conditions == null ? null : Arrays.asList(conditions));
    }

    public LogicalCondition(String operator, List<Condition> conditions) {
        if (!"AND".equals(operator) && !"OR".equals(operator) && !"NOT".equals(operator)) {
            throw new IllegalArgumentException("unsupported logical operator: " + operator);
        }
        List<Condition> copy = new ArrayList<>();
        if (conditions != null) {
            for (Condition c : conditions) {
                if (c != null) {
                    copy.add(c);
                }
            }
        }
        if (copy.isEmpty()) {
            throw new IllegalArgumentException("logical condition requires at least one operand");
        }
        if ("NOT".equals(operator) && copy.size() != 1) {
            throw new IllegalArgumentException("NOT takes exactly one operand");
        }
        this.operator = operator;
        this.conditions = Collections.unmodifiableList(copy);
    }

    public List<Condition> conditions() {
        return conditions;
    }

    @Override
    public String toDsl() {
        if ("NOT".equals(operator)) {
            return "(NOT " + conditions.get(0).toDsl() + ")";
        }
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < conditions.size(); i++) {
            if (i > 0) {
                sb.append(' ').append(operator).append(' ');
            }
            sb.append(conditions.get(i).toDsl());
        }
        return sb.append(')').toString();
    }
}
