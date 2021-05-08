package com.zifang.util.core;

import com.zifang.util.core.lang.converter.ConvertRegister;
import com.zifang.util.core.lang.converter.unit.BigDecimalDoubleConverter;
import org.junit.Test;

public class ConvertRegisterTest {

    @Test
    public void test0(){
        ConvertRegister.registerConverter(BigDecimalDoubleConverter.class);
    }
}
