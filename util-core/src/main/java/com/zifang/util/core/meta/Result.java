package com.zifang.util.core.meta;

import com.zifang.util.core.constant.SymbolConstant;
import com.zifang.util.core.lang.StringUtil;
import com.zifang.util.core.lang.exception.BaseException;

import java.io.Serializable;


/**
 * 接口返回参数封装类
 *
 * @param <T>
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1444605237688228650L;

    /**
     * 调用成功返回的对象
     */
    private T data;

    /**
     * 调用是否成功
     */
    private boolean success = Boolean.FALSE;

    /**
     * 错误码
     */
    private int code = BaseStatusCode.OK.getCode();

    /**
     * 错误信息
     */
    private String message;

    /***
     * 构造器私有
     */
    private Result() {
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.data(null).success(Boolean.TRUE);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.data(data).success(Boolean.TRUE);
        return result;
    }


    public static <T> Result<T> error(StatusCode statusCode, Object... params) {
        Result<T> result = new Result<>();
        result.code(statusCode.getCode());
        String statusMessage = statusCode.getMessage();
        statusMessage = buildMessage(statusMessage, params);

        result.message(statusMessage);
        return result;
    }

    public static <T> Result<T> error(BaseException baseException, Object... params) {
        String baseMessage = baseException.getMessage();
        baseMessage = buildMessage(baseMessage, params);

        return error(baseException.getCode(), baseMessage);
    }

    private static <T> Result<T> error(int code, String message) {
        return new Result<T>().code(code).message(message);
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 自定义返回数据
     *
     * @param data 返回数据
     * @return 返回对象
     */
    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 自定义错误信息
     *
     * @param message 错误码
     */
    private Result<T> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 自定义状态码
     *
     * @param code 状态码
     * @return 返回对象
     */
    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    /***
     * 自定义返回结果
     * @param success 是否成功
     * @return 返回对象
     */
    private Result<T> success(Boolean success) {
        this.success = success;
        return this;
    }

    public static String buildMessage(String statusMessage, Object... params) {
        StringBuilder message = new StringBuilder();
        boolean isFormat = Boolean.FALSE;
        if (null != statusMessage) {
            message.append(statusMessage);
            if (statusMessage.contains(SymbolConstant.CURLY_BRACE)) {
                statusMessage = StringUtil.replace(statusMessage, SymbolConstant.CURLY_BRACE, "%s",false);
            }
            isFormat = StringUtil.isFormat(statusMessage);

            if (isFormat) {
                try {
                    message = new StringBuilder(String.format(statusMessage, params));
                } catch (Exception e) {
                    // 下游传递的message里面可能包含%s，比如"Format specifier '%s'"，这种场景在这里format也会抛异常
                    // ignore
                }
            }
        }
        // 不是format信息使用|拼接
        if (!isFormat) {
            if (params != null) {
                for (Object param : params) {
                    message.append("|").append(param);
                }
            }
        }

        return message.toString();
    }


    @Override
    public String toString() {
        return "Result{" + "data=" + data + ", success=" + success + ", code=" + code + ", message='" + message + '\'' + '}';
    }
}
