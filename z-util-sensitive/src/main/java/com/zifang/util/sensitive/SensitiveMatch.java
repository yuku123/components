package com.zifang.util.sensitive;

/**
 * 一处命中。必须带区间：只报"是否命中"没法脱敏，也没法定位高亮。
 */
public class SensitiveMatch {

    private final SensitiveType type;
    private final String value;
    private final int start;
    private final int end;
    private final String tag;

    public SensitiveMatch(SensitiveType type, String value, int start, int end, String tag) {
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
        this.tag = tag;
    }

    public SensitiveType type() {
        return type;
    }

    public String value() {
        return value;
    }

    /** 起始下标，含 */
    public int start() {
        return start;
    }

    /** 结束下标，不含 */
    public int end() {
        return end;
    }

    public int length() {
        return end - start;
    }

    public String tag() {
        return tag;
    }

    @Override
    public String toString() {
        return type + "[" + start + "," + end + ")=" + value + (tag != null ? " tag=" + tag : "");
    }
}
