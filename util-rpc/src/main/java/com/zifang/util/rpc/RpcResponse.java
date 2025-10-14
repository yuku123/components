package com.zifang.util.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse implements Serializable {
    private String requestId;
    private Object result;
    private Throwable exception;

    // 构造函数、getter和setter省略
}
