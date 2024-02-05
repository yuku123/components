package com.zifang.util.core.lang.collection;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 将Iterator类型的数据转化为流操作
 *
 * @author zifang
 */
public class Streams {

    public static <T> Stream<T> streamOf(Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
    }

    public static <T> Stream<T> streamOf(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> parallelStreamOf(Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), true);
    }

    public static <T> Stream<T> parallelStreamOf(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }
}
