package com.zifang.util.sensitive;

/**
 * 脱敏掩码风格。
 */
public enum MaskStyle {

    /** 全遮，长度不变 */
    FULL,
    /** 保留头尾，中间打码。保留位数按 {@link SensitiveType} 的默认值 */
    HEAD_TAIL,
    /** 全遮但换成 SHA-256 前 8 位，便于按脱敏值做关联分析而不还原原文 */
    HASH
}
