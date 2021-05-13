package com.zifang.util.core.lang.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

    public static Type getGenericInterfaceType(Class<?> base, Class<?> interfaceClazz) {

        Type genericType = null;

        // 递归接口
        Type[] types = base.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof Class) {
                genericType = getGenericInterfaceType((Class<?>) type, interfaceClazz);
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> parameterizedTypeRawType = (Class<?>) parameterizedType.getRawType();
                if (parameterizedTypeRawType == interfaceClazz) {
                    genericType = type;
                } else {
                    getGenericInterfaceType(parameterizedTypeRawType, interfaceClazz);
                }
            }
        }

        // 递归父类
        if (genericType == null) {
            Class<?> clazz = base.getSuperclass();
            if (clazz != null && clazz != Object.class) {
                genericType = getGenericInterfaceType(base.getSuperclass(), interfaceClazz);
            }
        }
        return genericType;
    }
}
