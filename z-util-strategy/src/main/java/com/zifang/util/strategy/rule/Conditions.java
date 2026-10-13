package com.zifang.util.strategy.rule;

import java.util.Arrays;
import java.util.List;

/**
 * 条件工厂。
 * <p>
 * 所有工厂方法都是不可变对象树的构造，节点在 {@link #toDsl()} 时做字段名校验与字面量转义。
 */
public final class Conditions {

    private Conditions() {
    }

    // ---------------- 比较 ----------------

    public static Condition eq(String field, Object value) {
        return compare(field, "=", value);
    }

    public static Condition ne(String field, Object value) {
        return compare(field, "<>", value);
    }

    public static Condition gt(String field, Object value) {
        return compare(field, ">", value);
    }

    public static Condition gte(String field, Object value) {
        return compare(field, ">=", value);
    }

    public static Condition lt(String field, Object value) {
        return compare(field, "<", value);
    }

    public static Condition lte(String field, Object value) {
        return compare(field, "<=", value);
    }

    /** 通配匹配，{@code %} 匹配任意长度，{@code _} 匹配单字符 */
    public static Condition like(String field, String pattern) {
        return compare(field, "LIKE", pattern);
    }

    /** 正则整体匹配（等价 {@code Pattern.matches}） */
    public static Condition matches(String field, String regex) {
        return compare(field, "REGEXP", regex);
    }

    public static Condition compare(String field, String operator, Object value) {
        return new CompareCondition(field, operator, value);
    }

    /** 命中任一值即可；DSL 无原生 IN，编译成 OR 等值链 */
    public static Condition in(String field, Object... values) {
        return in(field, values == null ? null : Arrays.asList(values));
    }

    public static Condition in(String field, List<?> values) {
        if (values == null || values.isEmpty()) {
            return Condition.alwaysTrue();
        }
        Condition result = null;
        for (Object v : values) {
            Condition c = eq(field, v);
            result = result == null ? c : or(result, c);
        }
        return result;
    }

    /** 闭区间 [low, high] */
    public static Condition between(String field, Object low, Object high) {
        return and(gte(field, low), lte(field, high));
    }

    public static Condition isNull(String field) {
        return new FuncCondition("ISNULL", field);
    }

    public static Condition isNotNull(String field) {
        return new FuncCondition("IS_NOT_NULL", field);
    }

    public static Condition exists(String field) {
        return isNotNull(field);
    }

    public static Condition absent(String field) {
        return isNull(field);
    }

    // ---------------- 逻辑 ----------------

    public static Condition and(Condition... conditions) {
        return new LogicalCondition("AND", conditions);
    }

    public static Condition and(List<Condition> conditions) {
        return new LogicalCondition("AND", conditions);
    }

    public static Condition or(Condition... conditions) {
        return new LogicalCondition("OR", conditions);
    }

    public static Condition or(List<Condition> conditions) {
        return new LogicalCondition("OR", conditions);
    }

    public static Condition not(Condition condition) {
        return new LogicalCondition("NOT", condition);
    }

    public static Condition dsl(String dsl) {
        return new RawCondition(dsl);
    }

    // ---------------- 字面量转义 ----------------

    static String literal(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "TRUE" : "FALSE";
        }
        if (value instanceof Number) {
            return String.valueOf(value);
        }
        return quote(value.toString());
    }

    static String quote(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 2);
        sb.append('\'');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\'' || c == '\\') {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.append('\'').toString();
    }

    /**
     * 字段名最终会拼进 DSL 文本交给词法器解析，所以只放行标识符与点号路径，
     * 挡掉引号、括号、空白这类会改变表达式结构的字符。
     */
    static String field(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("condition field must not be blank");
        }
        char first = name.charAt(0);
        if (!Character.isLetter(first) && first != '_') {
            throw new IllegalArgumentException("illegal condition field: " + name);
        }
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '.') {
                throw new IllegalArgumentException("illegal condition field: " + name);
            }
        }
        return name;
    }
}
