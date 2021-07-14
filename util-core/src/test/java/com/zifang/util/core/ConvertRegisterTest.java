package com.zifang.util.core;

import com.zifang.util.core.lang.converter.BigDecimalDoubleConverter;
import com.zifang.util.core.lang.converter.ConvertCaller;
import com.zifang.util.core.lang.converter.ConvertRegister;
import com.zifang.util.core.lang.converter.Converter;
import org.junit.Test;

public class ConvertRegisterTest {

    @Test
    public void test0() {
        ConvertRegister.registerConverter(BigDecimalDoubleConverter.class);
    }

    @Test
    public void test1(){
        Object a = 1;
        Object b = 2L;

        Class<?> aaa = a.getClass();
        Class<?> bbb = b.getClass();

        ConvertCaller convertCaller = Converter.caller(aaa,bbb);
        Object transform = convertCaller.to(1);
        System.out.println("sss");
    }
}
