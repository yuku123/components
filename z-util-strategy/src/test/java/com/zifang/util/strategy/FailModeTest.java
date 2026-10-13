package com.zifang.util.strategy;

import com.zifang.util.strategy.rule.CompareCondition;
import com.zifang.util.strategy.rule.Condition;
import com.zifang.util.strategy.rule.Conditions;
import com.zifang.util.strategy.rule.Effect;
import com.zifang.util.strategy.rule.MatchSpec;
import com.zifang.util.strategy.rule.Rule;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 条件求值失败时的处置，以及引擎两个槽位的切换守卫。
 * <p>
 * 用 {@code CAST('abc' AS INT)} 做触发条件：它能编译通过，但求值时抛 SqlException ——
 * 正好是"规则装配没问题、线上数据出问题"这一类真实故障。
 */
class FailModeTest {

    private static final Rule BROKEN = Rule.builder("BROKEN")
            .condition(Conditions.dsl("CAST('abc' AS INT)"))
            .effect(Effect.of(DecisionOutcome.REJECT))
            .build();

    private static Decision evaluate(Rule rule, MatchSpec spec) {
        return DecisionPackage.builder("p", "1").runMode(RunMode.ACTIVE)
                .matchSpec(spec)
                .rule(rule)
                .build()
                .evaluate(StrategyContext.of(new HashMap<>()));
    }

    // ---------------- fail-open / fail-closed ----------------

    @Test
    void fail_closed把出错当成条件不成立() {
        Decision d = evaluate(BROKEN, MatchSpec.firstMatch().onError(MatchSpec.OnError.FAIL_CLOSED));
        assertEquals(DecisionOutcome.PASS, d.outcome());
        assertTrue(d.hits().isEmpty());
    }

    @Test
    void fail_open把出错当成条件成立并照常施加效果() {
        Decision d = evaluate(BROKEN, MatchSpec.firstMatch().onError(MatchSpec.OnError.FAIL_OPEN));
        assertEquals(DecisionOutcome.REJECT, d.outcome());
        assertEquals(1, d.hits().size());
        assertEquals("BROKEN", d.hits().get(0).ruleId());
    }

    @Test
    void 不显式声明就走fail_closed() {
        assertEquals(MatchSpec.OnError.FAIL_CLOSED, MatchSpec.firstMatch().onError());
        assertEquals(DecisionOutcome.PASS, evaluate(BROKEN, MatchSpec.firstMatch()).outcome());
        assertEquals(DecisionOutcome.PASS, evaluate(BROKEN, MatchSpec.collectAll()).outcome());
    }

    @Test
    void 出错这一事实一定留在轨迹里() {
        Decision closed = evaluate(BROKEN, MatchSpec.firstMatch());
        assertEquals(2, closed.trace().size());
        assertEquals("rule", closed.trace().steps().get(0).source());
        assertEquals("BROKEN", closed.trace().steps().get(0).ruleId());
        assertFalse(closed.trace().steps().get(0).matched());
        assertTrue(closed.trace().steps().get(0).error().contains("CAST"));

        Decision open = evaluate(BROKEN, MatchSpec.firstMatch().onError(MatchSpec.OnError.FAIL_OPEN));
        // 出错 + 命中两步都要能看到，只看结论会误判成"规则正常命中"
        assertEquals(2, open.trace().size());
        assertTrue(open.trace().steps().get(0).error() != null);
        assertTrue(open.trace().steps().get(1).matched());
    }

    @Test
    void 出错不影响同包其它规则的求值() {
        Decision d = DecisionPackage.builder("p", "1").runMode(RunMode.ACTIVE)
                .matchSpec(MatchSpec.firstMatch().onError(MatchSpec.OnError.FAIL_CLOSED))
                .rule(BROKEN)
                .rule(Rule.builder("AFTER").priority(200)
                        .condition(Conditions.eq("a", 1))
                        .effect(Effect.of(DecisionOutcome.MANUAL)).build())
                .build()
                .evaluate(StrategyContext.of(facts("a", 1)));
        assertEquals(DecisionOutcome.MANUAL, d.outcome());
        assertEquals(1, d.hits().size());
        assertEquals("AFTER", d.hits().get(0).ruleId());
    }

    // ---------------- 槽位切换守卫 ----------------

    private static DecisionPackage pkg(String version, RunMode mode) {
        return DecisionPackage.builder("p", version).runMode(mode)
                .rule(Rule.builder("R").condition(Condition.alwaysTrue())
                        .effect(Effect.of(DecisionOutcome.REJECT)).build())
                .build();
    }

    private static Map<String, Object> facts(Object... kv) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            map.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return map;
    }

    @Test
    void 未装包就求值直接报错() {
        DecisionEngine engine = DecisionEngine.create();
        assertNull(engine.currentPackage());
        assertThrows(IllegalStateException.class, () -> engine.evaluate(facts(), "u1"));
    }

    @Test
    void 装包时拒草稿与null() {
        DecisionEngine engine = DecisionEngine.create();
        assertThrows(IllegalArgumentException.class, () -> engine.install(null));
        assertThrows(IllegalArgumentException.class, () -> engine.install(pkg("1", RunMode.DRAFT)));
    }

    @Test
    void 未装current不能promote() {
        DecisionEngine engine = DecisionEngine.create();
        assertThrows(IllegalStateException.class,
                () -> engine.promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(10, "userId")));
    }

    @Test
    void promote拒草稿与同一个包() {
        DecisionPackage cur = pkg("1", RunMode.ACTIVE);
        DecisionEngine engine = DecisionEngine.create().install(cur);
        assertThrows(IllegalArgumentException.class,
                () -> engine.promote(pkg("2", RunMode.DRAFT), TrafficPolicy.of(10, "userId")));
        assertThrows(IllegalArgumentException.class,
                () -> engine.promote(cur, TrafficPolicy.of(10, "userId")));
        assertThrows(IllegalArgumentException.class, () -> engine.promote(null, null));
    }

    @Test
    void 装新包会顺带结束灰度() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(100, "userId"));
        assertNotNull(engine.candidatePackage());
        assertEquals("2", engine.candidatePackage().version());
        assertEquals(100, engine.trafficPolicy().percent());

        engine.install(pkg("3", RunMode.ACTIVE));
        assertNull(engine.candidatePackage());
        assertEquals(0, engine.trafficPolicy().percent());
        assertEquals("3", engine.evaluate(facts(), "u1").appliedVersion());
    }

    @Test
    void promote传null策略等价于不放量() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.ACTIVE), null);
        assertEquals(0, engine.trafficPolicy().percent());
        assertEquals("1", engine.evaluate(facts(), "u1").appliedVersion());
    }

    @Test
    void 策略类setter传null落回禁用() {
        DecisionEngine engine = DecisionEngine.create().install(pkg("1", RunMode.ACTIVE));
        engine.controlPolicy(null).metrics(null);
        engine.promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(100, "userId"));
        DecisionResult r = engine.evaluate(facts(), "u1");
        assertEquals(DecisionOutcome.REJECT, r.effectiveOutcome());
        assertFalse(r.controlReleased());
        assertTrue(r.hitGray());
    }

    @Test
    void 结束灰度后candidate被清空且策略复位() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(100, "userId"));
        assertEquals("2", engine.evaluate(facts(), "u1").appliedVersion());

        engine.clearCandidate();
        assertNull(engine.candidatePackage());
        assertEquals(0, engine.trafficPolicy().percent());
        assertEquals("1", engine.evaluate(facts(), "u1").appliedVersion());
        assertFalse(engine.evaluate(facts(), "u1").hitGray());
    }

    @Test
    void 试运行包的流量策略不参与判定() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.TRIAL), TrafficPolicy.of(100, "userId"));

        DecisionResult r = engine.evaluate(facts(), "u1");
        assertEquals("1", r.appliedVersion());
        assertFalse(r.hitGray());
        assertNotNullShadow(r);
    }

    private static void assertNotNullShadow(DecisionResult r) {
        assertTrue(r.shadow() != null, "试运行必须产出影子结论");
        assertFalse(r.shadowDiffers(), "两个包规则相同，结论应一致");
    }

    @Test
    void 灰度命中与未命中分别记不同版本() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(100, "userId"));
        assertTrue(engine.evaluate(facts(), "u1").hitGray());

        engine.clearCandidate();
        engine.promote(pkg("3", RunMode.ACTIVE), TrafficPolicy.of(0, "userId"));
        DecisionResult r = engine.evaluate(facts(), "u1");
        assertFalse(r.hitGray());
        assertEquals("1", r.appliedVersion());
        assertEquals("3", engine.candidatePackage().version());
        assertEquals("1", r.currentVersion());
        assertEquals("p", r.packageName());
        assertNull(r.shadow());
    }

    @Test
    void 引擎的toString能看出两个槽位() {
        DecisionEngine engine = DecisionEngine.create().install(pkg("1", RunMode.ACTIVE));
        assertTrue(engine.toString().contains("current"));
        assertTrue(engine.toString().contains("candidate"));
    }

    @Test
    void 结果的toString带灰度与对照组标记() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(100, "userId"))
                .controlPolicy(ControlPolicy.of(100, "userId"));
        DecisionResult r = engine.evaluate(facts(), "u1");
        assertTrue(r.toString().contains("[gray]"));
        assertTrue(r.toString().contains("[control]"));
        assertTrue(r.toString().contains("p@2"));
    }

    @Test
    void 落对照组时对外结论为pass但版本仍是灰度包() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .promote(pkg("2", RunMode.ACTIVE), TrafficPolicy.of(100, "userId"))
                .controlPolicy(ControlPolicy.of(100, "userId"));
        DecisionResult r = engine.evaluate(facts(), "u1");
        assertEquals(DecisionOutcome.REJECT, r.decision().outcome());
        assertEquals(DecisionOutcome.PASS, r.effectiveOutcome());
        assertEquals("2", r.appliedVersion());
        assertTrue(r.controlReleased());
    }

    @Test
    void 非对照组样本的对外结论就是决策结论() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .controlPolicy(ControlPolicy.of(50, "userId"));
        for (int i = 0; i < 200; i++) {
            DecisionResult r = engine.evaluate(facts(), "u" + i);
            assertEquals(r.controlReleased() ? DecisionOutcome.PASS : DecisionOutcome.REJECT,
                    r.effectiveOutcome());
        }
    }

    @Test
    void 每次求值都会回调指标() {
        DecisionMetrics.InMemory metrics = new DecisionMetrics.InMemory();
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("1", RunMode.ACTIVE))
                .metrics(metrics);
        engine.evaluate(facts(), "u1");
        engine.evaluate(facts(), "u2");
        assertEquals(2, metrics.total());
        assertEquals(2, metrics.ruleHitCount("R"));
    }
}