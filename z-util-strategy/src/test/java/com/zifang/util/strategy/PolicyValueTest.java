package com.zifang.util.strategy;

import com.zifang.util.strategy.rule.Effect;
import com.zifang.util.strategy.rule.HitRecord;
import com.zifang.util.strategy.rule.MatchMode;
import com.zifang.util.strategy.rule.MatchSpec;
import com.zifang.util.strategy.rule.Rule;
import com.zifang.util.strategy.rule.RuleEvaluator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 值对象与分流策略：结论严重度、轨迹、指标、灰度/对照组比例、上下文取值、构建期校验。
 */
class PolicyValueTest {

    // ---------------- 结论严重度 ----------------

    @Test
    void 严重度顺序即取严顺序() {
        assertTrue(DecisionOutcome.REJECT.isWorseThan(DecisionOutcome.MANUAL));
        assertTrue(DecisionOutcome.MANUAL.isWorseThan(DecisionOutcome.REVIEW));
        assertTrue(DecisionOutcome.REVIEW.isWorseThan(DecisionOutcome.SCORE));
        assertTrue(DecisionOutcome.SCORE.isWorseThan(DecisionOutcome.PASS));
        assertTrue(DecisionOutcome.PASS.isWorseThan(null));
        assertFalse(DecisionOutcome.REJECT.isWorseThan(DecisionOutcome.REJECT));
    }

    @Test
    void worse取更严重的一侧且容忍null() {
        assertEquals(DecisionOutcome.REJECT, DecisionOutcome.worse(DecisionOutcome.REJECT, DecisionOutcome.PASS));
        assertEquals(DecisionOutcome.REJECT, DecisionOutcome.worse(DecisionOutcome.PASS, DecisionOutcome.REJECT));
        assertEquals(DecisionOutcome.REVIEW, DecisionOutcome.worse(null, DecisionOutcome.REVIEW));
        assertEquals(DecisionOutcome.REVIEW, DecisionOutcome.worse(DecisionOutcome.REVIEW, null));
        assertNull(DecisionOutcome.worse(null, null));
        assertEquals(5, DecisionOutcome.values().length);
    }

    @Test
    void 决策合并只升不降() {
        Decision d = new Decision();
        assertTrue(d.isPass());
        d.mergeOutcome(DecisionOutcome.REVIEW);
        assertEquals(DecisionOutcome.REVIEW, d.outcome());
        d.mergeOutcome(DecisionOutcome.PASS);
        assertEquals(DecisionOutcome.REVIEW, d.outcome());
        d.mergeOutcome(null);
        assertEquals(DecisionOutcome.REVIEW, d.outcome());
        d.mergeOutcome(DecisionOutcome.REJECT);
        assertEquals(DecisionOutcome.REJECT, d.outcome());
    }

    @Test
    void 决策对象的分值标签与代码() {
        Decision d = new Decision();
        d.addScore(30);
        d.addScore(12.5);
        d.addTag("a");
        d.addTag("a");
        d.addTag(null);
        d.code("BIG");
        assertEquals(42.5, d.score(), 1e-9);
        assertEquals(1, d.tags().size());
        assertEquals("BIG", d.code());
        assertThrows(UnsupportedOperationException.class, () -> d.tags().add("b"));
        assertThrows(UnsupportedOperationException.class, () -> d.hits().add(null));
        assertTrue(d.toString().contains("BIG"));
    }

    @Test
    void 决策的轨迹可被整体替换() {
        Decision d = new Decision();
        assertTrue(d.trace().isEmpty());
        d.trace(null);
        assertTrue(d.trace().isEmpty());
        DecisionTrace t = new DecisionTrace().add(TraceStep.of("rule", "R1", true, DecisionOutcome.REVIEW, "why", 7));
        d.trace(t);
        assertEquals(1, d.trace().size());
        assertSame(t, d.trace());
    }

    @Test
    void 效果对象的空判定() {
        assertTrue(Effect.none().isEmpty());
        assertTrue(Effect.of(null, null, 0, null).isEmpty());
        assertFalse(Effect.of(DecisionOutcome.PASS).isEmpty());
        assertFalse(Effect.score(1).isEmpty());
        assertFalse(Effect.tag("t").isEmpty());
        assertFalse(Effect.of(DecisionOutcome.PASS, "C").isEmpty());
        Effect all = Effect.of(DecisionOutcome.MANUAL, "C", 5, "t");
        assertEquals(DecisionOutcome.MANUAL, all.outcome());
        assertEquals("C", all.code());
        assertEquals(5d, all.scoreDelta(), 1e-9);
        assertEquals("t", all.tag());
        assertTrue(all.toString().contains("MANUAL"));
    }

    @Test
    void 命中记录把规则与效果对齐() {
        HitRecord h = new HitRecord("R1", "大额", Effect.of(DecisionOutcome.REJECT, "BIG", 60, "big"));
        assertEquals("R1", h.ruleId());
        assertEquals("大额", h.ruleName());
        assertEquals(DecisionOutcome.REJECT, h.outcome());
        assertEquals("BIG", h.code());
        assertEquals(60d, h.scoreDelta(), 1e-9);
        assertEquals("big", h.tag());
        assertTrue(h.toString().contains("大额"));
        assertTrue(new HitRecord("R2", null, Effect.none()).toString().contains("null"));
    }

    // ---------------- 轨迹 ----------------

    @Test
    void 轨迹可合并且步骤列表不可改() {
        DecisionTrace t = new DecisionTrace();
        assertTrue(t.isEmpty());
        t.add(TraceStep.of("rule", "R1", true, DecisionOutcome.REVIEW, "hit", 3))
                .add(TraceStep.of("rule", "R2", false, null, null, 4));
        assertEquals(2, t.size());
        assertEquals(7, t.totalNanos());
        assertThrows(UnsupportedOperationException.class, () -> t.steps().clear());
    }

    @Test
    void 轨迹合并会平铺另一个轨迹的步骤() {
        DecisionTrace a = new DecisionTrace().add(TraceStep.of("rule", "R1", true, null, null, 1));
        DecisionTrace b = new DecisionTrace().add(TraceStep.of("pkg", "P2", false, null, null, 2));
        a.merge(b);
        assertEquals(2, a.size());
        a.merge(null);
        a.merge(DecisionTrace.empty());
        assertEquals(2, a.size());
        assertTrue(DecisionTrace.empty().isEmpty());
    }

    @Test
    void 错误步骤没有结论只有错误信息() {
        TraceStep e = TraceStep.error("rule", "R1", "boom", 9);
        assertEquals("boom", e.error());
        assertNull(e.outcome());
        assertFalse(e.matched());
        assertEquals(9, e.durationNanos());
        assertTrue(e.toString().contains("ERROR: boom"));
    }

    @Test
    void 步骤的toString覆盖有无规则号与消息两种形态() {
        assertEquals("rule#R1 HIT -> REVIEW (why) 5ns",
                TraceStep.of("rule", "R1", true, DecisionOutcome.REVIEW, "why", 5).toString());
        assertEquals("rule#R1 miss 0ns",
                TraceStep.of("rule", "R1", false, null, null, 0).toString());
        assertEquals("pkg HIT 1ns",
                TraceStep.of("pkg", null, true, null, null, 1).toString());
    }

    // ---------------- 分流策略 ----------------

    @Test
    void 比例越界在构建期就被拒() {
        assertThrows(IllegalArgumentException.class, () -> TrafficPolicy.of(-1, "userId"));
        assertThrows(IllegalArgumentException.class, () -> TrafficPolicy.of(101, "userId"));
        assertThrows(IllegalArgumentException.class, () -> ControlPolicy.of(-1, "userId"));
        assertThrows(IllegalArgumentException.class, () -> ControlPolicy.of(101, "userId"));
    }

    @Test
    void 零比例与null标识都不命中() {
        TrafficPolicy t = TrafficPolicy.of(0, "userId");
        assertFalse(t.hit(12345));
        assertFalse(t.hit(null));
        ControlPolicy c = ControlPolicy.of(0, "userId");
        assertFalse(c.inControl(12345));
        assertFalse(c.inControl(null));
        assertFalse(TrafficPolicy.none().hit(12345));
        assertFalse(ControlPolicy.disabled().inControl(12345));
    }

    @Test
    void 全量比例对任意标识都命中() {
        assertTrue(TrafficPolicy.of(100, "userId").hit(null));
        assertTrue(TrafficPolicy.of(100, "userId").hit(999));
        assertTrue(ControlPolicy.of(100, "userId").inControl(999));
        // 对照组对 null 标识一律不纳入：拿不到稳定标识就没有可比的样本
        assertFalse(ControlPolicy.of(100, "userId").inControl(null));
    }

    @Test
    void 分桶落在零到一百之间且同一标识稳定() {
        TrafficPolicy t = TrafficPolicy.of(50, "userId");
        for (int i = 0; i < 500; i++) {
            String id = "user-" + i;
            int bucket = t.bucket(id);
            assertTrue(bucket >= 0 && bucket < 100, "bucket 越界: " + bucket);
            assertEquals(bucket, t.bucket(id));
        }
        assertEquals(0, t.bucket(null));
    }

    @Test
    void 策略对象保留入参便于排查() {
        assertEquals(30, TrafficPolicy.of(30, "userId").percent());
        assertEquals("userId", TrafficPolicy.of(30, "userId").hashKey());
        assertEquals(30, ControlPolicy.of(30, "userId").percent());
        assertEquals("userId", ControlPolicy.of(30, "userId").hashKey());
        assertTrue(TrafficPolicy.of(30, "userId").toString().contains("30%"));
        assertTrue(ControlPolicy.of(30, "userId").toString().contains("userId"));
    }

    @Test
    void 对照组只对拦截样本放行() {
        ControlPolicy c = ControlPolicy.of(100, "userId");
        assertTrue(c.inControl("u1"));
        // 引擎侧只在非 PASS 时才问对照组，这里验策略本身不认业务结论
        assertEquals(0, ControlPolicy.disabled().percent());
        assertNull(ControlPolicy.disabled().hashKey());
    }

    // ---------------- 指标 ----------------

    private static DecisionResult result(DecisionOutcome outcome, boolean gray, boolean control,
                                          Decision shadow, String ruleId) {
        Decision d = new Decision();
        d.mergeOutcome(outcome);
        if (ruleId != null) {
            d.addHit(new HitRecord(ruleId, null, Effect.none()));
        }
        return new DecisionResult(d, shadow, "p", "1", "1", gray, control);
    }

    @Test
    void 指标累计总量与分项() {
        DecisionMetrics.InMemory m = new DecisionMetrics.InMemory();
        m.record(result(DecisionOutcome.REJECT, true, false, null, "R1"));
        m.record(result(DecisionOutcome.REJECT, false, true, null, "R1"));
        m.record(result(DecisionOutcome.PASS, false, false, null, null));

        assertEquals(3, m.total());
        assertEquals(1, m.gray());
        assertEquals(1, m.control());
        assertEquals(0, m.trial());
        assertEquals(0, m.shadowDiffRate(), 1e-9);
        assertEquals(2, m.outcomeCount(DecisionOutcome.REJECT));
        assertEquals(1, m.outcomeCount(DecisionOutcome.PASS));
        assertEquals(0, m.outcomeCount(DecisionOutcome.MANUAL));
        assertEquals(2, m.ruleHitCount("R1"));
        assertEquals(0, m.ruleHitCount("R9"));
    }

    @Test
    void 试运行差异率是差异数除以试运行数() {
        DecisionMetrics.InMemory m = new DecisionMetrics.InMemory();
        m.record(result(DecisionOutcome.PASS, false, false,
                new Decision(), "R1")); // 同结论，不算差异
        Decision shadow = new Decision();
        shadow.mergeOutcome(DecisionOutcome.REJECT);
        m.record(result(DecisionOutcome.PASS, false, false, shadow, "R1"));

        assertEquals(2, m.trial());
        assertEquals(1, m.shadowDiff());
        assertEquals(0.5d, m.shadowDiffRate(), 1e-9);
    }

    @Test
    void 指标可以整体清零() {
        DecisionMetrics.InMemory m = new DecisionMetrics.InMemory();
        m.record(result(DecisionOutcome.REJECT, true, true, new Decision(), "R1"));
        m.reset();
        assertEquals(0, m.total());
        assertEquals(0, m.gray());
        assertEquals(0, m.control());
        assertEquals(0, m.trial());
        assertEquals(0, m.shadowDiff());
        assertEquals(0, m.outcomeCount(DecisionOutcome.REJECT));
        assertEquals(0, m.ruleHitCount("R1"));
    }

    // ---------------- 编排参数 ----------------

    @Test
    void 编排参数的三种默认形态() {
        assertEquals(MatchMode.FIRST_MATCH, MatchSpec.firstMatch().mode());
        assertEquals(MatchMode.COLLECT_ALL, MatchSpec.collectAll().mode());
        assertEquals(60d, MatchSpec.threshold(60).threshold(), 1e-9);
        assertEquals(MatchSpec.OnError.FAIL_CLOSED, MatchSpec.firstMatch().onError());
        assertEquals(MatchSpec.OnError.FAIL_OPEN, MatchSpec.firstMatch().onError(MatchSpec.OnError.FAIL_OPEN).onError());
        // 传 null 落回 fail-closed，不给隐式的"看情况"
        assertEquals(MatchSpec.OnError.FAIL_CLOSED, MatchSpec.firstMatch().onError(null).onError());
        assertTrue(MatchSpec.threshold(60).toString().contains("60"));
        assertFalse(MatchSpec.firstMatch().toString().contains("@"));
    }

    @Test
    void 编排参数不可变_改onError不污染共享的默认值() {
        MatchSpec base = MatchSpec.firstMatch();
        MatchSpec open = base.onError(MatchSpec.OnError.FAIL_OPEN);
        assertNotEquals(base, open);
        assertEquals(MatchSpec.OnError.FAIL_CLOSED, base.onError());
        assertSame(MatchSpec.firstMatch(), MatchSpec.firstMatch());
        assertSame(MatchSpec.collectAll(), MatchSpec.collectAll());
    }

    // ---------------- 上下文取值 ----------------

    @Test
    void 上下文按类型宽容取值() {
        StrategyContext ctx = new StrategyContext()
                .fact("s", "12.5").fact("n", 7).fact("b", true).fact("bStr", "true")
                .fact("bad", "abc").fact("nothing", null);
        assertEquals("12.5", ctx.getString("s"));
        assertNull(ctx.getString("nothing"));
        assertEquals(12.5d, ctx.getDouble("s", 0), 1e-9);
        assertEquals(7, ctx.getInt("n", 0));
        assertEquals(3.5d, ctx.getDouble("bad", 3.5), 1e-9);
        assertEquals(9, ctx.getInt("nothing", 9));
        assertTrue(ctx.getBoolean("b", false));
        assertTrue(ctx.getBoolean("bStr", false));
        assertTrue(ctx.getBoolean("nothing", true));
        assertFalse(ctx.getBoolean("bad", false));
    }

    @Test
    void 上下文能区分缺键与值为null() {
        StrategyContext ctx = new StrategyContext().fact("n", null);
        assertTrue(ctx.has("n"));
        assertFalse(ctx.has("nope"));
        assertNull(ctx.get("n"));
        assertNull(ctx.get("nope"));
    }

    @Test
    void 上下文的两个视图都不可改且行视图键大写() {
        StrategyContext ctx = new StrategyContext().fact("userId", 1).fact("a.b", 2).facts(null);
        Map<String, Object> row = ctx.asRow();
        assertEquals(1, row.get("USERID"));
        assertEquals(2, row.get("A.B"));
        assertThrows(UnsupportedOperationException.class, () -> ctx.asMap().clear());
        assertEquals(2, ctx.asMap().size());
        assertEquals(0, StrategyContext.of(null).asMap().size());
    }

    // ---------------- 构建期校验 ----------------

    @Test
    void 规则id必须非空且会被裁掉首尾空白() {
        assertThrows(IllegalArgumentException.class, () -> Rule.builder(null));
        assertThrows(IllegalArgumentException.class, () -> Rule.builder("  "));
        assertEquals("R1", Rule.builder("  R1  ").build().id());
        assertEquals(100, Rule.builder("R1").build().priority());
        assertTrue(Rule.builder("R1").build().enabled());
        assertNull(Rule.builder("R1").build().name());
        assertEquals("n", Rule.builder("R1").name("n").build().name());
    }

    @Test
    void 规则效果传null落回空效果() {
        Rule r = Rule.builder("R1").effect(null).build();
        assertTrue(r.effect().isEmpty());
        assertFalse(Rule.builder("R1").enabled(false).build().enabled());
        assertTrue(Rule.builder("R1").build().toString().contains("R1"));
    }

    @Test
    void 决策包名与版本必须非空() {
        assertThrows(IllegalArgumentException.class, () -> DecisionPackage.builder(null, "1"));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackage.builder(" ", "1"));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackage.builder("p", null));
        assertThrows(IllegalArgumentException.class, () -> DecisionPackage.builder("p", "  "));
        assertEquals("p", DecisionPackage.builder(" p ", " 1 ").build().name());
        assertEquals("1", DecisionPackage.builder("p", "1").build().version());
    }

    @Test
    void 决策包的可选参数留空时落回草稿与collect_all() {
        DecisionPackage pkg = DecisionPackage.builder("p", "1").runMode(null).matchSpec(null).build();
        assertEquals(RunMode.DRAFT, pkg.runMode());
        assertEquals(MatchMode.COLLECT_ALL, pkg.matchSpec().mode());
        assertTrue(pkg.rules().isEmpty());
        assertTrue(pkg.toString().contains("p@1"));
    }

    @Test
    void 决策包忽略null规则并可整批加入() {
        DecisionPackage pkg = DecisionPackage.builder("p", "1")
                .rule(null)
                .rules(null)
                .rules(Arrays.asList(Rule.builder("R1").build(), null))
                .build();
        assertEquals(1, pkg.rules().size());
        assertEquals("R1", pkg.rules().get(0).id());
    }

    @Test
    void 空包求值直接放行() {
        Decision d = DecisionPackage.builder("p", "1").build().evaluate(StrategyContext.of(null));
        assertTrue(d.isPass());
        assertEquals(0d, d.score(), 1e-9);
        assertTrue(RuleEvaluator.evaluate(null, new StrategyContext(), MatchSpec.collectAll()).isPass());
        assertTrue(RuleEvaluator.evaluate(Collections.<Rule>emptyList(),
                new StrategyContext(), MatchSpec.collectAll()).isPass());
    }
}
