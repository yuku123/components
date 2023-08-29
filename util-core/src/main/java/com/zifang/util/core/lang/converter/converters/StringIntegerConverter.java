package com.zifang.util.core.lang.converter.converters;

import com.zifang.util.core.lang.converter.IConverter;

public class StringIntegerConverter implements IConverter<String, Integer> {

    @Override
    public Integer to(String value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.valueOf(value);
        }
    }
}
