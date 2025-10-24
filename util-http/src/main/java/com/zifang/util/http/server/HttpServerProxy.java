package com.zifang.util.http.server;

import com.zifang.util.http.client.HttpRequestInvocationHandler;
import com.zifang.util.proxy.ProxyUtil;

/**
 * 针对请求接口的代理
 */
public class HttpServerProxy {

    public static <T> T proxy(Class<T> serverInterface) {
        return ProxyUtil.newProxyInstance(new HttpServerInvocationHandler(serverInterface), serverInterface);
    }
}
