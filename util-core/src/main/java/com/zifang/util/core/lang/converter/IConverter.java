package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.tuples.Pair;

public interface IConverter<F, T> {

    default T to(F value){
        throw new RuntimeException("未实现");
    }

    default Pair<Class<?>, Class<?>> getPair(){
        return null;
    }

    T to(F value, T defaultValue);

    default T to(F value, Class<T> clazz){
        try {
            return to(value,clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
