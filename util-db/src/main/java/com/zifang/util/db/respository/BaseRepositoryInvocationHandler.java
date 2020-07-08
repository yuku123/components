package com.zifang.util.db.respository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class BaseRepositoryInvocationHandler implements InvocationHandler {

    private Class targetClass;

    public BaseRepositoryInvocationHandler(Class clazz) {
        this.targetClass = clazz;

        solve(targetClass);
    }

    private void solve(Class targetClass) {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
