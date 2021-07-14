package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Method;

public class Converter {
    public static ConvertCaller caller(Class<?> aaa, Class<?> bbb) {
        ConvertCaller convertCaller = new ConvertCaller();
        Pair<Method,Object> pair = ConvertRegister.find(aaa,bbb);
        convertCaller.setMethod(pair.getA());
        convertCaller.setCaller(pair.getB());
        return convertCaller;
    }
}
