package com.zifang.util.core.lang.exception;


import com.zifang.util.core.meta.StatusCode;

/**
 * 接口参数异常错误码定义
 */
public enum ParamValidateStatusCode implements StatusCode {

    // 参数错误
    PARAMETER_ERROR(1, "{}");

    ParamValidateStatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    @Override
    public int getCode() {
        return 1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
