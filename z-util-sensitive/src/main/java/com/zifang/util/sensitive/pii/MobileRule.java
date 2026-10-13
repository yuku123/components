package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveType;

/**
 * 中国大陆手机号。号段第二位限定 3-9，前后做数字边界，避免从长数字串里截一段出来。
 */
public class MobileRule extends PiiRule {

    private static final String REGEX = "(?<!\\d)1[3-9]\\d{9}(?!\\d)";

    public MobileRule() {
        this("mobile");
    }

    public MobileRule(String tag) {
        super(REGEX, tag);
    }

    @Override
    public SensitiveType type() {
        return SensitiveType.MOBILE;
    }

    @Override
    protected boolean accept(String candidate) {
        return candidate.length() == 11 && candidate.charAt(0) == '1';
    }
}
