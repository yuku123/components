package com.zifang.util.core.meta.exception;

import com.c2f.boot.base.meta.Result;
import com.c2f.boot.base.meta.StatusCode;

import static com.c2f.boot.base.meta.Result.buildMessage;

/**
 * 基础的异常类
 *
 * @author zhixiang.lwt on 2020/2/28.
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 2059913032332171665L;

    /**
     * 错误
     */
    private final StatusCode statusCode;

    /**
     * 错误信息
     */
    private String message;

    public BaseException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        this.message = statusCode.getMessage();
    }

    public BaseException(StatusCode statusCode, Object... params) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        String baseMessage = statusCode.getMessage();
        this.message = buildMessage(baseMessage, params);
    }

    public BaseException(StatusCode statusCode, Throwable e) {
        super(statusCode.getMessage(), e);
        this.statusCode = statusCode;
        this.message = statusCode.getMessage();
    }

    public BaseException(Result<?> result, Object... params) {
        this(new StatusCode() {
            @Override
            public int getCode() {
                return result.getCode();
            }

            @Override
            public String getMessage() {
                return result.getMessage();
            }
        }, params);
    }

    public BaseException(Result<?> result, Throwable e) {
        this(new StatusCode() {
            @Override
            public int getCode() {
                return result.getCode();
            }

            @Override
            public String getMessage() {
                return result.getMessage();
            }
        }, e);
    }

    public BaseException(Result<?> result) {
        this(new StatusCode() {
            @Override
            public int getCode() {
                return result.getCode();
            }

            @Override
            public String getMessage() {
                return result.getMessage();
            }
        });
    }

    public int getCode() {
        return statusCode.getCode();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return String
                .format("BaseException[status:%s(%s),message:%s]", statusCode, statusCode.getCode(),
                        message);
    }
}
