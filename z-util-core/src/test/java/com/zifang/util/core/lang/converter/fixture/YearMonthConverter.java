package com.zifang.util.core.lang.converter.fixture;

import com.zifang.util.core.lang.converter.Convert;
import com.zifang.util.core.lang.converter.ConvertContext;
import com.zifang.util.core.lang.converter.Converter;

import java.time.YearMonth;

/**
 * 类级 {@link Convert} 样例：字符串 "2024-01" 转 YearMonth。
 */
@Convert(scope = "fixture", from = String.class, to = YearMonth.class, priority = 50)
public class YearMonthConverter implements Converter<String, YearMonth> {

    @Override
    public YearMonth convert(String value, ConvertContext context) {
        return YearMonth.parse(value);
    }
}