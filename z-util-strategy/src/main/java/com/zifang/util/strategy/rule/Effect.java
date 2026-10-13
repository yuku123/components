package com.zifang.util.strategy.rule;

import com.zifang.util.strategy.DecisionOutcome;

/**
 * 规则命中后的效果：给结论、加减分、打标签。四者可以任意组合。
 */
public class Effect {

    private final DecisionOutcome outcome;
    private final String code;
    private final double scoreDelta;
    private final String tag;

    private Effect(DecisionOutcome outcome, String code, double scoreDelta, String tag) {
        this.outcome = outcome;
        this.code = code;
        this.scoreDelta = scoreDelta;
        this.tag = tag;
    }

    public static Effect none() {
        return new Effect(null, null, 0, null);
    }

    public static Effect of(DecisionOutcome outcome) {
        return new Effect(outcome, null, 0, null);
    }

    public static Effect of(DecisionOutcome outcome, String code) {
        return new Effect(outcome, code, 0, null);
    }

    public static Effect score(double delta) {
        return new Effect(null, null, delta, null);
    }

    public static Effect tag(String tag) {
        return new Effect(null, null, 0, tag);
    }

    public static Effect of(DecisionOutcome outcome, String code, double scoreDelta, String tag) {
        return new Effect(outcome, code, scoreDelta, tag);
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

    public boolean isEmpty() {
        return outcome == null && code == null && scoreDelta == 0 && tag == null;
    }

    @Override
    public String toString() {
        return "Effect{outcome=" + outcome + ", code=" + code + ", score=" + scoreDelta + ", tag=" + tag + '}';
    }
}
