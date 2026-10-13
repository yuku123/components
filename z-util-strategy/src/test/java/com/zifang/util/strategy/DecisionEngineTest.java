package com.zifang.util.strategy;

import com.zifang.util.strategy.loader.DecisionPackageLoader;
import com.zifang.util.strategy.rule.Condition;
import com.zifang.util.strategy.rule.Conditions;
import com.zifang.util.strategy.rule.Effect;
import com.zifang.util.strategy.rule.MatchSpec;
import com.zifang.util.strategy.rule.Rule;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecisionEngineTest {

    private static StrategyContext facts(Object... kv) {
        StrategyContext ctx = new StrategyContext();
        for (int i = 0; i < kv.length; i += 2) {
            ctx.fact(String.valueOf(kv[i]), kv[i + 1]);
        }
        return ctx;
    }

    private static Rule rule(String id, int priority, Condition condition, Effect effect) {
        return Rule.builder(id).priority(priority).condition(condition).effect(effect).build();
    }

    // ---------------- 条件求值 ----------------

    @Test
    void 比较与逻辑组合翻译成dsl并正确求值() {
        Condition c = Conditions.and(
                Conditions.gt("amount", 10000),
                Conditions.in("country", "CN", "US"),
                Conditions.or(Conditions.eq("vipLevel", 3), Conditions.like("deviceId", "LD%")));

        assertEquals(DecisionOutcome.REJECT, DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, c, Effect.of(DecisionOutcome.REJECT)))
                .build().evaluate(facts("amount", 50000, "country", "CN", "vipLevel", 1, "deviceId", "LD8899")).outcome());
        assertEquals(DecisionOutcome.PASS, DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, c, Effect.of(DecisionOutcome.REJECT)))
                .build().evaluate(facts("amount", 50000, "country", "CN", "vipLevel", 1, "deviceId", "XX0000")).outcome());
    }

    @Test
    void 驼峰事实键能对上dsl的大写列引用() {
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, Conditions.gt("userId", 1000), Effect.of(DecisionOutcome.REVIEW)))
                .build().evaluate(facts("userId", 4321));
        assertEquals(DecisionOutcome.REVIEW, d.outcome());
    }

    @Test
    void 缺失事实按null求值而不是抛异常() {
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, Conditions.isNotNull("channel"), Effect.of(DecisionOutcome.MANUAL)))
                .build().evaluate(facts());
        assertEquals(DecisionOutcome.PASS, d.outcome());
        assertEquals(1, d.trace().size(), "未命中的规则也要留 trace，否则无法解释为什么放行");
    }

    @Test
    void 字符串字面量里的引号被转义() {
        Condition c = Conditions.eq("merchant", "O'Brien & Co");
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, c, Effect.of(DecisionOutcome.REJECT)))
                .build().evaluate(facts("merchant", "O'Brien & Co"));
        assertEquals(DecisionOutcome.REJECT, d.outcome());
    }

    @Test
    void 非法字段名在构建期就被挡住() {
        assertThrows(IllegalArgumentException.class, () -> Conditions.eq("amount) OR (1=1", 1));
        assertThrows(IllegalArgumentException.class, () -> Conditions.compare("amount", "DROP TABLE", 1));
    }

    @Test
    void between_展开为闭区间() {
        Condition c = Conditions.between("amount", 100, 200);
        assertEquals(DecisionOutcome.REJECT, DecisionPackage.builder("p", "1").runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, c, Effect.of(DecisionOutcome.REJECT)))
                .build().evaluate(facts("amount", 150)).outcome());
        assertEquals(DecisionOutcome.PASS, DecisionPackage.builder("p", "1").runMode(RunMode.ACTIVE)
                .rule(rule("R", 1, c, Effect.of(DecisionOutcome.REJECT)))
                .build().evaluate(facts("amount", 900)).outcome());
    }

    // ---------------- 规则编排 ----------------

    @Test
    void 优先级升序求值且first_match短路() {
        Rule low = rule("R-low", 50, Conditions.gt("amount", 1000), Effect.of(DecisionOutcome.REVIEW));
        Rule high = rule("R-high", 10, Conditions.gt("amount", 5000), Effect.of(DecisionOutcome.REJECT));
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .matchSpec(MatchSpec.firstMatch())
                .rule(low).rule(high)
                .build().evaluate(facts("amount", 9000));
        assertEquals(DecisionOutcome.REJECT, d.outcome());
        assertEquals(1, d.hits().size(), "FIRST_MATCH 命中第一条即停");
        assertEquals("R-high", d.hits().get(0).ruleId());
    }

    @Test
    void collect_all_累计分与标签() {
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .matchSpec(MatchSpec.collectAll())
                .rule(rule("R1", 10, Conditions.gt("amount", 1000), Effect.score(40)))
                .rule(rule("R2", 20, Conditions.eq("vipLevel", 3), Effect.tag("vip")))
                .rule(rule("R3", 30, Conditions.eq("blocked", true), Effect.of(DecisionOutcome.REJECT)))
                .build().evaluate(facts("amount", 5000, "vipLevel", 3, "blocked", false));
        assertEquals(40d, d.score());
        assertTrue(d.tags().contains("vip"));
        assertEquals(2, d.hits().size());
    }

    @Test
    void threshold_达到分值即停() {
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .matchSpec(MatchSpec.threshold(100))
                .rule(rule("R1", 10, Condition.alwaysTrue(), Effect.score(60)))
                .rule(rule("R2", 20, Condition.alwaysTrue(), Effect.score(60)))
                .rule(rule("R3", 30, Condition.alwaysTrue(), Effect.score(60)))
                .build().evaluate(new StrategyContext());
        assertEquals(2, d.hits().size(), "累到 120 即停，不跑第三条");
        assertEquals(120d, d.score());
        assertEquals(DecisionOutcome.SCORE, d.outcome());
    }

    @Test
    void 禁用规则不参与求值() {
        Decision d = DecisionPackage.builder("p", "1")
                .runMode(RunMode.ACTIVE)
                .rule(Rule.builder("R1").condition(Condition.alwaysTrue())
                        .effect(Effect.of(DecisionOutcome.REJECT)).enabled(false).build())
                .build().evaluate(new StrategyContext());
        assertEquals(DecisionOutcome.PASS, d.outcome());
        assertEquals(0, d.trace().size());
    }

    // ---------------- 试运行 / 灰度 / 对照组 ----------------

    private static DecisionPackage pkg(String name, String version, RunMode mode, Effect effect) {
        return DecisionPackage.builder(name, version)
                .runMode(mode)
                .rule(rule("R-" + name, 10, Conditions.gt("amount", 1000), effect))
                .build();
    }

    @Test
    void 试运行全流量跑但不生效_结论落在shadow() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS)))
                .promote(pkg("candidate", "2", RunMode.TRIAL, Effect.of(DecisionOutcome.REJECT)), TrafficPolicy.none());

        DecisionResult r = engine.evaluate(facts("amount", 9999), "user-1");

        assertEquals(DecisionOutcome.PASS, r.decision().outcome(), "试运行不改变生效结论");
        assertEquals("1", r.appliedVersion());
        assertNotNull(r.shadow(), "新包全流量执行，结论在 shadow");
        assertEquals(DecisionOutcome.REJECT, r.shadow().outcome());
        assertTrue(r.shadowDiffers());
        assertFalse(r.hitGray());
    }

    @Test
    void 灰度按稳定哈希分流_同一标识不抖动() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS)))
                .promote(pkg("candidate", "2", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT)),
                        TrafficPolicy.of(50, "userId"));

        boolean sawGray = false;
        boolean sawNormal = false;
        for (int i = 0; i < 200; i++) {
            String user = "user-" + i;
            DecisionResult first = engine.evaluate(facts("amount", 9999), user);
            DecisionResult again = engine.evaluate(facts("amount", 9999), user);
            assertEquals(first.hitGray(), again.hitGray(), "同一标识两次必须落同一分片");
            assertEquals(first.hitGray() ? DecisionOutcome.REJECT : DecisionOutcome.PASS,
                    first.decision().outcome());
            sawGray |= first.hitGray();
            sawNormal |= !first.hitGray();
        }
        assertTrue(sawGray && sawNormal, "50% 放量两个分片都该有流量");
    }

    @Test
    void 灰度全量时全命中_放量为零时全不命中() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS)));
        engine.promote(pkg("candidate", "2", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT)),
                TrafficPolicy.of(100, "userId"));
        assertTrue(engine.evaluate(facts("amount", 1), "u").hitGray());

        engine.promote(pkg("candidate3", "3", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT)),
                TrafficPolicy.of(0, "userId"));
        assertFalse(engine.evaluate(facts("amount", 1), "u").hitGray());
    }

    @Test
    void 对照组放行但保留原始拦截证据() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT)))
                .controlPolicy(ControlPolicy.of(100, "userId"));

        DecisionResult r = engine.evaluate(facts("amount", 9999), "user-1");

        assertTrue(r.controlReleased());
        assertEquals(DecisionOutcome.PASS, r.effectiveOutcome(), "对外放行");
        assertEquals(DecisionOutcome.REJECT, r.decision().outcome(), "原始结论保留，供事后人工复核");
    }

    @Test
    void 放行为pass时不计入对照组() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS)))
                .controlPolicy(ControlPolicy.of(100, "userId"));
        assertFalse(engine.evaluate(facts("amount", 1), "user-1").controlReleased());
    }

    // ---------------- 包切换 ----------------

    @Test
    void 回滚就是把旧快照装回去() {
        DecisionPackage v1 = pkg("risk", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS));
        DecisionPackage v2 = pkg("risk", "2", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT));
        DecisionEngine engine = DecisionEngine.create().install(v1);
        assertEquals(DecisionOutcome.PASS, engine.evaluate(facts("amount", 9999), "u").decision().outcome());

        engine.install(v2);
        assertEquals(DecisionOutcome.REJECT, engine.evaluate(facts("amount", 9999), "u").decision().outcome());

        engine.install(v1);
        assertEquals(DecisionOutcome.PASS, engine.evaluate(facts("amount", 9999), "u").decision().outcome());
    }

    @Test
    void 装草稿包和未装包都报错() {
        DecisionEngine engine = DecisionEngine.create();
        assertThrows(IllegalStateException.class, () -> engine.evaluate(new StrategyContext(), "u"));
        assertThrows(IllegalArgumentException.class,
                () -> engine.install(pkg("d", "1", RunMode.DRAFT, Effect.none())));
        engine.install(pkg("c", "1", RunMode.ACTIVE, Effect.none()));
        assertThrows(IllegalArgumentException.class,
                () -> engine.promote(pkg("d", "2", RunMode.DRAFT, Effect.none()), TrafficPolicy.none()));
    }

    @Test
    void 结束灰度后candidate出流量() {
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT)))
                .promote(pkg("candidate", "2", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS)),
                        TrafficPolicy.of(100, "userId"));
        assertEquals(DecisionOutcome.PASS, engine.evaluate(facts("amount", 9999), "u").decision().outcome());

        engine.clearCandidate();
        DecisionResult r = engine.evaluate(facts("amount", 9999), "u");
        assertEquals(DecisionOutcome.REJECT, r.decision().outcome());
        assertNull(engine.candidatePackage());
    }

    // ---------------- 指标 ----------------

    @Test
    void 指标记下灰度试运行对照组与规则命中() {
        DecisionMetrics.InMemory metrics = new DecisionMetrics.InMemory();
        DecisionEngine engine = DecisionEngine.create()
                .install(pkg("current", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT)))
                .metrics(metrics);

        engine.evaluate(facts("amount", 9999), "user-1");
        engine.evaluate(facts("amount", 1), "user-2");

        assertEquals(2, metrics.total());
        assertEquals(1, metrics.outcomeCount(DecisionOutcome.REJECT));
        assertEquals(1, metrics.ruleHitCount("R-current"));
        assertEquals(0, metrics.control(), "未配对照组时不应有放行计数");
        assertEquals(0, metrics.trial());
    }

    // ---------------- JSON 载入 ----------------

    @Test
    void json_载入dsl文本条件() {
        DecisionPackage p = DecisionPackageLoader.fromJson("{"
                + "\"name\":\"大额异常交易\",\"version\":\"3\",\"runMode\":\"ACTIVE\","
                + "\"match\":{\"mode\":\"COLLECT_ALL\",\"onError\":\"FAIL_CLOSED\"},"
                + "\"rules\":[{\"id\":\"R001\",\"priority\":10,"
                + "\"condition\":\"amount > 10000 AND country <> 'CN'\","
                + "\"effect\":{\"outcome\":\"REVIEW\",\"code\":\"BIG\",\"score\":60,\"tag\":\"big\"}}]}");

        assertEquals("大额异常交易", p.name());
        assertEquals("3", p.version());
        assertEquals(RunMode.ACTIVE, p.runMode());

        Decision hit = p.evaluate(facts("amount", 50000, "country", "US"));
        assertEquals(DecisionOutcome.REVIEW, hit.outcome());
        assertEquals("BIG", hit.code());
        assertEquals(60d, hit.score());
        assertTrue(hit.tags().contains("big"));

        assertEquals(DecisionOutcome.PASS, p.evaluate(facts("amount", 50000, "country", "CN")).outcome());
    }

    @Test
    void json_载入结构化条件() {
        DecisionPackage p = DecisionPackageLoader.fromJson("{"
                + "\"name\":\"设备风控\",\"version\":\"1\",\"runMode\":\"ACTIVE\","
                + "\"rules\":[{\"id\":\"R1\",\"condition\":{\"and\":["
                + "{\"field\":\"vipLevel\",\"op\":\"<=\",\"value\":2},"
                + "{\"field\":\"deviceId\",\"op\":\"REGEXP\",\"value\":\"LD[0-9]{4}\"},"
                + "{\"field\":\"country\",\"op\":\"IN\",\"value\":[\"CN\",\"US\"]},"
                + "{\"not\":{\"field\":\"channel\",\"op\":\"IS_NULL\"}}]},"
                + "\"effect\":{\"outcome\":\"REJECT\"}}]}");

        assertEquals(DecisionOutcome.REJECT,
                p.evaluate(facts("vipLevel", 1, "deviceId", "LD8899", "country", "CN", "channel", "app")).outcome());
        assertEquals(DecisionOutcome.PASS,
                p.evaluate(facts("vipLevel", 1, "deviceId", "LD8899", "country", "CN")).outcome(), "channel 缺失则 NOT 成立但 IS_NULL 也成立，规则不应命中");
        assertEquals(DecisionOutcome.PASS,
                p.evaluate(facts("vipLevel", 9, "deviceId", "LD8899", "country", "CN", "channel", "app")).outcome());
    }

    @Test
    void json_between_操作符() {
        DecisionPackage p = DecisionPackageLoader.fromJson("{"
                + "\"name\":\"区间\",\"version\":\"1\",\"runMode\":\"ACTIVE\","
                + "\"rules\":[{\"id\":\"R1\",\"condition\":{\"field\":\"amount\",\"op\":\"BETWEEN\",\"value\":[100,200]},"
                + "\"effect\":{\"outcome\":\"REJECT\"}}]}");
        assertEquals(DecisionOutcome.REJECT, p.evaluate(facts("amount", 150)).outcome());
        assertEquals(DecisionOutcome.PASS, p.evaluate(facts("amount", 300)).outcome());
    }

    @Test
    void json_缺必填字段直接报错() {
        assertThrows(IllegalArgumentException.class,
                () -> DecisionPackageLoader.fromJson("{\"version\":\"1\"}"));
        assertThrows(IllegalArgumentException.class,
                () -> DecisionPackageLoader.fromJson("{\"name\":\"x\"}"));
    }

    // ---------------- 并发 ----------------

    @Test
    void 决策包多线程共享安全_切换原子() throws Exception {
        DecisionPackage v1 = pkg("risk", "1", RunMode.ACTIVE, Effect.of(DecisionOutcome.PASS));
        DecisionPackage v2 = pkg("risk", "2", RunMode.ACTIVE, Effect.of(DecisionOutcome.REJECT));
        DecisionEngine engine = DecisionEngine.create().install(v1);

        int threads = 8;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    for (int n = 0; n < 2000; n++) {
                        DecisionResult r = engine.evaluate(facts("amount", 9999), "user-" + n);
                        assertTrue(r.decision().outcome() == DecisionOutcome.PASS
                                || r.decision().outcome() == DecisionOutcome.REJECT);
                    }
                } catch (Throwable t) {
                    failure.compareAndSet(null, t);
                } finally {
                    done.countDown();
                }
            });
        }
        for (int i = 0; i < 500; i++) {
            engine.install((i % 2 == 0) ? v2 : v1);
        }
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS));
        pool.shutdownNow();
        assertNull(failure.get(), "并发求值不得抛异常: " + failure.get());
    }

    @Test
    void 事实集视图不可改() {
        StrategyContext ctx = facts("amount", 1);
        Map<String, Object> view = ctx.asMap();
        assertThrows(UnsupportedOperationException.class, () -> view.put("x", 1));
    }

    @Test
    void 决策包immutable_规则按优先级冻结排序() {
        DecisionPackage p = DecisionPackage.builder("p", "1")
                .rule(rule("B", 20, Condition.alwaysTrue(), Effect.none()))
                .rule(rule("A", 10, Condition.alwaysTrue(), Effect.none()))
                .build();
        assertEquals("A", p.rules().get(0).id());
        assertEquals("B", p.rules().get(1).id());
        assertThrows(UnsupportedOperationException.class, () -> p.rules().add(rule("C", 30, Condition.alwaysTrue(), Effect.none())));
    }
}
