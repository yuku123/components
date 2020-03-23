package com.zifang.util.web;

public enum CommonCode implements ResultCode{

    // 参数错误
    INVALID_PARAM(false,10003,"非法参数！");

    // 操作错误
    // 权限相关
    // 未知错误

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String msg;

    CommonCode(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return msg;
    }
}
