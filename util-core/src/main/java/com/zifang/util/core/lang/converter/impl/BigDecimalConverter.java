package com.zifang.util.core.lang.converter.impl;

import com.zifang.util.core.lang.converter.IConverter;
import com.zifang.util.core.lang.converter.exception.ConvertionException;

import java.math.BigDecimal;

public class BigDecimalConverter implements IConverter<BigDecimal> {

    @Override
    public BigDecimal convert(final Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString().trim());
        } catch (NumberFormatException nfex) {
            throw new ConvertionException(value, nfex);
        }
    }
}
