package com.zifang.util.core.util;

import lombok.extern.slf4j.Slf4j;

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
}
