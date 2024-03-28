package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Method;

public class Converter {

    private static ConvertCaller defaultCaller = new ConvertCaller();

    public static <T> ConvertCaller<T> caller(Class<?> from, Class<T> target) {

        Class<?> parsedFrom = PrimitiveUtil.isPrimitive(from)? PrimitiveUtil.getPrimitiveWrapper(from): from;
        Class<?> parsedTarget = PrimitiveUtil.isPrimitive(target)? PrimitiveUtil.getPrimitiveWrapper(target): target;

        Pair<Method, Object> pair = ConvertRegister.find(parsedFrom, parsedTarget);

        ConvertCaller<T> convertCaller = new ConvertCaller<>();
        convertCaller.setMethod(pair.getA());
        convertCaller.setCaller(pair.getB());
        convertCaller.setFrom(from);
        convertCaller.setTarget(target);

        return convertCaller;
    }

    public static <T> T to(Object value, Class<T> clazz) {

        Class<?> from = value.getClass();
        Class<T> target = clazz;

        Class<?> parsedFrom = PrimitiveUtil.isPrimitive(from)? PrimitiveUtil.getPrimitiveWrapper(from): from;
        Class<?> parsedTarget = PrimitiveUtil.isPrimitive(target)? PrimitiveUtil.getPrimitiveWrapper(target): target;

        Pair<Method, Object> pair = ConvertRegister.find(parsedFrom, parsedTarget);

        ConvertCaller<T> convertCaller = new ConvertCaller<>();
        convertCaller.setMethod(pair.getA());
        convertCaller.setCaller(pair.getB());
        convertCaller.setFrom(from);
        convertCaller.setTarget(target);

        try {
            return (T) convertCaller.to(value, clazz.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
