package com.zifang.util.http;

import com.zifang.util.aop.ProxyUtil;

/**
 * 针对请求接口的代理
 */
public class HttpRequestProxy {

    public static <T> T proxy(Class<T> requestInterface) {
        return ProxyUtil.newProxyInstance(new HttpRequestInvocationHandler(requestInterface), requestInterface);
    }
}
