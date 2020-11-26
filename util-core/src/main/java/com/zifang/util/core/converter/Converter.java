package com.zifang.util.core.converter;

public interface Converter<F,T> {
    T to(F f,T t);
}
