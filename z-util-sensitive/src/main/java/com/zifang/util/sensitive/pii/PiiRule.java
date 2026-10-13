package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveMatch;
import com.zifang.util.sensitive.SensitiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PII 规则：正则负责<b>召回</b>，校验器负责<b>精确率</b>。
 * <p>
 * 这层校验不是锦上添花。{@code 6222021234567890} 能匹配银行卡正则，但它不是卡号 ——
 * 校验位会把它挡掉。身份证同理：格式对而校验位错的号码在正常文本里遍地都是
 * （订单号、日志时间戳），不过校验就会大量误伤。
 */
public abstract class PiiRule {

    private final Pattern pattern;
    private final String tag;

    protected PiiRule(String regex, String tag) {
        this.pattern = Pattern.compile(regex);
        this.tag = tag;
    }

    public abstract SensitiveType type();

    /** 校验位 / 业务规则校验，通过才认这次命中 */
    protected abstract boolean accept(String candidate);

    public List<SensitiveMatch> detect(String text) {
        List<SensitiveMatch> matches = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return matches;
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String candidate = matcher.group();
            if (accept(candidate)) {
                matches.add(new SensitiveMatch(type(), candidate, matcher.start(), matcher.end(), tag));
            }
        }
        return matches;
    }

    public boolean detects(String text) {
        return !detect(text).isEmpty();
    }

    protected String tag() {
        return tag;
    }
}
