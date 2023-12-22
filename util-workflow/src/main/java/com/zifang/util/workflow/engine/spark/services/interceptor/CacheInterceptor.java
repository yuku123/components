package com.zifang.util.workflow.engine.spark.services.interceptor;

import com.zifang.util.proxy.aspects.Aspect;
import com.zifang.util.workflow.annoation.EngineServiceInterceptor;

import java.lang.reflect.Method;

@EngineServiceInterceptor
public class CacheInterceptor implements Aspect {
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
