package com.zifang.util.core.function;

@FunctionalInterface
public interface Function2<T1,T2,R> {

    R accept(T1 t1,T2 t2);

}
