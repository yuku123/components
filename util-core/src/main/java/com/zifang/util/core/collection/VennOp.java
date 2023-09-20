package com.zifang.util.core.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 * 描述两个集合之间的内容关系
 */
public class VennOp<E> {

    private Collection<E> empty = new ArrayList<>();

    private Collection<E> c1 = null;
    private Collection<E> c2 = null;

    public VennOp(Collection<E> c1, Collection<E> c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * 并集 c1 U c2
     */
    public Collection<E> union() {
        Collection<E> c = new ArrayList<>();

        c.addAll(c1.stream().distinct().collect(Collectors.toList()));
        c.addAll(c2.stream().distinct().collect(Collectors.toList()));

        return c;
    }

    public Integer unionCount() {
        return union().size();
    }

    /**
     * 交集 c1 n c2
     */
    public Collection<E> intersection() {
        Collection<E> dc1 = c1.stream().distinct().collect(Collectors.toList());
        Collection<E> dc2 = c2.stream().distinct().collect(Collectors.toList());
        dc1.removeAll(dc2);
        return dc1;
    }

    /**
     * 交集 c1 n c2 后的集合大小
     */
    public Integer intersectionCount() {
        return intersection().size();
    }

    public void dicript() {
//        long left = 1;
//        long
    }
}
