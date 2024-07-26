package com.zifang.util.zex.bust.chapter10;

import org.junit.Test;

import java.lang.reflect.Method;

class LoadClass{

}


public class LoadClassTest {

    @Test
    public void test001() throws ClassNotFoundException{
        // 直接引用类
        Class<?> c1 = LoadClass.class;
        // 只用forName加载类
        Class<?> c2 = Class.forName("com.zifang.util.zex.bust.chapter10.LoadClass");
        // 从对象实例内获得class
        Class<?> c3 = new LoadClass().getClass();
        // 使用类加载器进行加载
        Class<?> c4 = Thread.currentThread().getContextClassLoader().loadClass("com.zifang.util.zex.bust.chapter10.LoadClass");

        System.out.println("c1:"+c1.getName());
        System.out.println("c2:"+c2.getName());
        System.out.println("c3:"+c3.getName());
        System.out.println("c4:"+c2.getName());

    }

    @Test
    public void test002() throws ClassNotFoundException {
        Class<?> c1 = int.class;
        Class<?> c2 = Class.forName("I");
        System.out.println();
    }

    @Test
    public void test003() throws ClassNotFoundException {
        Class<?> c1 = int.class;
        for(Method method : c1.getClass().getMethods()){
            System.out.println(method.getReturnType().getName() + " " + method.getName() + "(" + ");");
        }
        System.out.println();
    }

}
