package com.zifang.util.http;

import org.junit.Test;

public class MorkRequestTest {

    @Test
    public void t(){
        MockRequest mockRequest = HttpRequestProxy.proxy(MockRequest.class);
    }
}
