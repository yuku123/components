package com.zifang.util.core.pattern.factory;

public interface IFactory<K, T> {

    T getInstance(K k);

}
