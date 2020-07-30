package com.zifang.util.http.helper;

import com.zifang.util.http.define.RequestMethod;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 根据 请求定义而产生请求
 * */
public class HttpRequestProducer {

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public Object produceRequest(HttpRequestDefination httpRequestDefination) {

        try {
            if(RequestMethod.GET == httpRequestDefination.getHttpRequestLine().getRequestMethod()){
                return handleGetRequest(httpRequestDefination);
            } else if(RequestMethod.POST == httpRequestDefination.getHttpRequestLine().getRequestMethod()){
                return handlePostRequest(httpRequestDefination);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object handlePostRequest(HttpRequestDefination httpRequestDefination) {
        return null;
    }

    private Object handleGetRequest(HttpRequestDefination httpRequestDefination) throws IOException {
        HttpGet httpGet = new HttpGet(httpRequestDefination.getHttpRequestLine().getUrl());
        CloseableHttpResponse response = httpClient.execute(httpGet);;
        return EntityUtils.toString(response.getEntity());
    }

}
