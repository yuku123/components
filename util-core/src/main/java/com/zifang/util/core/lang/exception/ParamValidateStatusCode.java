package com.zifang.util.core.lang.exception;


import com.zifang.util.core.meta.StatusCode;

/**
 * 接口参数异常错误码定义
 */
public enum ParamValidateStatusCode implements StatusCode {

    PARAMETER_ERROR(1, "{}");

    ParamValidateStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
