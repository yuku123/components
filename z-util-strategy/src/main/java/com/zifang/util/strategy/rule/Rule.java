package com.zifang.util.strategy.rule;

import com.zifang.util.expr.sql.expression.ExpressionEvaluator;

/**
 * 规则：条件 + 效果 + 优先级。构建期就把条件编译成 DSL AST，热路径只求值不重新解析。
 */
public class Rule {

    private final String id;
    private final String name;
    private final int priority;
    private final boolean enabled;
    private final Condition condition;
    private final Effect effect;
    private final ExpressionEvaluator.Compiled compiled;

    private Rule(Builder b, ExpressionEvaluator.Compiled compiled) {
        this.id = b.id;
        this.name = b.name;
        this.priority = b.priority;
        this.enabled = b.enabled;
        this.condition = b.condition;
        this.effect = b.effect;
        this.compiled = compiled;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    /** 升序求值，值小的先跑 */
    public int priority() {
        return priority;
    }

    public boolean enabled() {
        return enabled;
    }

    public Condition condition() {
        return condition;
    }

    public Effect effect() {
        return effect;
    }

    ExpressionEvaluator.Compiled compiled() {
        return compiled;
    }

    @Override
    public String toString() {
        return "Rule{" + id + " p=" + priority + (enabled ? "" : " disabled") + " " + condition + " -> " + effect + '}';
    }

    public static class Builder {

        private final String id;
        private String name;
        private int priority = 100;
        private boolean enabled = true;
        private Condition condition;
        private Effect effect = Effect.none();

        private Builder(String id) {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("rule id must not be blank");
            }
            this.id = id.trim();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder condition(Condition condition) {
            this.condition = condition;
            return this;
        }

        public Builder effect(Effect effect) {
            this.effect = effect == null ? Effect.none() : effect;
            return this;
        }

        public Rule build() {
            Condition c = condition == null ? Condition.alwaysTrue() : condition;
            // 构建期编译：条件写错要在装配时就炸，而不是等线上第一条流量
            ExpressionEvaluator.Compiled compiled = new ExpressionEvaluator().compile(c.toDsl());
            return new Rule(this, compiled);
        }
    }
}
