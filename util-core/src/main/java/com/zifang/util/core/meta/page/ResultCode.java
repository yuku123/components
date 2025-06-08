package com.zifang.util.core.meta.page;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;


@Getter
@AllArgsConstructor
public enum ResultCode {

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception") {},
    SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful") {},
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 Not Found") {},
    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "Request Rejected") {},
    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Request Unauthorized") {},
    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Required Parameter") {},
    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "Message Can't be Read") {},
    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Mismatch") {},
    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error") {},
    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Validation Error") {},
    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Supported") {},
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error") {},
    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Media Type Not Supported") {};

    final Integer code;
    final String msg;
}
