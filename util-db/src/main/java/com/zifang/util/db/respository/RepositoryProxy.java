package com.zifang.util.db.respository;

import com.zifang.util.aop.ProxyUtil;

public class RepositoryProxy {

    public static <I> I proxy(Class<I> clazz) {
        return ProxyUtil.newProxyInstance(new BaseRepositoryInvocationHandler(clazz), clazz);
    }
}
