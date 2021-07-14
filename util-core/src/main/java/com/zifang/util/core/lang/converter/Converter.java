package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Method;

public class Converter {
    public static ConvertCaller caller(Class<?> from, Class<?> target) {
        ConvertCaller convertCaller = new ConvertCaller();
        Pair<Method,Object> pair = ConvertRegister.find(from,target);
        convertCaller.setMethod(pair.getA());
        convertCaller.setCaller(pair.getB());
        convertCaller.setFrom(from);
        convertCaller.setTarget(target);
        return convertCaller;
    }
}
