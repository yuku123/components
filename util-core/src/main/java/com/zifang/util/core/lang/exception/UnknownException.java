package com.zifang.util.core.lang.exception;

public class UnknownException extends RuntimeException {

    private static final long serialVersionUID = -7655513487870988265L;

    public UnknownException(String message) {
        super(message);
    }

    public UnknownException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownException() {
        super();
    }

    public UnknownException(Throwable cause) {
        super(cause);
    }

}
