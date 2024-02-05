package com.zifang.util.core.lang.exception;

import com.zifang.util.core.meta.StatusCode;

/**
 * 业务异常
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1646453246258984129L;

    public BusinessException(StatusCode statusCode, String msg) {
        super(statusCode, msg);
    }

    public BusinessException(StatusCode statusCode, Throwable e) {
        super(statusCode, e);
    }

    public BusinessException(StatusCode statusCode) {
        super(statusCode);
    }

    public BusinessException(StatusCode statusCode, Object... params) {
        super(statusCode, params);
    }
}
