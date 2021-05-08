package com.zifang.util.http.helper;

import com.zifang.util.http.define.RequestMethod;
import lombok.Data;

/**
 * 请求行
 */
@Data
public class HttpRequestLine {

    /**
     * 请求种类
     */
    private RequestMethod requestMethod;

    /**
     * 请求地址
     */
    private String url;
}
