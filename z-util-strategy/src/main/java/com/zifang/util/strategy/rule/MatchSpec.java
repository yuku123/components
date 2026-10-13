package com.zifang.util.strategy.rule;

/**
 * 规则编排参数。
 * <p>
 * {@code onError} 必须显式声明：条件求值出错时是当作不通过（fail-closed）还是当作通过（fail-open）。
 * 风控两个方向的代价不对称 —— 反欺诈宁可误杀、反洗钱宁可放过 —— 所以不给隐式默认行为。
 */
public class MatchSpec {

    /**
     * 条件求值出错时的处置。
     */
    public enum OnError {
        /** 出错视为条件不成立（规则不命中） */
        FAIL_CLOSED,
        /** 出错视为条件成立（规则命中） */
        FAIL_OPEN
    }

    private static final MatchSpec FIRST_MATCH = new MatchSpec(MatchMode.FIRST_MATCH, 0, OnError.FAIL_CLOSED);
    private static final MatchSpec COLLECT_ALL = new MatchSpec(MatchMode.COLLECT_ALL, 0, OnError.FAIL_CLOSED);

    private final MatchMode mode;
    private final double threshold;
    private final OnError onError;

    private MatchSpec(MatchMode mode, double threshold, OnError onError) {
        this.mode = mode;
        this.threshold = threshold;
        this.onError = onError;
    }

    public static MatchSpec firstMatch() {
        return FIRST_MATCH;
    }

    public static MatchSpec collectAll() {
        return COLLECT_ALL;
    }

    public static MatchSpec threshold(double threshold) {
        return new MatchSpec(MatchMode.THRESHOLD, threshold, OnError.FAIL_CLOSED);
    }

    public static MatchSpec of(MatchMode mode, double threshold, OnError onError) {
        return new MatchSpec(mode, threshold, onError);
    }

    public MatchSpec onError(OnError onError) {
        return new MatchSpec(mode, threshold, onError == null ? OnError.FAIL_CLOSED : onError);
    }

    public MatchMode mode() {
        return mode;
    }

    public double threshold() {
        return threshold;
    }

    public OnError onError() {
        return onError;
    }

    @Override
    public String toString() {
        return "MatchSpec{" + mode + (mode == MatchMode.THRESHOLD ? "@" + threshold : "")
                + ", onError=" + onError + '}';
    }
}
