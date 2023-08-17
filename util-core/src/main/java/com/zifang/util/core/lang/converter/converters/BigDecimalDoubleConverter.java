package com.zifang.util.core.lang.converter.converters;

import com.zifang.util.core.lang.converter.IConverter;

import java.math.BigDecimal;

/**
 * @author zifang
 */
public class BigDecimalDoubleConverter implements IConverter<BigDecimal, Double> {

    @Override
    public Double to(BigDecimal bigDecimal, Double defaultValue) {
        if (bigDecimal == null) {
            return defaultValue;
        } else {
            return bigDecimal.doubleValue();
        }
    }
}
