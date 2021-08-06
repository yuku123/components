package com.zifang.util.core.lang;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理基本类型与包装类型的工具类
 *
 * @author zifang
 */
@Slf4j
public class PrimitiveUtil {

    public static final Map<Class<?>,Object> primitiveTypeDefaults = new LinkedHashMap<Class<?>,Object>(){
        {
            put(Byte.class,(byte)0);
            put(Short.class,(short)0);
            put(Integer.class,0);
            put(Character.class,'0');
            put(Long.class,0L);
            put(Float.class,0F);
            put(Double.class,0D);
            put(Boolean.class,false);
        }
    };

    /**
     * 原始类型的集合
     */
    private static final List<Class<?>> primitiveTypeList = new ArrayList<Class<?>>() {
        {
            add(byte.class);
            add(char.class);
            add(short.class);
            add(int.class);
            add(long.class);
            add(float.class);
            add(double.class);
            add(boolean.class);
        }
    };

    /**
     * 封装类型的集合
     */
    private static final List<Class<?>> primitiveWrapperTypeList = new ArrayList<Class<?>>() {
        {
            add(Byte.class);
            add(Character.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
        }
    };

    public static Object defaultValue(Class<?> clazz){
        return primitiveTypeDefaults.get(clazz);
    }

    /**
     * 判断是否是基本类型
     */
    public static <T> boolean isPrimitive(Class<T> clazz) {
        return primitiveTypeList.contains(clazz);
    }

    /**
     * 判断是否是基本类型的包装类
     */
    public static <T> boolean isPrimitiveWrapper(Class<T> clazz) {
        return primitiveWrapperTypeList.contains(clazz);
    }

    /**
     * 判断是否为普通类型 -> 既不是基本类型也不是基本类型的包装
     */
    public static <T> boolean isGeneralType(Class<T> clazz) {
        return (!isPrimitive(clazz)) && (!isPrimitiveWrapper(clazz));
    }

    /**
     * 得到包装类对应的基本类型
     */
    public static Class<?> getPrimitive(Class<?> clazz) {
        if (!isPrimitiveWrapper(clazz)) {
            String error = "the input class" + clazz.getName() + " is not wrapperType";
            log.error(error);
            throw new RuntimeException(error);
        }
        return primitiveTypeList.get(primitiveWrapperTypeList.indexOf(clazz));
    }

    /**
     * 得到基本类型对应的包装类型
     */
    public static Class<?> getPrimitiveWrapper(Class<?> clazz) {
        if (!isPrimitive(clazz)) {
            return clazz;
        } else {
            return primitiveWrapperTypeList.get(primitiveTypeList.indexOf(clazz));
        }
    }
}
