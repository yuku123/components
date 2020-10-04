package com.zifang.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class HttpClientTest {

    private CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    @Test
    public void testGet() throws IOException {
        String url = "/api/files/1";
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPut() throws IOException {
        String url = "/api/user";
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-Type", "application/json;charset=utf8");
        httpPut.setEntity(new StringEntity("吃饭", "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPut);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPost() throws IOException {
        String url = "/api/user";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity("吃饭", "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testUpload1() throws IOException {
        String url = "/api/files/1";
        HttpPost httpPost = new HttpPost(url);
        File file = new File("C:/Users/hetiantian/Desktop/学习/docker_practice.pdf");
        FileBody fileBody = new FileBody(file);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);  //addPart上传文件
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testDelete() throws IOException {
        String url = "/api/user/12";
        HttpDelete httpDelete = new HttpDelete(url);
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
