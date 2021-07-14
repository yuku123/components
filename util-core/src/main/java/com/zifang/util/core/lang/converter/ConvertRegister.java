package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConvertRegister {

    private static Map<Method,Object> caller = new LinkedHashMap<>();
    private static final Map<Pair<Class<?>, Class<?>>, Method> registeredDefaultConverterMethod = new LinkedHashMap<>();

    private static final Map<Pair<Class<?>, Class<?>>, Class<? extends IConverter<?, ?>>> registeredConverter = new LinkedHashMap<>();

    static {
        DefaultConverter defaultConverter = new DefaultConverter();
        Method[] methods = defaultConverter.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            Pair<Class<?>, Class<?>> pair = new Pair<>(parameters[0].getType(), parameters[1].getType());
            registeredDefaultConverterMethod.put(pair, method);
            caller.put(method,defaultConverter);
        }
    }

    public static Pair<Method,Object> find(Class<?> a, Class<?> b){
        Method method = registeredDefaultConverterMethod.get(new Pair<Class<?>, Class<?>>(a,b));
        if (method == null){
            throw new RuntimeException("没有找到对应的转换器"+a.getName()+"->"+b.getName());
        }
        return new Pair<>(method,caller.get(method));
    }

    public static void registerConverter(Class<? extends IConverter<?, ?>> clazz) {
        System.out.println();
    }
}
