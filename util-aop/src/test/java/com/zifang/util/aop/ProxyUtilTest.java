package com.zifang.util.aop;

import com.zifang.util.aop.aspects.Aspect;
import org.junit.Test;

import java.lang.reflect.Method;


public class ProxyUtilTest {

    @Test
    public void proxy() {
        A a = ProxyUtil.proxy(new A(), new CustomerAspect());
        a.ex();
    }
}

class A {
    public void ex() {
        System.out.println("this is ex");
    }
}

class CustomerAspect implements Aspect {

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        System.out.println("before");
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        return false;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        return false;
    }
}