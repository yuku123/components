package com.zifang.util.core.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * 将Iterator类型的数据转化为流操作
 *
 *
 * */
public class StreamUtils {


    /**
     * Converts iterator to a stream.
     */
    public static <T> Stream<T> streamOf(final Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
    }

    /**
     * Converts interable to a non-parallel stream.
     */
    public static <T> Stream<T> streamOf(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Wraps an iterator as a stream.
     */
    public static <T> Stream<T> parallelStreamOf(final Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), true);
    }

    /**
     * Wraps an iterator as a stream.
     */
    public static <T> Stream<T> parallelStreamOf(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }
}
