package com.zifang.util.strategy.rule;

import com.zifang.util.expr.sql.expression.ExpressionEvaluator;
import com.zifang.util.strategy.Decision;
import com.zifang.util.strategy.DecisionOutcome;
import com.zifang.util.strategy.StrategyContext;
import com.zifang.util.strategy.TraceStep;

import java.util.List;
import java.util.Map;

/**
 * 规则求值器。按 priority 升序跑规则，按 {@link MatchSpec} 决定何时停，
 * 产出带完整 trace 的 {@link Decision}。
 */
public final class RuleEvaluator {

    private static final ExpressionEvaluator EVALUATOR = new ExpressionEvaluator();

    private RuleEvaluator() {
    }

    public static Decision evaluate(List<Rule> rules, StrategyContext context, MatchSpec spec) {
        Map<String, Object> row = context.asRow();
        Decision decision = new Decision();
        if (rules == null || rules.isEmpty()) {
            return decision;
        }
        for (Rule rule : rules) {
            if (!rule.enabled()) {
                continue;
            }
            long start = System.nanoTime();
            boolean hit;
            String error = null;
            try {
                hit = toBool(EVALUATOR.evaluate(rule.compiled(), row));
            } catch (Exception e) {
                error = e.toString();
                hit = spec.onError() == MatchSpec.OnError.FAIL_OPEN;
            }
            long elapsed = System.nanoTime() - start;

            if (error != null) {
                decision.trace().add(TraceStep.error("rule", rule.id(), error, elapsed));
            }
            if (!hit) {
                decision.trace().add(TraceStep.of("rule", rule.id(), false, null, null, elapsed));
                continue;
            }

            Effect effect = rule.effect();
            decision.addHit(new HitRecord(rule.id(), rule.name(), effect));
            decision.mergeOutcome(effect.outcome());
            if (effect.code() != null) {
                decision.code(effect.code());
            }
            decision.addScore(effect.scoreDelta());
            decision.addTag(effect.tag());
            decision.trace().add(TraceStep.of("rule", rule.id(), true, effect.outcome(), effect.code(), elapsed));

            if (spec.mode() == MatchMode.FIRST_MATCH) {
                break;
            }
            if (spec.mode() == MatchMode.THRESHOLD && decision.score() >= spec.threshold()) {
                break;
            }
        }
        if (decision.outcome() == DecisionOutcome.PASS && decision.score() > 0) {
            decision.mergeOutcome(DecisionOutcome.SCORE);
        }
        return decision;
    }

    static boolean toBool(Object v) {
        if (v == null) {
            return false;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof Number) {
            return ((Number) v).doubleValue() != 0;
        }
        if (v instanceof java.util.Collection) {
            return !((java.util.Collection<?>) v).isEmpty();
        }
        String s = String.valueOf(v).trim();
        return "TRUE".equalsIgnoreCase(s) || "1".equals(s);
    }
}
