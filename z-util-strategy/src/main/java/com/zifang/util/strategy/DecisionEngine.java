package com.zifang.util.strategy;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 决策引擎：持有 current 与 candidate 两个决策包槽位，按运行模式与流量策略决定本次谁生效。
 *
 * <pre>
 * candidate == null                   → current 出决策
 * candidate.runMode == TRIAL          → current 出决策；candidate 全流量执行，结论进 shadow（不生效）
 * candidate.runMode == ACTIVE 且命中分片 → candidate 出决策，hitGray = true
 * </pre>
 *
 * 两个槽位都是 {@link AtomicReference}：切换是换引用，天然原子；
 * 回滚就是重新 {@link #install(DecisionPackage)} 旧快照，不需要额外的历史栈。
 */
public class DecisionEngine {

    private final AtomicReference<DecisionPackage> current = new AtomicReference<>();
    private final AtomicReference<DecisionPackage> candidate = new AtomicReference<>();

    private volatile TrafficPolicy trafficPolicy = TrafficPolicy.none();
    private volatile ControlPolicy controlPolicy = ControlPolicy.disabled();
    private volatile DecisionMetrics metrics;

    public static DecisionEngine create() {
        return new DecisionEngine();
    }

    /** 装载生效包，同时清掉进行中的灰度 */
    public DecisionEngine install(DecisionPackage pkg) {
        if (pkg == null) {
            throw new IllegalArgumentException("package must not be null");
        }
        if (pkg.runMode() == RunMode.DRAFT) {
            throw new IllegalArgumentException(
                    "cannot install a DRAFT package as current; a draft does not execute");
        }
        current.set(pkg);
        candidate.set(null);
        trafficPolicy = TrafficPolicy.none();
        return this;
    }

    /**
     * 开启灰度或试运行。
     *
     * @param pkg    待验证的新包
     * @param policy 放量策略；{@link RunMode#TRIAL} 下不参与判定
     */
    public DecisionEngine promote(DecisionPackage pkg, TrafficPolicy policy) {
        if (pkg == null) {
            throw new IllegalArgumentException("package must not be null");
        }
        if (current.get() == null) {
            throw new IllegalStateException("install a current package before promoting a candidate");
        }
        if (pkg == current.get()) {
            throw new IllegalArgumentException("candidate must differ from the current package");
        }
        if (pkg.runMode() == RunMode.DRAFT) {
            throw new IllegalArgumentException("a DRAFT package never executes; use TRIAL or ACTIVE");
        }
        this.trafficPolicy = policy == null ? TrafficPolicy.none() : policy;
        candidate.set(pkg);
        return this;
    }

    /** 结束灰度/试运行，回到只用 current */
    public DecisionEngine clearCandidate() {
        candidate.set(null);
        trafficPolicy = TrafficPolicy.none();
        return this;
    }

    public DecisionEngine controlPolicy(ControlPolicy controlPolicy) {
        this.controlPolicy = controlPolicy == null ? ControlPolicy.disabled() : controlPolicy;
        return this;
    }

    public DecisionEngine metrics(DecisionMetrics metrics) {
        this.metrics = metrics;
        return this;
    }

    public DecisionPackage currentPackage() {
        return current.get();
    }

    public DecisionPackage candidatePackage() {
        return candidate.get();
    }

    public TrafficPolicy trafficPolicy() {
        return trafficPolicy;
    }

    public DecisionResult evaluate(Map<String, Object> facts, Object splitKey) {
        return evaluate(StrategyContext.of(facts), splitKey);
    }

    public DecisionResult evaluate(StrategyContext context, Object splitKey) {
        DecisionPackage cur = current.get();
        if (cur == null) {
            throw new IllegalStateException("no current package installed");
        }

        DecisionPackage cand = candidate.get();
        Decision effective;
        Decision shadow = null;
        String appliedVersion;
        boolean hitGray = false;

        if (cand == null || cand.runMode() == RunMode.DRAFT) {
            effective = cur.evaluate(context);
            appliedVersion = cur.version();
        } else if (cand.runMode() == RunMode.TRIAL) {
            // 试运行：全流量跑新包但不生效，结论只进 shadow 供新旧差异比对
            effective = cur.evaluate(context);
            shadow = cand.evaluate(context);
            appliedVersion = cur.version();
        } else if (trafficPolicy.hit(splitKey)) {
            effective = cand.evaluate(context);
            appliedVersion = cand.version();
            hitGray = true;
        } else {
            effective = cur.evaluate(context);
            appliedVersion = cur.version();
        }

        boolean controlReleased = !effective.isPass() && controlPolicy.inControl(splitKey);

        DecisionResult result = new DecisionResult(effective, shadow, cur.name(),
                cur.version(), appliedVersion, hitGray, controlReleased);

        DecisionMetrics m = metrics;
        if (m != null) {
            m.record(result);
        }
        return result;
    }

    @Override
    public String toString() {
        return "DecisionEngine{current=" + current.get()
                + ", candidate=" + candidate.get()
                + ", " + trafficPolicy + ", " + controlPolicy + '}';
    }
}
