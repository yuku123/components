package com.zifang.util.core.lang.converter.converters;

import com.zifang.util.core.lang.converter.IConverter;
import com.zifang.util.core.lang.exception.ConversionException;

import java.math.BigDecimal;

public class BigDecimalConverter implements IConverter<BigDecimal, Object> {

    @Override
    public Object to(BigDecimal bigDecimal, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString().trim());
        } catch (NumberFormatException nfex) {
            throw new ConversionException(value, nfex);
        }
    }
}
