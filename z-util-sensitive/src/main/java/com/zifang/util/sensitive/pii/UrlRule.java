package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveType;

/**
 * URL。含 http/https 前缀与裸 www. 两种常见写法。
 */
public class UrlRule extends PiiRule {

    private static final String REGEX = "(?i)\\b(?:https?://|www\\.)[^\\s\"'<>\\\\]+";

    public UrlRule() {
        this("url");
    }

    public UrlRule(String tag) {
        super(REGEX, tag);
    }

    @Override
    public SensitiveType type() {
        return SensitiveType.URL;
    }

    @Override
    protected boolean accept(String candidate) {
        return candidate.contains(".");
    }
}
