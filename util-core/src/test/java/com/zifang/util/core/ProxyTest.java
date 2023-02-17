package com.zifang.util.core;


import com.zifang.util.core.util.ClassUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {

        aa a = new aa();

        ina i = (ina) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), aa.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("invoke before");
                Object returns = method.invoke(a, args);
                System.out.println("invoke after function");
                return returns;
            }
        });
        ClassUtil.saveClassFile(i.getClass());
        i.ex();
    }
    // spring jdk、 cgilib

}

interface ina {
    String ex();
}

class aa implements ina {

    @Override
    public String ex() {
        System.out.println("this is aa");
        return "";
    }
}
