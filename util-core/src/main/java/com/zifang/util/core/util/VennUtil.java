package com.zifang.util.core.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/***
 * 韦恩图操作
 * @author zifang
 */
public class VennUtil {

    public static <E> Collection<E> union(Collection<E>... collection) {
        Collection<E> base = new ArrayList<>();
        for (Collection<E> collectionElement : collection) {
            base.addAll(new ArrayList<>(collectionElement));
        }
        base = base.stream().distinct().collect(Collectors.toList());
        return base;
    }

    public static <E> Collection<E> retain(Collection<E> collection1, Collection<E> collection2) {
        Collection<E> left = new ArrayList<>(collection1);
        Collection<E> right = new ArrayList<>(collection2);
        left.retainAll(right);
        return left;
    }
}