package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Converter {

    private static Map<Pair<Class<?>,Class<?>>, ConvertCaller> convertCallerCache = new HashMap<>();

    public static <T> ConvertCaller<T> caller(Class<?> from, Class<T> target) {

        Class<?> parsedFrom = PrimitiveUtil.getPrimitiveWrapper(from);
        Class<?> parsedTarget = PrimitiveUtil.getPrimitiveWrapper(target);

        Pair<Class<?>,Class<?>> pair = new Pair<>(parsedFrom, parsedTarget);

        if(convertCallerCache.containsKey(pair)){
            return convertCallerCache.get(pair);
        } else {
            Pair<Method, Object> methodObjectPair = ConvertRegister.find(parsedFrom, parsedTarget);

            ConvertCaller<T> convertCaller = new ConvertCaller<>();
            convertCaller.setMethod(methodObjectPair.getA());
            convertCaller.setCaller(methodObjectPair.getB());
            convertCaller.setFrom(from);
            convertCaller.setTarget(target);

            convertCallerCache.put(pair, convertCaller);

            return convertCaller;
        }
    }

    public static <T> T to(Object value, Class<T> clazz) {

        ConvertCaller<T> convertCaller = caller(value.getClass(), clazz);

        try {
            return (T) convertCaller.to(value, clazz.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
