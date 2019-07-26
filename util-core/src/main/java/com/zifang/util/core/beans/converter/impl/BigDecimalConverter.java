package com.zifang.util.core.beans.converter.impl;

import com.zifang.util.core.beans.converter.IConverter;
import com.zifang.util.core.beans.converter.exception.ConvertionException;

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
