package com.zifang.util.http.client;

import com.zifang.util.proxy.ProxyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 针对请求接口的代理
 */
public class HttpRequestProxy {

    public static <T> T proxy(Class<T> requestInterface) {
        return proxy(requestInterface, new HashMap<>());
    }

    public static <T> T proxy(Class<T> requestInterface, Map<String,Object> contextParams) {
        HttpRequestInvocationHandler httpRequestInvocationHandler = new HttpRequestInvocationHandler(requestInterface);
        httpRequestInvocationHandler.setContextParams(contextParams);
        return ProxyUtil.newProxyInstance(httpRequestInvocationHandler, requestInterface);
    }
}
