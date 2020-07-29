package com.zifang.util.http;

import org.junit.Test;

public class MorkRequestTest {

    @Test
    public void t(){
        MockRequest mockRequest = HttpRequestProxy.proxy(MockRequest.class);
        String s = mockRequest.test1("name1","password1");
        System.out.println(s);
    }
}
