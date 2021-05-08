package com.zifang.util.http;

import com.zifang.util.aop.ProxyUtil;
import com.zifang.util.core.annoations.AnnotationUtil;
import com.zifang.util.http.define.RestController;

/**
 * 针对请求接口的代理
 */
public class HttpRequestProxy {

    public static <T> T proxy(Class<T> requestInterface) {
        return ProxyUtil.newProxyInstance(new HttpRequestInvocationHandler(requestInterface), requestInterface);
    }
}
