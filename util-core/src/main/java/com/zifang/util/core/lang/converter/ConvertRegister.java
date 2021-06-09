package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConvertRegister {

    private static final Map<Pair<Class<?>, Class<?>>, Method> registeredDefaultConverterMethod = new LinkedHashMap<>();

    private static final Map<Pair<Class<?>, Class<?>>, Class<? extends Converter<?, ?>>> registeredConverter = new LinkedHashMap<>();

    static {
        Method[] methods = DefaultConverter.class.getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            Pair<Class<?>, Class<?>> pair = new Pair<>(parameters[0].getType(), parameters[1].getType());
            registeredDefaultConverterMethod.put(pair, method);
        }
    }

    public static void registerConverter(Class<? extends Converter<?, ?>> clazz) {
        // Pair<Class<?>,Class<?>> pair = new Pair<>(A,B);
        //Type type = ReflectUtil.getGenericInterfaceType(clazz, Converter.class);
        //Pair<Class<?>,Class<?>> pair = new Pair<>(parameters[0].getType(),parameters[1].getType());
        System.out.println();
    }
}
