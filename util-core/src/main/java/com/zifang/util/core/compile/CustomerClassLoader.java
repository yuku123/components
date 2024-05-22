package com.zifang.util.core.compile;

public class CustomerClassLoader extends ClassLoader{

    public CustomerClassLoader(ClassLoader parent) {
        super(parent);
    }
    public Class<?> defineClass(String className, byte[] bytes) {

        try {
            return super.defineClass(className,bytes,0,bytes.length);
        } catch (Exception | LinkageError e){
            e.printStackTrace();
        }
        return null;
    }
}
