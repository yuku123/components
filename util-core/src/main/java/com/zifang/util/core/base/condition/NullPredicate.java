package com.zifang.util.core.base.condition;

import java.util.function.Predicate;


/**
 * 判断是否为null的类
 * */
public class  NullPredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return t==null?true:false;
    }
}