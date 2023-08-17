package com.zifang.util.core;

import com.zifang.util.core.lang.converter.converters.BigDecimalDoubleConverter;
import com.zifang.util.core.lang.converter.ConvertRegister;
import com.zifang.util.core.lang.converter.Converter;
import org.junit.Test;

import java.math.BigDecimal;

public class ConvertRegisterTest {

    @Test
    public void test0() {
        ConvertRegister.registerConverter(BigDecimalDoubleConverter.class);
        assert (Double) Converter.caller(BigDecimal.class, Double.class).to(new BigDecimal("1.0")) == 1L;
    }

    @Test
    public void test1() {
        assert (Long) Converter.caller(Integer.class, Long.class).to(null) == 0L;
        assert (Long) Converter.caller(Integer.class, Long.class).to(null, 3L) == 3L;
        assert (Long) Converter.caller(Integer.class, Long.class).to(2) == 2L;
        assert (Long) Converter.caller(Integer.class, Long.class).to(2, 3L) == 2L;
    }
}
