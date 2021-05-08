package com.zifang.util.core.lang.converter;

public interface Converter<F,T> {
    T to(F f,T t);
}
