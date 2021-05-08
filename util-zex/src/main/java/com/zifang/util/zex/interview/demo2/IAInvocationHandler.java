package com.zifang.util.zex.interview.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IAInvocationHandler implements InvocationHandler {
    private String s;
    private IAImplement iaImplement;

    public IAInvocationHandler(IAImplement iaImplement, String s) {
        this.iaImplement = iaImplement;
        this.s = s;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returns = null;
        if (method.getName().equals(iaImplement.getMethod())) {
            return iaImplement.getReturns();
        } else {
            throw new RuntimeException("method unkown");
        }
    }
}
