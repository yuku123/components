package com.zifang.util.core.collections.venn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 *
 * 两个集合之间的内容关系
 *
 * */
public class VennDiscriptor<E> {

    private Collection<E> empty = new ArrayList<>();

    private Collection<E> collection1 = null;
    private Collection<E> collection2 = null;

    public VennDiscriptor(Collection<E> collection1, Collection<E> collection2){
        this.collection1 = collection1;
        this.collection2 = collection2;
    }

    public static <E> void discripeVenn(Collection<E> collection1,Collection<E> collection2){

        Collection<E> disctinctedCollection1 = collection1.stream().distinct().collect(Collectors.toList());
        Collection<E> disctinctedCollection2 = collection2.stream().distinct().collect(Collectors.toList());

    }

}
