package com.zifang.util.strategy.rule;

import com.zifang.util.strategy.DecisionOutcome;

/**
 * 规则命中记录。决策结果里带上它，业务方无需再查一次规则就能解释结论来源。
 */
public class HitRecord {

    private final String ruleId;
    private final String ruleName;
    private final DecisionOutcome outcome;
    private final String code;
    private final double scoreDelta;
    private final String tag;

    public HitRecord(String ruleId, String ruleName, Effect effect) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.outcome = effect.outcome();
        this.code = effect.code();
        this.scoreDelta = effect.scoreDelta();
        this.tag = effect.tag();
    }

    public String ruleId() {
        return ruleId;
    }

    public String ruleName() {
        return ruleName;
    }

    public DecisionOutcome outcome() {
        return outcome;
    }

    public String code() {
        return code;
    }

    public double scoreDelta() {
        return scoreDelta;
    }

    public String tag() {
        return tag;
    }

    @Override
    public String toString() {
        return ruleId + "[" + ruleName + "] " + outcome
                + (code != null ? "/" + code : "")
                + (scoreDelta != 0 ? " score=" + scoreDelta : "")
                + (tag != null ? " tag=" + tag : "");
    }
}
