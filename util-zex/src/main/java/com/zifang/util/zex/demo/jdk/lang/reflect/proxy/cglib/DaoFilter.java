package com.zifang.util.zex.demo.jdk.lang.reflect.proxy.cglib;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class DaoFilter implements CallbackFilter {

    /**
     *
     * 意思是CallbackFilter的accept方法返回的数值表示的是顺序，顺序和setCallbacks里面Proxy的顺序是一致的。再解释清楚一点，Callback数组中有三个callback，那么：
     *
     * 方法名为"select"的方法返回的顺序为0，即使用Callback数组中的0位callback，即DaoProxy
     * 方法名不为"select"的方法返回的顺序为1，即使用Callback数组中的1位callback，即DaoAnotherProxy
     *
     * */
    @Override
    public int accept(Method method) {
        if ("select".equals(method.getName())) {
            return 0;
        }
        return 1;
    }

}