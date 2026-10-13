package com.zifang.util.strategy;

/**
 * 轨迹中的一步：某条规则/某个策略的求值结论。
 */
public class TraceStep {

    private final String source;
    private final String ruleId;
    private final boolean matched;
    private final DecisionOutcome outcome;
    private final String message;
    private final long durationNanos;
    private final String error;

    private TraceStep(String source, String ruleId, boolean matched, DecisionOutcome outcome,
                      String message, long durationNanos, String error) {
        this.source = source;
        this.ruleId = ruleId;
        this.matched = matched;
        this.outcome = outcome;
        this.message = message;
        this.durationNanos = durationNanos;
        this.error = error;
    }

    public static TraceStep of(String source, String ruleId, boolean matched,
                               DecisionOutcome outcome, String message, long durationNanos) {
        return new TraceStep(source, ruleId, matched, outcome, message, durationNanos, null);
    }

    public static TraceStep error(String source, String ruleId, String error, long durationNanos) {
        return new TraceStep(source, ruleId, false, null, null, durationNanos, error);
    }

    public String source() {
        return source;
    }

    public String ruleId() {
        return ruleId;
    }

    public boolean matched() {
        return matched;
    }

    public DecisionOutcome outcome() {
        return outcome;
    }

    public String message() {
        return message;
    }

    public long durationNanos() {
        return durationNanos;
    }

    public String error() {
        return error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(source);
        if (ruleId != null) {
            sb.append('#').append(ruleId);
        }
        sb.append(' ').append(matched ? "HIT" : "miss");
        if (outcome != null) {
            sb.append(" -> ").append(outcome);
        }
        if (message != null) {
            sb.append(" (").append(message).append(')');
        }
        if (error != null) {
            sb.append(" [ERROR: ").append(error).append(']');
        }
        sb.append(' ').append(durationNanos).append("ns");
        return sb.toString();
    }
}
