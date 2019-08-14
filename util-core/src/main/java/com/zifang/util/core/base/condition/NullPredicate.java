package com.zifang.util.core.base.condition;

import java.util.function.Predicate;


/**
 * 在此先记录一下，此目标是为了条件过滤
 *
 * 提供逻辑判断的
 *
 * 组合框架，验证框架
 *
 * https://blog.csdn.net/ghsau/article/details/51704892
 *
 * */
public class  NullPredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return t==null?true:false;
    }
}
