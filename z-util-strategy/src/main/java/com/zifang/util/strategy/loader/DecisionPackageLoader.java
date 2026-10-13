package com.zifang.util.strategy.loader;

import com.zifang.util.json.JsonUtil;
import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import com.zifang.util.strategy.DecisionOutcome;
import com.zifang.util.strategy.DecisionPackage;
import com.zifang.util.strategy.RunMode;
import com.zifang.util.strategy.rule.Condition;
import com.zifang.util.strategy.rule.Conditions;
import com.zifang.util.strategy.rule.Effect;
import com.zifang.util.strategy.rule.MatchMode;
import com.zifang.util.strategy.rule.MatchSpec;
import com.zifang.util.strategy.rule.Rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 决策包载入器。
 * <p>
 * 条件支持两种写法，可混用：
 * <ul>
 *     <li>DSL 文本：{@code "condition": "amount > 10000 AND country <> 'CN'"}</li>
 *     <li>结构化（阿里云那套左变量 / 操作符 / 右变量）：
 *         {@code "condition": {"and": [{"field": "amount", "op": ">", "value": 10000}]}}</li>
 * </ul>
 * <pre>
 * name: 大额异常交易
 * version: "3"
 * runMode: TRIAL
 * match:
 *   mode: COLLECT_ALL
 *   onError: FAIL_CLOSED
 * rules:
 *   - id: R001
 *     priority: 10
 *     condition: "amount &gt; 10000 AND country &lt;&gt; 'CN'"
 *     effect: { outcome: REVIEW, code: BIG_AMOUNT, score: 60, tag: big_amount }
 * </pre>
 *
 * YAML 走 {@code DecisionPackageLoader.load(YamlUtil.parse(text))} ——
 * YAML 是 JSON 超集，落到同一个 Map 入口。本模块刻意不依赖 {@code z-util-parser-yaml}：
 * SnakeYAML 有 CVE 前科，不该由一个决策内核拖给所有消费方。
 */
public final class DecisionPackageLoader {

    private DecisionPackageLoader() {
    }

    public static DecisionPackage fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("package json must not be blank");
        }
        Map<String, Object> spec;
        try {
            // 走 fromJson 而不是 parseToMap：后者对数组值给的是自家 JsonArray 而非 List，
            // 嵌套 rules 判不出来，会静默装出一个空包。
            spec = JsonUtil.fromJson(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (ClassCastException e) {
            // 顶层不是对象时，泛型返回值的隐式 checkcast 会先炸 ClassCastException
            throw new IllegalArgumentException("package json is not a JSON object");
        }
        if (spec == null || spec.isEmpty()) {
            throw new IllegalArgumentException("package json is not a valid object");
        }
        return load(spec);
    }

    public static DecisionPackage load(Map<String, Object> rawSpec) {
        if (rawSpec == null) {
            throw new IllegalArgumentException("package spec must not be null");
        }
        Map<String, Object> spec = castMap(normalize(rawSpec));
        String name = str(spec, "name");
        String version = str(spec, "version");
        DecisionPackage.Builder builder = DecisionPackage.builder(name, version)
                .runMode(runMode(opt(spec, "runMode")))
                .matchSpec(matchSpec(asMap(spec.get("match"))));

        Object rules = spec.get("rules");
        if (rules instanceof List) {
            for (Object item : (List<?>) rules) {
                builder.rule(rule(asMap(item)));
            }
        }
        return builder.build();
    }

    private static Rule rule(Map<String, Object> spec) {
        Object effectSpec = spec.get("effect");
        Effect effect = effectSpec == null ? Effect.none() : effect(asMap(effectSpec));
        return Rule.builder(str(spec, "id"))
                .name(opt(spec, "name"))
                .priority(intOf(spec.get("priority"), 100))
                .enabled(boolOf(spec.get("enabled"), true))
                .condition(condition(spec.get("condition")))
                .effect(effect)
                .build();
    }

    private static MatchSpec matchSpec(Map<String, Object> spec) {
        if (spec == null) {
            return MatchSpec.collectAll();
        }
        MatchMode mode = MatchMode.valueOf(str(spec, "mode").toUpperCase());
        MatchSpec.OnError onError = MatchSpec.OnError.valueOf(str(spec, "onError").toUpperCase());
        return MatchSpec.of(mode, doubleOf(spec.get("threshold"), 0), onError);
    }

    private static Effect effect(Map<String, Object> spec) {
        String outcome = opt(spec, "outcome");
        return Effect.of(
                outcome == null ? null : DecisionOutcome.valueOf(outcome.toUpperCase()),
                opt(spec, "code"),
                doubleOf(spec.get("score"), 0),
                opt(spec, "tag"));
    }

    // ---------------- 条件 ----------------

    private static Condition condition(Object spec) {
        if (spec == null) {
            return Condition.alwaysTrue();
        }
        if (spec instanceof String) {
            return Conditions.dsl((String) spec);
        }
        Map<String, Object> node = asMap(spec);
        if (node == null) {
            throw new IllegalArgumentException("condition must be a DSL string or an object");
        }
        Condition and = compound(node, "and");
        if (and != null) {
            return and;
        }
        Condition or = compound(node, "or");
        if (or != null) {
            return or;
        }
        if (node.containsKey("not")) {
            return Conditions.not(condition(node.get("not")));
        }
        return comparison(node);
    }

    private static Condition compound(Map<String, Object> node, String key) {
        Object operands = node.get(key);
        if (!(operands instanceof List)) {
            return null;
        }
        List<Condition> conditions = new ArrayList<>();
        for (Object operand : (List<?>) operands) {
            conditions.add(condition(operand));
        }
        return "and".equals(key) ? Conditions.and(conditions) : Conditions.or(conditions);
    }

    private static Condition comparison(Map<String, Object> node) {
        String field = str(node, "field");
        String op = str(node, "op").toUpperCase();
        Object value = node.get("value");

        if ("IN".equals(op)) {
            if (!(value instanceof List)) {
                throw new IllegalArgumentException("op IN requires a list value on field: " + field);
            }
            return Conditions.in(field, (List<?>) value);
        }
        if ("BETWEEN".equals(op)) {
            if (!(value instanceof List) || ((List<?>) value).size() != 2) {
                throw new IllegalArgumentException("op BETWEEN requires a 2-element list value on field: " + field);
            }
            List<?> range = (List<?>) value;
            return Conditions.between(field, range.get(0), range.get(1));
        }
        if ("IS_NULL".equals(op)) {
            return Conditions.isNull(field);
        }
        if ("IS_NOT_NULL".equals(op) || "EXISTS".equals(op)) {
            return Conditions.isNotNull(field);
        }
        if ("ABSENT".equals(op)) {
            return Conditions.isNull(field);
        }
        if ("NOT_IN".equals(op)) {
            if (!(value instanceof List)) {
                throw new IllegalArgumentException("op NOT_IN requires a list value on field: " + field);
            }
            return Conditions.not(Conditions.in(field, (List<?>) value));
        }
        return Conditions.compare(field, op, value);
    }

    // ---------------- 取值 ----------------

    /**
     * 把解析器的节点类型归一化成 JDK 的 Map / List。
     * <p>
     * {@code z-util-parser-json} 对数组给的是自家 {@link JsonArray}（只实现 {@code Iterable}）、
     * 对对象给的是 {@link JsonObject}，两者都不是 JDK 集合。不归一化的话
     * {@code instanceof List} 一律判不中，rules 会静默变成空包 —— 规则一条都没进，
     * 决策全是 PASS，而且不报错。
     */
    @SuppressWarnings("unchecked")
    private static Object normalize(Object value) {
        if (value instanceof JsonArray) {
            JsonArray array = (JsonArray) value;
            List<Object> items = new ArrayList<>(array.size());
            for (int i = 0; i < array.size(); i++) {
                items.add(normalize(array.get(i)));
            }
            return items;
        }
        if (value instanceof JsonObject) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<String, Object> e : ((JsonObject) value).getAllKeyValue()) {
                map.put(e.getKey(), normalize(e.getValue()));
            }
            return map;
        }
        if (value instanceof Map) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<?, ?> e : ((Map<?, ?>) value).entrySet()) {
                map.put(String.valueOf(e.getKey()), normalize(e.getValue()));
            }
            return map;
        }
        if (value instanceof Iterable) {
            List<Object> items = new ArrayList<>();
            for (Object item : (Iterable<?>) value) {
                items.add(normalize(item));
            }
            return items;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Object v) {
        if (!(v instanceof Map)) {
            throw new IllegalArgumentException("expected a JSON/YAML object but got: " + v);
        }
        return (Map<String, Object>) v;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object v) {
        return v instanceof Map ? (Map<String, Object>) v : null;
    }

    private static String str(Map<String, Object> spec, String key) {
        Object v = spec.get(key);
        if (v == null) {
            throw new IllegalArgumentException("missing required field: " + key);
        }
        return String.valueOf(v);
    }

    /** 可选字段，缺失或为 null 时返回 null */
    private static String opt(Map<String, Object> spec, String key) {
        Object v = spec.get(key);
        return v == null ? null : String.valueOf(v);
    }

    private static int intOf(Object v, int defaultValue) {
        return v == null ? defaultValue : (int) doubleOf(v, defaultValue);
    }

    private static double doubleOf(Object v, double defaultValue) {
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        }
        return v == null ? defaultValue : Double.parseDouble(String.valueOf(v));
    }

    private static boolean boolOf(Object v, boolean defaultValue) {
        return v == null ? defaultValue : Boolean.parseBoolean(String.valueOf(v));
    }

    private static RunMode runMode(String value) {
        return value == null ? RunMode.DRAFT : RunMode.valueOf(value.toUpperCase());
    }
}
