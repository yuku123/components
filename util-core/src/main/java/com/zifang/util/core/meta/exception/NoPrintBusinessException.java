package com.zifang.util.core.meta.exception;


import com.zifang.util.core.meta.StatusCode;


public class NoPrintBusinessException extends BaseException {

    private static final long serialVersionUID = 2519740137508800641L;

    public NoPrintBusinessException(StatusCode statusCode, Throwable e) {
        super(statusCode, e);
    }

    public NoPrintBusinessException(StatusCode statusCode) {
        super(statusCode);
    }

    public NoPrintBusinessException(StatusCode statusCode, Object... params) {
        super(statusCode, params);
    }

}
