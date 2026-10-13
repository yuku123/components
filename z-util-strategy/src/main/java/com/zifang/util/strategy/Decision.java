package com.zifang.util.strategy;

import com.zifang.util.strategy.rule.HitRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 单个决策包的求值结论。
 */
public class Decision {

    private DecisionOutcome outcome = DecisionOutcome.PASS;
    private String code;
    private double score;
    private final Set<String> tags = new LinkedHashSet<>();
    private final List<HitRecord> hits = new ArrayList<>();
    private DecisionTrace trace = new DecisionTrace();

    public DecisionOutcome outcome() {
        return outcome;
    }

    public String code() {
        return code;
    }

    public double score() {
        return score;
    }

    public Set<String> tags() {
        return Collections.unmodifiableSet(tags);
    }

    public List<HitRecord> hits() {
        return Collections.unmodifiableList(hits);
    }

    public DecisionTrace trace() {
        return trace;
    }

    public boolean isPass() {
        return outcome == DecisionOutcome.PASS;
    }

    public void addHit(HitRecord hit) {
        hits.add(hit);
    }

    public void mergeOutcome(DecisionOutcome other) {
        if (other != null && other.isWorseThan(outcome)) {
            outcome = other;
        }
    }

    public void code(String code) {
        this.code = code;
    }

    public void addScore(double delta) {
        this.score += delta;
    }

    public void addTag(String tag) {
        if (tag != null) {
            tags.add(tag);
        }
    }

    public void trace(DecisionTrace trace) {
        this.trace = trace == null ? new DecisionTrace() : trace;
    }

    @Override
    public String toString() {
        return "Decision{" + outcome + (code != null ? "/" + code : "")
                + ", score=" + score + ", tags=" + tags + ", hits=" + hits.size() + '}';
    }
}
