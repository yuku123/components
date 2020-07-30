package com.zifang.util.http;

import com.zifang.util.core.annoations.AnnotationUtil;
import com.zifang.util.http.define.RequestMapping;
import com.zifang.util.http.define.RequestMethod;
import com.zifang.util.http.define.RequestParam;
import com.zifang.util.http.define.RestController;
import com.zifang.util.http.helper.HttpDefinitionSolver;
import com.zifang.util.http.helper.HttpRequestDefination;
import com.zifang.util.http.helper.HttpRequestProducer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestInvocationHandler implements InvocationHandler {

    private Class target; // 代理的接口类

    public  HttpRequestInvocationHandler(Class requestInterface) {
        this.target = requestInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1 检查相关
        check(proxy,method,args);

        // 2 解释器
        HttpDefinitionSolver httpDefinitionSolver = new HttpDefinitionSolver();
        httpDefinitionSolver.set(target,proxy,method,args); // 设入参数
        httpDefinitionSolver.solve();

        // 3 获得标准请求定义
        HttpRequestDefination httpRequestDefination = httpDefinitionSolver.getHttpRequestDefination();

        // 4 产生请求
        HttpRequestProducer httpRequestProducer = new HttpRequestProducer();
        Object response = httpRequestProducer.produceRequest(httpRequestDefination);
        return response;

    }

    private void check(Object proxy, Method method, Object[] args) {
        // 1. class 必须继承为 RestController
        if( !target.isAnnotationPresent(RestController.class)) {
            throw new RuntimeException(target.getName() + "类没有RestController注解");
        }
    }
}
