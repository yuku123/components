package com.zifang.util.core.lang.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Slf4j
public class ClassLoaderUtil {

    /**
     * 获取类加载器
     */
    public static ClassLoader overrideClassLoader;

    public static ClassLoader getContextClassLoader() {
        return overrideClassLoader != null ? overrideClassLoader : Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载指定的类
     *
     * @param className 需要加载的类
     * @return 加载后的类
     */
    public static Class<?> loadClass(String className) {
        Class<?> theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error:" + e.getMessage());
            e.printStackTrace();
        }
        return theClass;
    }

    public static List<Class> getLoaderClass(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        Class cla = classLoader.getClass();
        while (cla != ClassLoader.class) {
            cla = cla.getSuperclass();
        }
        Field field = cla.getDeclaredField("classes");
        field.setAccessible(true);
        Vector v = (Vector) field.get(classLoader);
        List<Class> result = new ArrayList<>();
        for (Object o : v) {
            result.add((Class) o);
        }
        return result;
    }
}
