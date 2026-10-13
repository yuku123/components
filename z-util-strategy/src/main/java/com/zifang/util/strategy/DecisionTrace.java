package com.zifang.util.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 决策轨迹。风控要求可解释，所以 trace 是一等返回值而不是 debug 附加物。
 */
public class DecisionTrace {

    private static final DecisionTrace EMPTY = new DecisionTrace(Collections.<TraceStep>emptyList());

    private final List<TraceStep> steps;

    public DecisionTrace() {
        this.steps = new ArrayList<>();
    }

    private DecisionTrace(List<TraceStep> steps) {
        this.steps = steps;
    }

    public static DecisionTrace empty() {
        return EMPTY;
    }

    public DecisionTrace add(TraceStep step) {
        steps.add(step);
        return this;
    }

    /** 把另一个 trace 的步骤平铺进来，用于多策略/多规则汇总 */
    public DecisionTrace merge(DecisionTrace other) {
        if (other != null && other != EMPTY) {
            steps.addAll(other.steps);
        }
        return this;
    }

    public List<TraceStep> steps() {
        return Collections.unmodifiableList(steps);
    }

    public boolean isEmpty() {
        return steps.isEmpty();
    }

    public int size() {
        return steps.size();
    }

    public long totalNanos() {
        long sum = 0;
        for (TraceStep step : steps) {
            sum += step.durationNanos();
        }
        return sum;
    }
}
