package com.zifang.util.db.respository;

import com.zifang.util.core.pattern.aop.aspects.Aspect;

import java.lang.reflect.Method;

public class BaseRepositoryAspect implements Aspect {


    @Override
    public boolean before(Object target, Method method, Object[] args) {
        return false;
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
