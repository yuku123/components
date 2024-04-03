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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Converters {

    private static final Map<Method, Object> caller = new LinkedHashMap<>();

    // 内部默认存在的转换器
    private static final Map<Pair<Class<?>, Class<?>>, Method> registeredDefaultConverterMethod = new LinkedHashMap<>();
    // 外部注入的转换器
    private static final Map<Pair<Class<?>, Class<?>>, Method> registeredConverter = new LinkedHashMap<>();

    private static final Map<Pair<Class<?>,Class<?>>, ConvertCaller<?,?>> convertCallerCache = new HashMap<>();

    static {
        DefaultConverter<Object,Object> defaultConverter = new DefaultConverter<>();
//        registerConverter(defaultConverter);
        Method[] methods = DefaultConverter.class.getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            Pair<Class<?>, Class<?>> pair = new Pair<>(parameters[0].getType(), parameters[1].getType());
            registeredDefaultConverterMethod.put(pair, method);
            caller.put(method, defaultConverter);
        }
    }


    public static <F,T> IConverter<F,T> findConverter(Class<F> a, Class<T> b){
        return null;
    }
    public static Pair<Method, Object> find(Class<?> a, Class<?> b) {

        // 优先外部注入，需要精确匹配
        Method methodCustomer = match(registeredConverter, a, b, true);
        if (methodCustomer != null) {
            return new Pair<>(methodCustomer, caller.get(methodCustomer));
        } else {
            Method method = match(registeredDefaultConverterMethod, a, b, false);
            if (method == null) {
                throw new RuntimeException("没有找到对应的转换器" + a.getName() + "->" + b.getName());
            }
            return new Pair<>(method, caller.get(method));
        }
    }

    private static Method match(Map<Pair<Class<?>, Class<?>>, Method> registeredConverter, Class<?> a, Class<?> b, boolean extact) {

        if(extact){
            for(Map.Entry<Pair<Class<?>, Class<?>>, Method> entry : registeredConverter.entrySet()){
                Pair<Class<?>, Class<?>> pair = entry.getKey();
                if(pair.getA() == a && pair.getB() == b){
                    return entry.getValue();
                }
            }
        } else {
            for(Map.Entry<Pair<Class<?>, Class<?>>, Method> entry : registeredConverter.entrySet()){
                Pair<Class<?>, Class<?>> pair = entry.getKey();
                if(pair.getA().isAssignableFrom(a) && pair.getB().isAssignableFrom(b)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public static  <F,T> void registerConverter(Class<? extends IConverter<F,T>> clazz) {
        try {
            Object instance = clazz.newInstance();
            registerConverter((IConverter<F,T>)instance);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <F,T> void registerConverter(IConverter<F, T> converter){
        try {
            ClassParser classParser = new ClassParserFactory().getInstance(converter.getClass());
            Type type = classParser.getGenericType(IConverter.class);
            if (type instanceof ParameterizedTypeImpl) {
                ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) type;
                Type[] types = parameterizedType.getActualTypeArguments();
                Pair<Class<?>, Class<?>> pair = new Pair<>((Class<?>) types[0], (Class<?>) types[1]);
                Method method = converter.getClass().getDeclaredMethod("to", (Class<?>) types[0], (Class<?>) types[1]);
                registeredConverter.put(pair, method);
                caller.put(method, converter);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static <F,T> IConverter<F,T> caller(Class<F> from, Class<T> target) {

        // 获取原始类型
        Class<?> parsedFrom = PrimitiveUtil.getPrimitiveWrapper(from);
        Class<?> parsedTarget = PrimitiveUtil.getPrimitiveWrapper(target);

        Pair<Class<?>,Class<?>> pair = new Pair<>(parsedFrom, parsedTarget);

        if(convertCallerCache.containsKey(pair)){
            return (ConvertCaller<F,T>) convertCallerCache.get(pair);
        } else {

            Pair<Method, Object> methodObjectPair = find(parsedFrom, parsedTarget);

            ConvertCaller<F,T> convertCaller = new ConvertCaller<>();
            convertCaller.setMethod(methodObjectPair.getA());
            convertCaller.setCaller(methodObjectPair.getB());
            convertCaller.setFrom(from);
            convertCaller.setTarget(target);

            convertCallerCache.put(pair, convertCaller);

            return convertCaller;
        }
    }

    public static <F,T> T to(Object value, Class<T> clazz) {
        return caller((Class<F>) value.getClass(), clazz).to((F)value);
    }
}
