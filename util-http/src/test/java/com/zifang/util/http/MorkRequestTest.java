package com.zifang.util.http;

import com.zifang.util.http.client.HttpRequestProxy;
import org.junit.Test;

public class MorkRequestTest {

    @Test
    public void t() {
        MockRequest mockRequest = HttpRequestProxy.proxy(MockRequest.class);
        String s = mockRequest.test1("name1", "password1");
        System.out.println(s);
    }

    @Test
    public void t2() {
        Data data = new Data();
        data.setName("name1");
        data.setPw("password1");

        MockRequest mockRequest = HttpRequestProxy.proxy(MockRequest.class);
        String s = mockRequest.test2(data);
        System.out.println(s);
    }
}
