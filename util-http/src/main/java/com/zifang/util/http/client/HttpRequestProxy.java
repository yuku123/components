package com.zifang.util.http.client;

import com.zifang.util.proxy.ProxyUtil;

/**
 * 针对请求接口的代理
 */
public class HttpRequestProxy {

    public static <T> T proxy(Class<T> requestInterface) {
        return ProxyUtil.newProxyInstance(new HttpRequestInvocationHandler(requestInterface), requestInterface);
    }
}
