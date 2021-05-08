package com.zifang.util.aop;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Test2 {

    @Test
    public void test() {

        te t = ProxyUtil.newProxyInstance(new in(), te.class);
        t.t();

    }
}

interface te {
    void t();
}

class in implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName());
        return null;
    }
}