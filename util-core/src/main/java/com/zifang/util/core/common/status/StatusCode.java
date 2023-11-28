package com.zifang.util.core.common.status;


/**
 * 状态码接口
 */
public interface StatusCode {

    /**
     * 状态码标示
     */
    int getCode();

    /**
     * 错误信息
     */
    String getMessage();
}
