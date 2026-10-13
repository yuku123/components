package com.zifang.util.core.lang.converter.fixture;

import com.zifang.util.core.lang.converter.Convert;
import com.zifang.util.core.lang.converter.ConvertContext;

import java.time.Duration;

/**
 * 静态方法级 {@link Convert} 样例："90m" / "2h" 这类简写转 Duration。
 */
public class DurationHolder {

    @Convert(scope = "fixture", from = String.class, to = Duration.class)
    public static Duration parse(String value, ConvertContext context) {
        String text = value.trim();
        char unit = text.charAt(text.length() - 1);
        long amount = Long.parseLong(text.substring(0, text.length() - 1));
        switch (unit) {
            case 'h':
                return Duration.ofHours(amount);
            case 'm':
                return Duration.ofMinutes(amount);
            case 's':
                return Duration.ofSeconds(amount);
            default:
                throw new IllegalArgumentException("未知单位: " + unit);
        }
    }
}