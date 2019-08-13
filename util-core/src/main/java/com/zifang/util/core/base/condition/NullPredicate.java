package com.zifang.util.core.base.condition;

import java.util.function.Predicate;

public class  NullPredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return t==null?true:false;
    }
}
