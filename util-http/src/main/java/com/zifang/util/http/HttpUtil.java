package com.zifang.util.http;

import java.util.Base64;

public class HttpUtil {

    /**
     * 模拟 curl -x 操作
     * */
    public static String toBasicAuthValue(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
