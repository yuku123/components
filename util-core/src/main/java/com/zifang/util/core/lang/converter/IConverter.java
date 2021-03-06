package com.zifang.util.core.lang.converter;

public interface IConverter<F, T> {

    T to(F value, T defaultValue);
}
