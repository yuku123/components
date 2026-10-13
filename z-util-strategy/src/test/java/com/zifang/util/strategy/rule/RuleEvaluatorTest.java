package com.zifang.util.strategy.rule;

import com.zifang.util.strategy.Decision;
import com.zifang.util.strategy.DecisionOutcome;
import com.zifang.util.strategy.StrategyContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link RuleEvaluator} 的求值细节：布尔化规则、只加分不出结论的 score 收口、trace 完整性。
 */
class RuleEvaluatorTest {

    private static StrategyContext facts(Object... kv) {
        StrategyContext ctx = new StrategyContext();
        for (int i = 0; i < kv.length; i += 2) {
            ctx.fact(String.valueOf(kv[i]), kv[i + 1]);
        }
        return ctx;
    }

    private static Decision eval(Rule rule, MatchSpec spec, StrategyContext ctx) {
        return RuleEvaluator.evaluate(Collections.singletonList(rule), ctx, spec);
    }

    // ---------------- 布尔化 ----------------

    @Test
    void 求值结果的布尔化规则() {
        assertFalse(RuleEvaluator.toBool(null));
        assertTrue(RuleEvaluator.toBool(Boolean.TRUE));
        assertFalse(RuleEvaluator.toBool(Boolean.FALSE));
        assertTrue(RuleEvaluator.toBool(1));
        assertTrue(RuleEvaluator.toBool(0.5d));
        assertFalse(RuleEvaluator.toBool(0));
        assertFalse(RuleEvaluator.toBool(0L));
        assertFalse(RuleEvaluator.toBool(0.0d));
        assertTrue(RuleEvaluator.toBool(Arrays.asList(1)));
        assertFalse(RuleEvaluator.toBool(Collections.emptyList()));
        assertTrue(RuleEvaluator.toBool("TRUE"));
        assertTrue(RuleEvaluator.toBool("true"));
        assertTrue(RuleEvaluator.toBool(" 1 "));
        assertFalse(RuleEvaluator.toBool("yes"));
        assertFalse(RuleEvaluator.toBool(""));
    }

    @Test
    void 空规则集直接放行且不留轨迹() {
        Decision d = RuleEvaluator.evaluate(Collections.<Rule>emptyList(), new StrategyContext(), MatchSpec.collectAll());
        assertTrue(d.isPass());
        assertTrue(d.trace().isEmpty());
    }

    // ---------------- 分数收口 ----------------

    @Test
    void 只加分的规则收口成score结论() {
        Rule r = Rule.builder("R1").condition(Condition.alwaysTrue()).effect(Effect.score(30)).build();
        Decision d = eval(r, MatchSpec.collectAll(), facts());
        assertEquals(DecisionOutcome.SCORE, d.outcome());
        assertEquals(30d, d.score(), 1e-9);
    }

    @Test
    void 分数为零不出score结论() {
        Rule r = Rule.builder("R1").condition(Condition.alwaysTrue()).effect(Effect.score(0)).build();
        assertEquals(DecisionOutcome.PASS, eval(r, MatchSpec.collectAll(), facts()).outcome());
    }

    @Test
    void 负分不出score结论也不影响结论() {
        Rule r = Rule.builder("R1").condition(Condition.alwaysTrue()).effect(Effect.score(-30)).build();
        Decision d = eval(r, MatchSpec.collectAll(), facts());
        assertEquals(DecisionOutcome.PASS, d.outcome());
        assertEquals(-30d, d.score(), 1e-9);
    }

    @Test
    void 有明确结论时分数不覆盖结论() {
        Rule r = Rule.builder("R1").condition(Condition.alwaysTrue())
                .effect(Effect.of(DecisionOutcome.REVIEW, "C", 5, "t")).build();
        Decision d = eval(r, MatchSpec.collectAll(), facts());
        assertEquals(DecisionOutcome.REVIEW, d.outcome());
        assertEquals("C", d.code());
        assertEquals(1, d.tags().size());
        assertEquals("t", d.hits().get(0).tag());
    }

    // ---------------- trace ----------------

    @Test
    void 每条启用规则都留一步轨迹() {
        Rule hit = Rule.builder("HIT").priority(1).condition(Condition.alwaysTrue())
                .effect(Effect.of(DecisionOutcome.PASS, "ok")).build();
        Rule miss = Rule.builder("MISS").priority(2).condition(Conditions.eq("a", 1)).build();
        Decision d = RuleEvaluator.evaluate(Arrays.asList(hit, miss), facts("a", 2), MatchSpec.collectAll());
        assertEquals(2, d.trace().size());
        assertEquals("HIT", d.trace().steps().get(0).ruleId());
        assertTrue(d.trace().steps().get(0).matched());
        assertEquals("MISS", d.trace().steps().get(1).ruleId());
        assertFalse(d.trace().steps().get(1).matched());
        assertTrue(d.trace().totalNanos() >= 0);
    }

    @Test
    void 禁用规则不占轨迹() {
        Rule off = Rule.builder("OFF").enabled(false).condition(Condition.alwaysTrue())
                .effect(Effect.of(DecisionOutcome.REJECT)).build();
        Decision d = RuleEvaluator.evaluate(Collections.singletonList(off), facts(), MatchSpec.collectAll());
        assertTrue(d.trace().isEmpty());
        assertEquals(DecisionOutcome.PASS, d.outcome());
    }

    // ---------------- 终止模式 ----------------

    @Test
    void first_match只跑第一条命中的() {
        Rule a = Rule.builder("A").priority(1).condition(Conditions.gt("score", 10))
                .effect(Effect.score(10)).build();
        Rule b = Rule.builder("B").priority(2).condition(Conditions.gt("score", 5))
                .effect(Effect.score(20)).build();
        Decision d = RuleEvaluator.evaluate(Arrays.asList(a, b), facts("score", 100), MatchSpec.firstMatch());
        assertEquals(1, d.hits().size());
        assertEquals("A", d.hits().get(0).ruleId());
        assertEquals(10d, d.score(), 1e-9);
    }

    @Test
    void threshold达到即停未达到跑完() {
        Rule a = Rule.builder("A").priority(1).condition(Condition.alwaysTrue()).effect(Effect.score(40)).build();
        Rule b = Rule.builder("B").priority(2).condition(Condition.alwaysTrue()).effect(Effect.score(40)).build();
        // 第一条就攒到 40 分：阈值 40 停在这儿，阈值 80 还得再跑一条
        assertEquals(1, RuleEvaluator.evaluate(Arrays.asList(a, b), facts(), MatchSpec.threshold(40)).hits().size());
        assertEquals(2, RuleEvaluator.evaluate(Arrays.asList(a, b), facts(), MatchSpec.threshold(80)).hits().size());
    }

    @Test
    void threshold边界取大于等于() {
        Rule a = Rule.builder("A").condition(Condition.alwaysTrue()).effect(Effect.score(50)).build();
        assertEquals(1, RuleEvaluator.evaluate(Collections.singletonList(a), facts(), MatchSpec.threshold(50)).hits().size());
    }

    @Test
    void threshold与first_match都不影响空转() {
        Rule miss = Rule.builder("M").condition(Conditions.eq("a", 1)).effect(Effect.of(DecisionOutcome.REJECT)).build();
        for (MatchSpec spec : new MatchSpec[]{MatchSpec.firstMatch(), MatchSpec.collectAll(), MatchSpec.threshold(1)}) {
            Decision d = RuleEvaluator.evaluate(Collections.singletonList(miss), facts("a", 2), spec);
            assertEquals(DecisionOutcome.PASS, d.outcome(), spec.toString());
            assertEquals(1, d.trace().size(), spec.toString());
        }
    }

    // ---------------- 事实集 ----------------

    @Test
    void 条件可以直接引用嵌套路径字段() {
        Map<String, Object> nested = new LinkedHashMap<>();
        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("vip", 3);
        nested.put("profile", profile);
        Rule r = Rule.builder("R").condition(Conditions.eq("profile.vip", 3))
                .effect(Effect.of(DecisionOutcome.REVIEW)).build();
        Decision d = RuleEvaluator.evaluate(Collections.singletonList(r), StrategyContext.of(nested), MatchSpec.collectAll());
        assertEquals(DecisionOutcome.REVIEW, d.outcome());
    }
}