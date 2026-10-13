package com.zifang.util.strategy;

/**
 * 对照组策略：从"命中拦截"的用户里随机放行一小撮。
 * <p>
 * 为什么需要：全量拦会误杀正常用户，而误杀率无法从日志反推 ——
 * 被放行的人本来就不会进拦截日志。随机放行 1% 命中用户、事后人工复核这批人的实际欺诈率，
 * 就得到了误杀率的估计值。
 * <p>
 * 代价是刻意放过一小撮欺诈，所以百分比要小，且要按稳定哈希而不是随机数，
 * 否则同一用户反复进出对照组，样本不可比。
 */
public class ControlPolicy {

    private static final ControlPolicy DISABLED = new ControlPolicy(0, null);

    private final int percent;
    private final String hashKey;

    private ControlPolicy(int percent, String hashKey) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("control percent must be within [0,100]: " + percent);
        }
        this.percent = percent;
        this.hashKey = hashKey;
    }

    public static ControlPolicy disabled() {
        return DISABLED;
    }

    public static ControlPolicy of(int percent, String hashKey) {
        return new ControlPolicy(percent, hashKey);
    }

    public int percent() {
        return percent;
    }

    public String hashKey() {
        return hashKey;
    }

    /**
     * 判定某标识是否落入对照组。
     *
     * @param splitKey 稳定标识；为 null 时不入对照组，避免同一个 null 被反复计入
     */
    public boolean inControl(Object splitKey) {
        if (percent == 0) {
            return false;
        }
        if (splitKey == null) {
            return false;
        }
        if (percent >= 100) {
            return true;
        }
        int h = splitKey.hashCode();
        return Math.abs(h % 100) < percent;
    }

    @Override
    public String toString() {
        return "ControlPolicy{" + percent + "%" + (hashKey != null ? " by " + hashKey : "") + '}';
    }
}
