package com.zifang.util.strategy;

import com.zifang.util.strategy.rule.MatchSpec;
import com.zifang.util.strategy.rule.Rule;
import com.zifang.util.strategy.rule.RuleEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 决策包：一组规则 + 编排参数 + 版本号 + 运行模式。
 * <p>
 * <b>不可变快照</b> —— 构建后所有字段只读。灰度期间要改规则必须产生新包（version 递增），
 * 不允许就地改：就地改会让"正在跑的灰度"和"你以为在跑的灰度"不是同一份配置，回滚也无从谈起。
 * <p>
 * 引擎侧用 {@code AtomicReference} 持有它，切换 = 换引用 = 原子，回滚 = 换回旧包引用。
 */
public class DecisionPackage {

    private final String name;
    private final String version;
    private final RunMode runMode;
    private final List<Rule> rules;
    private final MatchSpec matchSpec;

    private DecisionPackage(Builder b) {
        this.name = b.name;
        this.version = b.version;
        this.runMode = b.runMode;
        this.matchSpec = b.matchSpec;
        List<Rule> sorted = new ArrayList<>(b.rules);
        sorted.sort(Comparator.comparingInt(Rule::priority));
        this.rules = Collections.unmodifiableList(sorted);
    }

    public static Builder builder(String name, String version) {
        return new Builder(name, version);
    }

    public String name() {
        return name;
    }

    public String version() {
        return version;
    }

    public RunMode runMode() {
        return runMode;
    }

    /** 按 priority 升序 */
    public List<Rule> rules() {
        return rules;
    }

    public MatchSpec matchSpec() {
        return matchSpec;
    }

    public Decision evaluate(StrategyContext context) {
        return RuleEvaluator.evaluate(rules, context, matchSpec);
    }

    @Override
    public String toString() {
        return "DecisionPackage{" + name + "@" + version + ", " + runMode + ", rules=" + rules.size()
                + ", " + matchSpec + '}';
    }

    public static class Builder {

        private final String name;
        private final String version;
        private RunMode runMode = RunMode.DRAFT;
        private MatchSpec matchSpec = MatchSpec.collectAll();
        private final List<Rule> rules = new ArrayList<>();

        private Builder(String name, String version) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("package name must not be blank");
            }
            if (version == null || version.trim().isEmpty()) {
                throw new IllegalArgumentException("package version must not be blank");
            }
            this.name = name.trim();
            this.version = version.trim();
        }

        public Builder runMode(RunMode runMode) {
            this.runMode = runMode == null ? RunMode.DRAFT : runMode;
            return this;
        }

        public Builder matchSpec(MatchSpec matchSpec) {
            this.matchSpec = matchSpec == null ? MatchSpec.collectAll() : matchSpec;
            return this;
        }

        public Builder rule(Rule rule) {
            if (rule != null) {
                this.rules.add(rule);
            }
            return this;
        }

        public Builder rules(List<Rule> rules) {
            if (rules != null) {
                for (Rule r : rules) {
                    if (r != null) {
                        this.rules.add(r);
                    }
                }
            }
            return this;
        }

        public DecisionPackage build() {
            return new DecisionPackage(this);
        }
    }
}
