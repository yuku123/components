package com.zifang.util.core.lang.exception;

public class ConversionException extends RuntimeException {

    public ConversionException(Object value, NumberFormatException nfex) {
        super("find exception when convert " + value, nfex);
    }
}
