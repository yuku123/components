package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveType;

/**
 * IPv4。四段各 0-255 —— 正则能召回到 999.999.999.999，靠校验器挡掉。
 * <p>
 * 不含 IPv6：IPv6 的正则要处理八组、压缩记法、IPv4 尾部映射四种形态，
 * 写出来的正则既慢又容易漏，在日志这种以 IPv4 为主的场景里收益很低。
 */
public class IpRule extends PiiRule {

    private static final String REGEX =
            "(?<![0-9.])((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(?![0-9.])";

    public IpRule() {
        this("ip");
    }

    public IpRule(String tag) {
        super(REGEX, tag);
    }

    @Override
    public SensitiveType type() {
        return SensitiveType.IP;
    }

    @Override
    protected boolean accept(String candidate) {
        String[] parts = candidate.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            int value = Integer.parseInt(part);
            if (value < 0 || value > 255) {
                return false;
            }
        }
        return true;
    }
}
