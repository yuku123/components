package com.zifang.util.core.collections;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Lists {

    /**
     * 将多个元素变为List
     *
     * @param t1 the elements you appended need to be transform to the list
     * @return a new list
     * */
    public static <T> List<T> of(T... t1){
        return Arrays.asList(t1);
    }

    /**
     * 传入一个字符串，传入分割符，得到子序列的List
     *
     * */
    public static List<String> of(String content,String splitor){
        return Arrays.asList(content.split(splitor));
    }

    /**
     *
     * 对一个数组进行过滤操作
     *
     * @param elements 将要被处理的list
     * @param predicate 判断条件
     * */
    public static <E> List<E> filter(List<E> elements,Predicate<E> predicate){
        return elements.stream().filter(predicate).collect(Collectors.toList());
    }

}
