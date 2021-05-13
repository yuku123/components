package com.zifang.util.core.lang.converter.unit;

import com.zifang.util.core.lang.converter.Converter;

import java.math.BigDecimal;

/**
 * @author zifang
 */
public class BigDecimalDoubleConverter implements Converter<BigDecimal, Double> {

    @Override
    public Double to(BigDecimal bigDecimal, Double d) {
        if (bigDecimal == null) {
            return d;
        } else {
            return bigDecimal.doubleValue();
        }
    }
}
