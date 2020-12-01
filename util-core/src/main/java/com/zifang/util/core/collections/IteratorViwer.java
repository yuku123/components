package com.zifang.util.core.collections;

import java.util.*;
import java.util.function.Consumer;

/**
 * 联立多个Iterator
 *
 * 批量操作多个Iterator 的工具
 *
 * @author zifang
 *
 * */
public class IteratorViwer<E> implements Iterator<E>{

    private List<Iterator<E>> innerIteratorList = new ArrayList<>();

    public IteratorViwer(){}

    public IteratorViwer(Iterator<E> ... interators){
        innerIteratorList.addAll(Arrays.asList(interators));
    }

    public void add(IteratorViwer<E> iteratorViwer){}

    public void add(Iterable<E> iterable){}

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        return null;
    }

    @Override
    public void remove() {

    }
    @Override
    public void forEachRemaining(Consumer<? super E> action) {

    }
}
