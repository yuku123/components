package com.zifang.util.proxy.aspects;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 简单切面类，不做任何操作<br>
 * 可以继承此类实现自己需要的方法即可
 */
public class SimpleAspect implements Aspect, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        //继承此类后实现此方法
        return true;
    }


    /**
     * 目标方法执行后的操作
     */
    public boolean after(Object target, Method method, Object[] args) {
        //继承此类后实现此方法
        return after(target, method, args, null);
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        //继承此类后实现此方法
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        //继承此类后实现此方法
        return true;
    }

}
