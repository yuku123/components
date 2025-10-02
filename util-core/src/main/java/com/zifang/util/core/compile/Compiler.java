package com.zifang.util.core.compile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zifang
 */
public class Compiler {

    private static CustomerClassLoader defineClassLoader = new CustomerClassLoader(Thread.currentThread().getContextClassLoader());

    public static Class<?> compile(String packageName , String simpleName , String script) {
        Class<?> clazz = null;

        try {
            Map<String, BytesJavaFileObject> map = CustomerJavaCompiler.compile(simpleName, script);
            String className = packageName + "." +simpleName;

            BytesJavaFileObject bytesJavaFileObject = map.get(className);
            if (bytesJavaFileObject == null) {
                throw new RuntimeException(String.format("cannot found class:%s", className));
            }

            clazz = defineClassLoader.defineClass(className, bytesJavaFileObject.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> compile(List<StringJavaFileObject> scripts, String getClass) {
        Class<?> clazz = null;
        Class returnClass = null;
        try {
            Map<String, BytesJavaFileObject> map = CustomerJavaCompiler.compile(scripts);

            for(Map.Entry<String,BytesJavaFileObject> entry : map.entrySet()){

                BytesJavaFileObject bytesJavaFileObject = map.get(entry.getKey());
                if (bytesJavaFileObject == null) {
                    throw new RuntimeException(String.format("cannot found class:%s", entry.getKey()));
                }

                if(entry.getKey().equals(getClass)){
                    returnClass = defineClassLoader.defineClass(entry.getKey(), bytesJavaFileObject.getBytes());
                } else {
                    // 主动拉到jvm
                    clazz = defineClassLoader.defineClass(entry.getKey(), bytesJavaFileObject.getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnClass;
    }


    public static Class<?> compile(Map<String,String> scriptCodeMap, String getClass) {
        List<StringJavaFileObject> scripts = new ArrayList<>();
        scriptCodeMap.forEach((key, value) -> scripts.add(new StringJavaFileObject(key, value)));
        return compile(scripts, getClass);
    }
}
