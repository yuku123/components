package com.zifang.util.strategy;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 单次决策的事实集。
 * <p>
 * 刻意不提供强类型 Fact：风控事实常是半结构化 JSON，强类型会逼调用方为每个场景写一份 DTO。
 * 缺失的键返回 {@code null} 而不是抛异常，由条件表达式自己决定怎么判。
 * <p>
 * 实例非线程安全，按单次决策的生命周期使用。
 */
public class StrategyContext {

    private final Map<String, Object> facts = new LinkedHashMap<>();

    public StrategyContext() {
    }

    public static StrategyContext of(Map<String, Object> facts) {
        StrategyContext ctx = new StrategyContext();
        if (facts != null) {
            ctx.facts.putAll(facts);
        }
        return ctx;
    }

    public StrategyContext fact(String key, Object value) {
        facts.put(key, value);
        return this;
    }

    public StrategyContext facts(Map<String, Object> values) {
        if (values != null) {
            facts.putAll(values);
        }
        return this;
    }

    public Object get(String key) {
        return facts.get(key);
    }

    public boolean has(String key) {
        return facts.containsKey(key);
    }

    public String getString(String key) {
        Object v = facts.get(key);
        return v == null ? null : String.valueOf(v);
    }

    public double getDouble(String key, double defaultValue) {
        return toDouble(facts.get(key), defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return (int) toDouble(facts.get(key), defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object v = facts.get(key);
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        return v == null ? defaultValue : Boolean.parseBoolean(String.valueOf(v));
    }

    /** 只读视图，按调用方给的原始键名 */
    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(facts);
    }

    /**
     * 求值用的行数据。
     * <p>
     * 键统一转成大写：DSL 词法器把标识符一律大写化，列引用求值时也先找原名再找小写名。
     * 不统一的话 {@code userId} 这类驼峰键会永远查不到（USERID / userid 都 ≠ userId）。
     */
    public Map<String, Object> asRow() {
        Map<String, Object> row = new LinkedHashMap<>();
        flatten("", facts, row);
        return row;
    }

    /**
     * 嵌套 Map 摊平成点号路径。
     * <p>
     * {@link com.zifang.util.strategy.rule.Conditions#field(String)} 放行了 {@code profile.vip}
     * 这种带点的字段名，条件里也常这么写。不摊平的话列引用 PROFILE.VIP 在行里根本不存在，
     * 条件恒为 false，规则静默失效 —— 比直接报错更难查。
     */
    private static void flatten(String prefix, Map<String, Object> source, Map<String, Object> target) {
        for (Map.Entry<String, Object> e : source.entrySet()) {
            String key = e.getKey();
            if (key == null) {
                continue;
            }
            String path = prefix.isEmpty() ? key : prefix + "." + key;
            Object value = e.getValue();
            if (value instanceof Map) {
                flatten(path, castMap(value), target);
            } else {
                target.put(path.toUpperCase(), value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Object value) {
        return (Map<String, Object>) value;
    }

    static double toDouble(Object v, double defaultValue) {
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        }
        if (v == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(String.valueOf(v));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
