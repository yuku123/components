package com.zifang.util.core;

import com.zifang.util.core.lang.converter.ConvertRegister;
import com.zifang.util.core.lang.converter.Converter;
import com.zifang.util.core.lang.converter.converters.BigDecimalDoubleConverter;
import com.zifang.util.core.lang.converter.converters.StringIntegerConverter;
import org.junit.Test;

import java.math.BigDecimal;

public class ConvertRegisterTest {


    @Test
    public void test001() {
        assert Converter.caller(String.class, Integer.class).to("12").equals(12);
        assert Converter.caller(Integer.class, Long.class).to(null).equals(0L);
        assert Converter.caller(Integer.class, Long.class).to(null, 3L).equals(3L);
        assert Converter.caller(Integer.class, Long.class).to(2).equals(2L);
        assert Converter.caller(Integer.class, Long.class).to(2, 3L).equals(2L);
    }


    @Test
    public void test002(){
        assert Converter.to("12", Integer.class).equals(12);
        assert Converter.caller(BigDecimal.class, Double.class).to(new BigDecimal("1.0")).equals(1.0);
        assert Converter.caller(Integer.class, Long.class).to(null).equals(0L);
        assert Converter.caller(Integer.class, Long.class).to(null, 3L).equals(3L);
        assert Converter.caller(Integer.class, Long.class).to(2).equals(2L);
        assert Converter.caller(Integer.class, Long.class).to(2, 3L).equals(2L);
    }

    @Test
    public void test003(){
        ConvertRegister.registerConverter(BigDecimalDoubleConverter.class);
        ConvertRegister.registerConverter(StringIntegerConverter.class);

        assert Converter.caller(BigDecimal.class, Double.class).to(new BigDecimal("1.0")).equals(1.0);
    }

}
