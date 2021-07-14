package com.zifang.util.core.lang.converter;

public interface IConverter<F, T> {
    T to(F f, T t);
}
