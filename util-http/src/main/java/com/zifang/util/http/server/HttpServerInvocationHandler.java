package com.zifang.util.http.server;

import com.zifang.util.http.base.define.RestController;
import com.zifang.util.http.base.helper.HttpDefinitionSolver;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;
import com.zifang.util.http.base.helper.HttpRequestProducer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class HttpServerInvocationHandler implements InvocationHandler {

    // 代理的接口类
    private final Class<?> target;

    // 调用过程中需要的上下文参数
    private Map<String,Object> contextParams;

    public HttpServerInvocationHandler(Class<?> requestInterface) {
        this.target = requestInterface;
    }

    public HttpServerInvocationHandler(Class<?> requestInterface, Map<String,Object> contextParams) {
        this.target = requestInterface;
        this.contextParams = contextParams;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1 检查相关
        check(proxy, method, args);

        // 2 解释器
        HttpDefinitionSolver httpDefinitionSolver = new HttpDefinitionSolver();
        httpDefinitionSolver.set(target, proxy, method, args, contextParams); // 设入参数
        httpDefinitionSolver.solve();

        // 3 获得标准请求定义
        HttpRequestDefinition httpRequestDefinition = httpDefinitionSolver.getHttpRequestDefinition();

        // 4 产生请求
        HttpRequestProducer httpRequestProducer = new HttpRequestProducer();
        return httpRequestProducer.produceRequest(httpRequestDefinition);

    }

    private void check(Object proxy, Method method, Object[] args) {
        // 1. class 必须继承为 RestController
        if (!target.isAnnotationPresent(RestController.class)) {
            throw new RuntimeException(target.getName() + "类没有RestController注解");
        }
    }
}
