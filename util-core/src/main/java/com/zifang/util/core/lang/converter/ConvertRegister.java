package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import com.zifang.util.core.lang.converter.converters.DefaultConverter;
import com.zifang.util.core.lang.reflect.ClassParser;
import com.zifang.util.core.lang.reflect.ClassParserFactory;
import com.zifang.util.core.lang.tuples.Pair;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConvertRegister {

    private static Map<Method, Object> caller = new LinkedHashMap<>();
    private static final Map<Pair<Class<?>, Class<?>>, Method> registeredDefaultConverterMethod = new LinkedHashMap<>();
    private static final Map<Pair<Class<?>, Class<?>>, Method> registeredConverter = new LinkedHashMap<>();

    static {
        DefaultConverter defaultConverter = new DefaultConverter();
        Method[] methods = defaultConverter.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            Pair<Class<?>, Class<?>> pair = new Pair<>(parameters[0].getType(), parameters[1].getType());
            registeredDefaultConverterMethod.put(pair, method);
            caller.put(method, defaultConverter);
        }
    }

    public static Pair<Method, Object> find(Class<?> a, Class<?> b) {
        Method methodCustomer = registeredConverter.get(new Pair<Class<?>, Class<?>>(PrimitiveUtil.getPrimitiveWrapper(a), b));
        if (methodCustomer != null) {
            return new Pair<>(methodCustomer, caller.get(methodCustomer));
        } else {
            Method method = registeredDefaultConverterMethod.get(new Pair<Class<?>, Class<?>>(a, b));
            if (method == null) {
                throw new RuntimeException("没有找到对应的转换器" + a.getName() + "->" + b.getName());
            }
            return new Pair<>(method, caller.get(method));
        }
    }

    public static void registerConverter(Class<? extends IConverter<?, ?>> clazz) {
        try {
            Object instance = clazz.newInstance();
            ClassParser classParser = new ClassParserFactory().getInstance(clazz);
            Type type = classParser.getGenericType(IConverter.class);
            if (type instanceof ParameterizedTypeImpl) {
                ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) type;
                Type[] types = parameterizedType.getActualTypeArguments();
                Pair<Class<?>, Class<?>> pair = new Pair<>((Class<?>) types[0], (Class<?>) types[1]);
                Method method = clazz.getDeclaredMethod("to", (Class<?>) types[0], (Class<?>) types[1]);
                registeredConverter.put(pair, method);
                caller.put(method, instance);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
