package com.zifang.util.core.converter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 转换器的注册
 * */
public class DefaultConverterRegister {

    private static Map<String,Object> stringObjectMap = new LinkedHashMap<>();

    public static <F,T> void register(Converter<F,T> converter){

    }
}
