package com.zifang.util.core.lang.converter;

public interface IConverter<F, T> {

    T to(F value, T defaultValue);

    default T to(F value, Class<T> clazz){
        try {
            return to(value,clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
