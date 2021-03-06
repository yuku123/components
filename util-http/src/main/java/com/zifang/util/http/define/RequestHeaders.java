package com.zifang.util.http.define;

import java.lang.annotation.*;

/**
 * 请求行信息
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeaders {
    RequestHeader[] value();
}
