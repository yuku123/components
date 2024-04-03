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
import java.util.Map;

public class Converters {

    private static final Map<Pair<Class<?>,Class<?>>,IConverter<?,?>> converterCache = new HashMap<>();

    static {
        DefaultConverter<Object,Object> defaultConverter = new DefaultConverter<>();
        Method[] methods = DefaultConverter.class.getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            Pair<Class<?>, Class<?>> pair = new Pair<>(parameters[0].getType(), parameters[1].getType());

            ConvertCaller<?, ?> iConverter = new ConvertCaller<>();
            iConverter.setFrom(parameters[0].getType());
            iConverter.setTarget(parameters[1].getType());
            iConverter.setCaller(defaultConverter);
            iConverter.setMethod(method);

            converterCache.put(pair, iConverter);
        }
    }


    public static <F,T> IConverter<F,T> findConverter(Class<F> a, Class<T> b){
        Pair<Class<?>, Class<?>> pair = new Pair<>(a, b);

        // 直接寻找
        if(converterCache.containsKey(pair)){
            return (IConverter<F,T>)converterCache.get(pair);
        }

        // 类型完全匹配
        for(Map.Entry<Pair<Class<?>,Class<?>>,IConverter<?,?>> entry : converterCache.entrySet()){
            if(entry.getKey().getA() == a && entry.getKey().getB() == b){
                return (IConverter<F,T>)entry.getValue();
            }
        }

        // 继承寻找
        for(Map.Entry<Pair<Class<?>,Class<?>>,IConverter<?,?>> entry : converterCache.entrySet()){
            if(entry.getKey().getA().isAssignableFrom(a) && entry.getKey().getB().isAssignableFrom(b)){
                return (IConverter<F,T>)entry.getValue();
            }
        }

        // todo 利用转换图能力进行转换
        throw new RuntimeException("没有找到对应的转换器" + a.getName() + "->" + b.getName());
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
//                registeredConverter.put(pair, method);
//                caller.put(method, converter);
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

        if(converterCache.containsKey(pair)){
            return (ConvertCaller<F,T>) converterCache.get(pair);
        } else {
            // 有可能参数是父类的
            ConvertCaller<F,T> convertCaller = (ConvertCaller<F,T>) findConverter(parsedFrom,parsedTarget);
            ConvertCaller<F,T> copy = convertCaller.copy();
            copy.setFrom(parsedFrom);
            copy.setTarget(parsedTarget);

            converterCache.put(pair, copy);

            return copy;
        }
    }

    public static <F,T> T to(Object value, Class<T> clazz) {
        return caller((Class<F>) value.getClass(), clazz).to((F)value);
    }
}
