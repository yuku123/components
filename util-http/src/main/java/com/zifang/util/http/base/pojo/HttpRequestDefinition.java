package com.zifang.util.http.base.pojo;

import lombok.Data;

/**
 * 所有注解获得到的信息都会在这个地方存储起来
 */
@Data
public class HttpRequestDefinition {

    /**
     * 请求行
     */
    private HttpRequestLine httpRequestLine;

    /**
     * 请求头
     */
    private HttpRequestHeader httpRequestHeader;

    /**
     * 请求体
     */
    private HttpRequestBody httpRequestBody;


}
