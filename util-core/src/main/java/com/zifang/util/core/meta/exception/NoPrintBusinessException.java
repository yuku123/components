package com.zifang.util.core.meta.exception;

import com.c2f.boot.base.meta.StatusCode;

/**
 * @author: zifang
 * @time: 2021/7/29 10:49:00
 * @description: 不打印的业务异常会提示给前端但不会打印到日志
 * @version: JDK 1.8
 */
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
