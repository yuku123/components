package com.zifang.util.sensitive;

/**
 * 敏感内容类型。PII 类型带默认保留位数，供脱敏时按类型给出可读又不可还原的掩码。
 */
public enum SensitiveType {

    /** 敏感词 / 违禁词 */
    WORD(2, 2),
    /** 身份证号（18 位，GB11643 校验位） */
    ID_CARD(6, 4),
    /** 银行卡号（Luhn） */
    BANK_CARD(4, 4),
    /** 手机号 */
    MOBILE(3, 4),
    /** 邮箱 */
    EMAIL(1, 0),
    /** IPv4 */
    IP(2, 0),
    /** URL */
    URL(0, 0),
    /** 业务自定义类型 */
    CUSTOM(2, 2);

    private final int defaultHead;
    private final int defaultTail;

    SensitiveType(int defaultHead, int defaultTail) {
        this.defaultHead = defaultHead;
        this.defaultTail = defaultTail;
    }

    public int defaultHead() {
        return defaultHead;
    }

    public int defaultTail() {
        return defaultTail;
    }
}
