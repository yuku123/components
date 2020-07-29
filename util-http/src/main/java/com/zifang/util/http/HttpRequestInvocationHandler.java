package com.zifang.util.http;

import com.zifang.util.core.annoations.AnnotationUtil;
import com.zifang.util.http.define.RequestMapping;
import com.zifang.util.http.define.RequestMethod;
import com.zifang.util.http.define.RestController;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpRequestInvocationHandler implements InvocationHandler {

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();


    private Class target; // 代理的接口类

    public  HttpRequestInvocationHandler(Class requestInterface) {
        this.target = requestInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String basicPath = null;

        // 1. class 必须继承为 RestController
        if(target.isAnnotationPresent(RestController.class)){

            basicPath = AnnotationUtil.getAnnotationValue(target,RestController.class);

        } else {

            throw new RuntimeException(target.getName()+"类没有RestController注解");

        }

        // 查看当前的方法体的调用方式是什么
        RequestMethod requestMethod = AnnotationUtil.getAnnotationValue(method, RequestMapping.class,"method");

        // 当前方法的调用地址是什么
        String requestPath = AnnotationUtil.getAnnotationValue(method, RequestMapping.class,"value");

        if(requestMethod == RequestMethod.GET){

            String url = basicPath+requestPath;

            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity()));

            //httpClient.
        }

        return "";

    }
}
