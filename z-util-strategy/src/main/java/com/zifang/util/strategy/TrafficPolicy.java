package com.zifang.util.strategy;

/**
 * 灰度流量策略：按稳定哈希把流量切给 candidate 包。
 * <p>
 * 分流单元必须是<b>稳定标识</b>（通常是用户 ID），不能是请求 ID ——
 * 请求 ID 分流会让同一用户在灰度期反复进出，指标无法归因，
 * 比例从 0 放到 5 再放到 20 时也会有一批人"来回跳"。
 */
public class TrafficPolicy {

    private static final TrafficPolicy NONE = new TrafficPolicy(0, null);

    private final int percent;
    private final String hashKey;

    private TrafficPolicy(int percent, String hashKey) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("traffic percent must be within [0,100]: " + percent);
        }
        this.percent = percent;
        this.hashKey = hashKey;
    }

    /** 不放任何流量给 candidate */
    public static TrafficPolicy none() {
        return NONE;
    }

    public static TrafficPolicy of(int percent, String hashKey) {
        return new TrafficPolicy(percent, hashKey);
    }

    public int percent() {
        return percent;
    }

    public String hashKey() {
        return hashKey;
    }

    /**
     * 命中分片判定。
     *
     * @param splitKey 稳定标识（用户 ID 等）；为 null 时按未命中处理
     */
    public boolean hit(Object splitKey) {
        if (percent == 0) {
            return false;
        }
        if (percent >= 100) {
            return true;
        }
        if (splitKey == null) {
            return false;
        }
        return bucket(splitKey) < percent;
    }

    /** 该标识落在 [0,100) 的哪个桶 */
    public int bucket(Object splitKey) {
        int h = splitKey == null ? 0 : splitKey.hashCode();
        return Math.abs(h % 100);
    }

    @Override
    public String toString() {
        return "TrafficPolicy{" + percent + "%" + (hashKey != null ? " by " + hashKey : "") + '}';
    }
}
