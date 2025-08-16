package com.zifang.util.core.lang.exception;


public class ExecuteScriptException extends RuntimeException {

    private static final long serialVersionUID = -2590455067812497134L;

    public ExecuteScriptException(String message) {
        super(message);
    }

    public ExecuteScriptException(String message, Throwable cause) {
        super(message, cause);
    }

}
