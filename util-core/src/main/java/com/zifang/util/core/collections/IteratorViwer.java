package com.zifang.util.core.collections;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 联立多个Iterator的操作
 *
 * */
public class IteratorViwer<E> implements Iterator<E>{


    public IteratorViwer(){

    }

    public IteratorViwer(Iterator<E> ... interators){

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
