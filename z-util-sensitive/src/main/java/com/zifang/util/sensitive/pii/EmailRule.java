package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveType;

/**
 * 邮箱。格式本身即精确，无需额外校验位。
 */
public class EmailRule extends PiiRule {

    private static final String REGEX =
            "(?<![A-Za-z0-9._%+-])[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(?![A-Za-z0-9-])";

    public EmailRule() {
        this("email");
    }

    public EmailRule(String tag) {
        super(REGEX, tag);
    }

    @Override
    public SensitiveType type() {
        return SensitiveType.EMAIL;
    }

    @Override
    protected boolean accept(String candidate) {
        int at = candidate.indexOf('@');
        return at > 0 && at < candidate.length() - 1
                && candidate.indexOf('@', at + 1) < 0;
    }
}
