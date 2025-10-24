package com.zifang.util.http.base.helper;

import com.zifang.util.http.base.define.RequestMethod;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 根据 请求定义而产生请求
 */
public class HttpRequestProducer {

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public Object produceRequest(HttpRequestDefinition httpRequestDefination) {

        try {
            if (RequestMethod.GET == httpRequestDefination.getHttpRequestLine().getRequestMethod()) {
                return handleGetRequest(httpRequestDefination);
            } else if (RequestMethod.POST == httpRequestDefination.getHttpRequestLine().getRequestMethod()) {
                return handlePostRequest(httpRequestDefination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object handlePostRequest(HttpRequestDefinition httpRequestDefinition) throws IOException {
        HttpPost httpPost = new HttpPost(httpRequestDefinition.getHttpRequestLine().getUrl());
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity(new String(httpRequestDefinition.getHttpRequestBody().getBody())));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }

    private Object handleGetRequest(HttpRequestDefinition httpRequestDefination) throws IOException {
        HttpGet httpGet = new HttpGet(httpRequestDefination.getHttpRequestLine().getUrl());
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }
}
