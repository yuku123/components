package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertPolicy;

/**
 * 输入侧预处理。空串语义统一在门面按 {@link ConvertPolicy.BlankPolicy} 处理，
 * 转换器只负责按策略决定要不要 trim。
 *
 * @author zifang
 */
final class Inputs {

    private Inputs() {
    }

    static String trim(String value, ConvertPolicy policy) {
        return policy.isTrim() ? value.trim() : value;
    }

    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}