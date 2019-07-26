package com.zifang.util.core.beans.converter.exception;

public class ConvertionException extends RuntimeException {

    public ConvertionException(Object value, NumberFormatException nfex) {
        super("find exception when convert "+value,nfex);
    }
}
