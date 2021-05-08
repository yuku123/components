package com.zifang.util.aop.proxy;

import com.zifang.util.aop.ProxyUtil;
import com.zifang.util.aop.aspects.Aspect;
import com.zifang.util.aop.interceptor.JdkInterceptor;

/**
 * JDK实现的切面代理
 */
public class JdkProxyFactory extends ProxyFactory {
    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T proxy(T target, Aspect aspect) {
        return (T) ProxyUtil.newProxyInstance(//
                target.getClass().getClassLoader(), //
                new JdkInterceptor(target, aspect), //
                target.getClass().getInterfaces());
    }
}
