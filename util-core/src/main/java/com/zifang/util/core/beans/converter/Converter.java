package com.zifang.util.core.beans.converter;

public interface Converter<F,T> {
    T to(F f,T t);
}
