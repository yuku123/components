package com.zifang.util.strategy;

/**
 * 决策指标出口。引擎每判一次调一次 {@link #record}，具体怎么落到看板由应用层实现。
 * <p>
 * 内核只定义"要记哪些量"，不做聚合报表、不做定时窗口统计 ——
 * 那需要存储和时间窗，是应用层的事。
 */
public interface DecisionMetrics {

    void record(DecisionResult result);

    /** 进程内计数实现。够单机灰度观察用；要跨机聚合请应用层换成自己的实现 */
    class InMemory implements DecisionMetrics {

        private final java.util.concurrent.atomic.AtomicLong total = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicLong gray = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicLong control = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicLong trial = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicLong shadowDiff = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.Map<String, java.util.concurrent.atomic.AtomicLong> byOutcome =
                new java.util.concurrent.ConcurrentHashMap<>();
        private final java.util.Map<String, java.util.concurrent.atomic.AtomicLong> byRule =
                new java.util.concurrent.ConcurrentHashMap<>();

        @Override
        public void record(DecisionResult result) {
            total.incrementAndGet();
            if (result.hitGray()) {
                gray.incrementAndGet();
            }
            if (result.controlReleased()) {
                control.incrementAndGet();
            }
            if (result.shadow() != null) {
                trial.incrementAndGet();
                if (result.shadowDiffers()) {
                    shadowDiff.incrementAndGet();
                }
            }
            bump(byOutcome, result.decision().outcome().name());
            for (com.zifang.util.strategy.rule.HitRecord hit : result.decision().hits()) {
                bump(byRule, hit.ruleId());
            }
        }

        private static void bump(java.util.Map<String, java.util.concurrent.atomic.AtomicLong> map, String key) {
            map.computeIfAbsent(key, k -> new java.util.concurrent.atomic.AtomicLong()).incrementAndGet();
        }

        public long total() {
            return total.get();
        }

        public long gray() {
            return gray.get();
        }

        public long control() {
            return control.get();
        }

        public long trial() {
            return trial.get();
        }

        public long shadowDiff() {
            return shadowDiff.get();
        }

        /** 试运行与生效结论的差异率；没有试运行时返回 0 */
        public double shadowDiffRate() {
            long t = trial.get();
            return t == 0 ? 0d : (double) shadowDiff.get() / t;
        }

        public long outcomeCount(DecisionOutcome outcome) {
            java.util.concurrent.atomic.AtomicLong c = byOutcome.get(outcome.name());
            return c == null ? 0 : c.get();
        }

        public long ruleHitCount(String ruleId) {
            java.util.concurrent.atomic.AtomicLong c = byRule.get(ruleId);
            return c == null ? 0 : c.get();
        }

        public void reset() {
            total.set(0);
            gray.set(0);
            control.set(0);
            trial.set(0);
            shadowDiff.set(0);
            byOutcome.clear();
            byRule.clear();
        }
    }
}
