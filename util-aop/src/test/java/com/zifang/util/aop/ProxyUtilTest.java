package com.zifang.util.aop;

import com.zifang.util.aop.aspects.Aspect;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ProxyUtilTest {

    @Test
    void proxy() {
        A a = ProxyUtil.proxy(new A(), new Aspect() {
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
        });
        a.ex();
    }

    @Test
    void newProxyInstance() {
    }

    @Test
    void newProxyInstance1() {
    }
}

class A{
    public void ex(){
        System.out.println("this is ex");
    }
}