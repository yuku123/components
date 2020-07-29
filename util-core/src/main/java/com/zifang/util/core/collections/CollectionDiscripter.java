package com.zifang.util.core.collections;

import java.util.Collection;

/**
 * 集合的描述对象
 * */
public class CollectionDiscripter<E> {

    private Collection<E> collection;

    public CollectionDiscripter(Collection<E> collection){
        this.collection = collection;
    }

}
