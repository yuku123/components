package com.zifang.util.strategy.loader;

import com.zifang.util.strategy.DecisionOutcome;
import com.zifang.util.strategy.DecisionPackage;
import com.zifang.util.strategy.RunMode;
import com.zifang.util.strategy.StrategyContext;
import com.zifang.util.strategy.rule.MatchMode;
import com.zifang.util.strategy.rule.MatchSpec;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JSON/YAML 载入面的操作符覆盖与报错边界。
 * <p>
 * {@code DecisionPackageLoader.load(Map)} 是 YAML 的入口：YAML 解析出来同样是
 * {@code Map<String, Object>}，所以这批用例同时也是 YAML 的用例。
 */
class DecisionPackageLoaderOpsTest {

    private static DecisionOutcome outcomeOf(String json, Object... kv) {
        Map<String, Object> facts = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            facts.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return DecisionPackageLoader.fromJson(json).evaluate(StrategyContext.of(facts)).outcome();
    }

    private static String pkg(String condition, String opBlock) {
        return "{\"name\":\"p\",\"version\":\"1\",\"runMode\":\"ACTIVE\",\"rules\":[{"
                + "\"id\":\"R1\",\"condition\":" + condition
                + ",\"effect\":{\"outcome\":\"REVIEW\"}" + (opBlock == null ? "" : "," + opBlock) + "}]}";
    }

    // ---------------- 空值类操作符 ----------------

    @Test
    void 结构化条件支持四个空值操作符() {
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"field\":\"nope\",\"op\":\"IS_NULL\",\"value\":null}", null), "a", 1));
        assertEquals(DecisionOutcome.PASS,
                outcomeOf(pkg("{\"field\":\"a\",\"op\":\"IS_NULL\",\"value\":null}", null), "a", 1));
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"field\":\"a\",\"op\":\"IS_NOT_NULL\",\"value\":null}", null), "a", 1));
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"field\":\"a\",\"op\":\"EXISTS\",\"value\":null}", null), "a", 1));
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"field\":\"nope\",\"op\":\"ABSENT\",\"value\":null}", null), "a", 1));
    }

    @Test
    void in与not_in在结构化条件里可用() {
        String in = "{\"field\":\"country\",\"op\":\"IN\",\"value\":[\"CN\",\"US\"]}";
        assertEquals(DecisionOutcome.REVIEW, outcomeOf(pkg(in, null), "country", "US"));
        assertEquals(DecisionOutcome.PASS, outcomeOf(pkg(in, null), "country", "DE"));

        String notIn = "{\"field\":\"country\",\"op\":\"NOT_IN\",\"value\":[\"CN\",\"US\"]}";
        assertEquals(DecisionOutcome.PASS, outcomeOf(pkg(notIn, null), "country", "US"));
        assertEquals(DecisionOutcome.REVIEW, outcomeOf(pkg(notIn, null), "country", "DE"));
    }

    @Test
    void in类操作符的value不是列表时直接报错() {
        assertThrows(IllegalArgumentException.class,
                () -> DecisionPackageLoader.fromJson(pkg("{\"field\":\"country\",\"op\":\"IN\",\"value\":\"CN\"}", null)));
        assertThrows(IllegalArgumentException.class,
                () -> DecisionPackageLoader.fromJson(pkg("{\"field\":\"country\",\"op\":\"NOT_IN\",\"value\":null}", null)));
    }

    @Test
    void between要求两元素列表() {
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"field\":\"amount\",\"op\":\"BETWEEN\",\"value\":[100,200]}", null), "amount", 150));
        assertEquals(DecisionOutcome.PASS,
                outcomeOf(pkg("{\"field\":\"amount\",\"op\":\"BETWEEN\",\"value\":[100,200]}", null), "amount", 300));
        assertThrows(IllegalArgumentException.class,
                () -> DecisionPackageLoader.fromJson(pkg("{\"field\":\"amount\",\"op\":\"BETWEEN\",\"value\":[100]}", null)));
        assertThrows(IllegalArgumentException.class,
                () -> DecisionPackageLoader.fromJson(pkg("{\"field\":\"amount\",\"op\":\"BETWEEN\",\"value\":100}", null)));
    }

    // ---------------- 逻辑组合 ----------------

    @Test
    void or节点取任一成立() {
        String or = "{\"or\":[{\"field\":\"a\",\"op\":\"=\",\"value\":1},{\"field\":\"b\",\"op\":\"=\",\"value\":2}]}";
        assertEquals(DecisionOutcome.REVIEW, outcomeOf(pkg(or, null), "a", 0, "b", 2));
        assertEquals(DecisionOutcome.PASS, outcomeOf(pkg(or, null), "a", 0, "b", 0));
    }

    @Test
    void not节点取反() {
        assertEquals(DecisionOutcome.PASS,
                outcomeOf(pkg("{\"not\":{\"field\":\"a\",\"op\":\"=\",\"value\":1}}", null), "a", 1));
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"not\":{\"field\":\"a\",\"op\":\"=\",\"value\":1}}", null), "a", 2));
    }

    @Test
    void 嵌套的and或or可以逐层展开() {
        String nested = "{\"and\":[{\"or\":[{\"field\":\"a\",\"op\":\"=\",\"value\":1},"
                + "{\"field\":\"a\",\"op\":\"=\",\"value\":2}]},{\"field\":\"b\",\"op\":\"=\",\"value\":9}]}";
        assertEquals(DecisionOutcome.REVIEW, outcomeOf(pkg(nested, null), "a", 2, "b", 9));
        assertEquals(DecisionOutcome.PASS, outcomeOf(pkg(nested, null), "a", 2, "b", 8));
        assertEquals(DecisionOutcome.PASS, outcomeOf(pkg(nested, null), "a", 3, "b", 9));
    }

    @Test
    void 操作符大小写不敏感() {
        assertEquals(DecisionOutcome.REVIEW,
                outcomeOf(pkg("{\"field\":\"country\",\"op\":\"in\",\"value\":[\"CN\"]}", null), "country", "CN"));
    }

    @Test
    void 条件既可以是字符串也可以是对象() {
        assertEquals(DecisionOutcome.REVIEW, outcomeOf(pkg("\"a = 1\"", null), "a", 1));
        assertEquals(DecisionOutcome.REVIEW, outcomeOf(pkg("{\"field\":\"a\",\"op\":\"=\",\"value\":1}", null), "a", 1));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson(pkg("123", null)));
    }

    // ---------------- 编排与规则字段 ----------------

    @Test
    void match块可配模式阈值与onError() {
        String json = "{\"name\":\"p\",\"version\":\"1\",\"runMode\":\"ACTIVE\","
                + "\"match\":{\"mode\":\"THRESHOLD\",\"threshold\":50,\"onError\":\"FAIL_OPEN\"},"
                + "\"rules\":[]}";
        DecisionPackage p = DecisionPackageLoader.fromJson(json);
        assertEquals(MatchMode.THRESHOLD, p.matchSpec().mode());
        assertEquals(50d, p.matchSpec().threshold(), 1e-9);
        assertEquals(MatchSpec.OnError.FAIL_OPEN, p.matchSpec().onError());
    }

    @Test
    void 缺match块时落回collect_all与fail_closed() {
        DecisionPackage p = DecisionPackageLoader.fromJson(
                "{\"name\":\"p\",\"version\":\"1\",\"rules\":[]}");
        assertEquals(MatchMode.COLLECT_ALL, p.matchSpec().mode());
        assertEquals(MatchSpec.OnError.FAIL_CLOSED, p.matchSpec().onError());
        assertEquals(RunMode.DRAFT, p.runMode());
    }

    @Test
    void match块字段缺失或非法时报错() {
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson(
                "{\"name\":\"p\",\"version\":\"1\",\"match\":{\"onError\":\"FAIL_CLOSED\"},\"rules\":[]}"));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson(
                "{\"name\":\"p\",\"version\":\"1\",\"match\":{\"mode\":\"COLLECT_ALL\"},\"rules\":[]}"));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson(
                "{\"name\":\"p\",\"version\":\"1\",\"match\":{\"mode\":\"NOPE\",\"onError\":\"FAIL_CLOSED\"},\"rules\":[]}"));
    }

    @Test
    void 规则的可选字段缺省时走默认值() {
        DecisionPackage p = DecisionPackageLoader.fromJson("{\"name\":\"p\",\"version\":\"1\",\"runMode\":\"ACTIVE\","
                + "\"rules\":[{\"id\":\"R1\",\"condition\":\"1 = 1\"}]}");
        assertEquals(1, p.rules().size());
        assertEquals(100, p.rules().get(0).priority());
        assertTrue(p.rules().get(0).enabled());
        assertEquals(0d, p.rules().get(0).effect().scoreDelta(), 1e-9);
    }

    @Test
    void enabled为false的规则不会进包求值() {
        String json = "{\"name\":\"p\",\"version\":\"1\",\"runMode\":\"ACTIVE\",\"rules\":["
                + "{\"id\":\"R1\",\"enabled\":false,\"condition\":\"1 = 1\",\"effect\":{\"outcome\":\"REJECT\"}}]}";
        assertEquals(DecisionOutcome.PASS, outcomeOf(json));
        assertFalse(DecisionPackageLoader.fromJson(json).rules().get(0).enabled());
    }

    @Test
    void 规则的优先级决定求值顺序() {
        String json = "{\"name\":\"p\",\"version\":\"1\",\"runMode\":\"ACTIVE\",\"rules\":["
                + "{\"id\":\"LATE\",\"priority\":900,\"condition\":\"1 = 1\",\"effect\":{\"outcome\":\"REJECT\"}},"
                + "{\"id\":\"EARLY\",\"priority\":1,\"condition\":\"1 = 1\",\"effect\":{\"outcome\":\"REVIEW\"}}]}";
        DecisionPackage p = DecisionPackageLoader.fromJson(json);
        assertEquals("EARLY", p.rules().get(0).id());
        assertEquals("LATE", p.rules().get(1).id());
    }

    @Test
    void 效果里的分数与标签会被载入() {
        String json = "{\"name\":\"p\",\"version\":\"1\",\"runMode\":\"ACTIVE\",\"rules\":[{"
                + "\"id\":\"R1\",\"condition\":\"1 = 1\","
                + "\"effect\":{\"outcome\":\"SCORE\",\"code\":\"BIG\",\"score\":60,\"tag\":\"big\"}}]}";
        assertEquals(DecisionOutcome.SCORE, outcomeOf(json));
    }

    @Test
    void 没写rules时装出空包而不是报错() {
        DecisionPackage p = DecisionPackageLoader.fromJson("{\"name\":\"p\",\"version\":\"1\"}");
        assertTrue(p.rules().isEmpty());
        assertEquals(DecisionOutcome.PASS,
                p.evaluate(StrategyContext.of(new HashMap<>())).outcome());
        DecisionPackage empty = DecisionPackageLoader.fromJson(
                "{\"name\":\"p\",\"version\":\"1\",\"rules\":[]}");
        assertNotNull(empty);
        assertTrue(empty.rules().isEmpty());
    }

    // ---------------- 入参边界 ----------------

    @Test
    void 空白与非对象的输入直接报错() {
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson(null));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson("   "));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson("[1,2]"));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson("{}"));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.load(null));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackageLoader.fromJson("123"));
    }

    @Test
    void map入口与json入口产出同一种包() {
        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("name", "p");
        spec.put("version", "1");
        spec.put("runMode", "ACTIVE");
        List<Object> rules = new java.util.ArrayList<>();
        Map<String, Object> rule = new LinkedHashMap<>();
        rule.put("id", "R1");
        rule.put("condition", "a = 1");
        rule.put("effect", new LinkedHashMap<>(java.util.Collections.singletonMap("outcome", "REVIEW")));
        rules.add(rule);
        spec.put("rules", rules);

        DecisionPackage fromMap = DecisionPackageLoader.load(spec);
        assertEquals(1, fromMap.rules().size());
        assertEquals(DecisionOutcome.REVIEW,
                fromMap.evaluate(StrategyContext.of(facts("a", 1))).outcome());
        assertEquals(DecisionOutcome.PASS,
                fromMap.evaluate(StrategyContext.of(facts("a", 2))).outcome());
    }

    @Test
    void 缺必填字段时报错并指出字段名() {
        try {
            DecisionPackageLoader.fromJson("{\"version\":\"1\",\"rules\":[]}");
            throw new AssertionError("应当报错");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name"), e.getMessage());
        }
        try {
            DecisionPackageLoader.fromJson("{\"name\":\"p\",\"version\":\"1\",\"rules\":[{\"condition\":\"1 = 1\"}]}");
            throw new AssertionError("应当报错");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("id"), e.getMessage());
        }
    }

    private static Map<String, Object> facts(Object... kv) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            map.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return map;
    }
}