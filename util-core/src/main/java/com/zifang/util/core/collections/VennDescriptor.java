package com.zifang.util.core.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 *
 * 描述两个集合之间的内容关系
 *
 * */
public class VennDescriptor<E> {

    private Collection<E> empty = new ArrayList<>();

    private Collection<E> c1 = null;
    private Collection<E> c2 = null;

    public VennDescriptor(Collection<E> c1, Collection<E> c2){
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * 并集 c1 U c2
     *
     * */
    public Collection<E> union(){

        Collection<E> dc1 = c1.stream().distinct().collect(Collectors.toList());

        Collection<E> dc2 = c1.stream().distinct().collect(Collectors.toList());

        dc1.addAll(dc2);

        return dc1;
    }

    public Integer unionCount(){
        return union().size();
    }

    /**
     * 交集 c1 n c2
     * */
    public Collection<E> intersection(){
        Collection<E> dc1 = c1.stream().distinct().collect(Collectors.toList());
        Collection<E> dc2 = c1.stream().distinct().collect(Collectors.toList());
        dc1.removeAll(dc2);
        return dc1;
    }

    /**
     * 交集 c1 n c2 后的集合大小
     * */
    public Integer intersectionCount(){
        return intersection().size();
    }

    public void dicript(){

    }


}
