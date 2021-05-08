package com.zifang.util.zex.interview.demo2;

import com.zifang.util.aop.ProxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {


    private static Object createObject(String str) {

        IAImplement iaImplement = new IAImplement(str);
        Object o = Proxy.newProxyInstance(Test.class.getClassLoader(), IAImplement.class.getInterfaces(), new IAInvocationHandler(iaImplement, str));
        return o;
    }

    public static void main(String[] args) {
        IA ia = ProxyUtil.newProxyInstance((proxy, method, args1) -> {
            System.out.println(method.getName());
            return null;
        }, IA.class);
        ia.getName();
    }
}
