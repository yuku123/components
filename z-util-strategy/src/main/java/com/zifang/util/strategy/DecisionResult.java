package com.zifang.util.strategy;

/**
 * 一次决策的完整结果：生效的决策 + 试运行影子决策 + 版本与分片标记。
 * <p>
 * 试运行（{@link RunMode#TRIAL}）下 candidate 包全流量执行但结论不生效，
 * 落在 {@link #shadow()} 里，供新旧输出差异比对。
 */
public class DecisionResult {

    private final Decision decision;
    private final Decision shadow;
    private final String currentVersion;
    private final String appliedVersion;
    private final String packageName;
    private final boolean hitGray;
    private final boolean controlReleased;

    DecisionResult(Decision decision, Decision shadow, String packageName,
                   String currentVersion, String appliedVersion, boolean hitGray, boolean controlReleased) {
        this.decision = decision;
        this.shadow = shadow;
        this.packageName = packageName;
        this.currentVersion = currentVersion;
        this.appliedVersion = appliedVersion;
        this.hitGray = hitGray;
        this.controlReleased = controlReleased;
    }

    /** 生效的决策。落入对照组时它已被改写为 PASS */
    public Decision decision() {
        return decision;
    }

    /** 试运行包的结果，未生效；无试运行时为 null */
    public Decision shadow() {
        return shadow;
    }

    public String packageName() {
        return packageName;
    }

    /** current 槽位上的包版本 */
    public String currentVersion() {
        return currentVersion;
    }

    /** 实际生效的包版本；灰度命中时是 candidate 的版本 */
    public String appliedVersion() {
        return appliedVersion;
    }

    /** 本次决策是否走了 candidate 分片 */
    public boolean hitGray() {
        return hitGray;
    }

    /** 本次决策是否被对照组放行 */
    public boolean controlReleased() {
        return controlReleased;
    }

    /**
     * 应当对外生效的结论。
     * <p>
     * 落入对照组时返回 PASS，但 {@link #decision()} 仍保留原始拦截结论 ——
     * 这批样本事后要人工复核，证据不能被引擎抹掉。
     */
    public DecisionOutcome effectiveOutcome() {
        return controlReleased ? DecisionOutcome.PASS : decision.outcome();
    }

    /** 试运行结论与生效结论是否不一致 —— 差异率是判断新规则能不能转正的核心指标 */
    public boolean shadowDiffers() {
        return shadow != null && shadow.outcome() != decision.outcome();
    }

    @Override
    public String toString() {
        return "DecisionResult{" + packageName + "@" + appliedVersion
                + (hitGray ? " [gray]" : "")
                + (controlReleased ? " [control]" : "")
                + " " + decision
                + (shadow != null ? " shadow=" + shadow : "")
                + '}';
    }
}
