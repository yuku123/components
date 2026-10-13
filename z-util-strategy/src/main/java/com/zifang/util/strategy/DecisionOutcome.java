package com.zifang.util.strategy;

/**
 * 决策结论。severity 越大越严重，用于多规则/多版本合并时取最严重结论。
 */
public enum DecisionOutcome {

    /** 放行 */
    PASS(0),
    /** 无直接结论，仅贡献分值 */
    SCORE(1),
    /** 需自动复核 */
    REVIEW(2),
    /** 需人工介入 */
    MANUAL(3),
    /** 拒绝 */
    REJECT(4);

    private final int severity;

    DecisionOutcome(int severity) {
        this.severity = severity;
    }

    public int severity() {
        return severity;
    }

    /** 取两者中更严重的结论；任一为 null 返回另一个 */
    public static DecisionOutcome worse(DecisionOutcome a, DecisionOutcome b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.severity >= b.severity ? a : b;
    }

    public boolean isWorseThan(DecisionOutcome other) {
        return other == null || this.severity > other.severity;
    }
}
