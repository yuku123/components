package com.zifang.util.http;

import com.zifang.util.core.annoations.AnnotationUtil;
import com.zifang.util.http.define.RequestMapping;
import com.zifang.util.http.define.RequestMethod;
import com.zifang.util.http.define.RequestParam;
import com.zifang.util.http.define.RestController;
import com.zifang.util.http.helper.HttpDefinationSolveChain;
import com.zifang.util.http.helper.HttpRequestDefination;
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

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();


    private Class target; // 代理的接口类

    public  HttpRequestInvocationHandler(Class requestInterface) {
        this.target = requestInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        check(proxy,method,args);

        HttpDefinationSolveChain solveChain = new HttpDefinationSolveChain();
        solveChain.set(proxy,method,args);
        solveChain.solve();

        HttpRequestDefination httpRequestDefination = solveChain.getHttpRequestDefination();




        String basicPath = AnnotationUtil.getAnnotationValue(target,RestController.class);

        // 查看当前的方法体的调用方式是什么
        RequestMethod requestMethod = AnnotationUtil.getAnnotationValue(method, RequestMapping.class,"method");

        // 当前方法的调用地址是什么
        String requestPath = AnnotationUtil.getAnnotationValue(method, RequestMapping.class,"value");

        // 方法参数
        Parameter[] paras = method.getParameters();

        if(requestMethod == RequestMethod.GET){

            String url = basicPath+requestPath;


            List<String> s = new ArrayList<>();

            for(int i = 0; i<paras.length; i++){
                String key = AnnotationUtil.getAnnotationValue(paras[i], RequestParam.class);
                String value = String.valueOf(args[i]);
                s.add(key+"="+value);
            }

            if(s.size() > 0){
                url = url + "?" + String.join("&",s);
            }

            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);


            System.out.println(EntityUtils.toString(response.getEntity()));

            //httpClient.
        }

        return "";

    }

    private void check(Object proxy, Method method, Object[] args) {

        // 1. class 必须继承为 RestController
        if( !target.isAnnotationPresent(RestController.class)) {
            throw new RuntimeException(target.getName() + "类没有RestController注解");
        }
    }
}
