package com.zifang.util.core.demo.jdk.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ArrayListMock<E> extends ArrayList<E> {

    // 默认容量为10，也就是通过new ArrayList()创建时的默认容量。
    private static final int DEFAULT_CAPACITY = 10;

    // 空的数组，这种是通过new ArrayList(0)创建时用的是这个空数组。
    private static final Object[] EMPTY_ELEMENTDATA = {};

    // 也是空数组，这种是通过new ArrayList()创建时用的是这个空数组，
    // 与EMPTY_ELEMENTDATA的区别是在添加第一个元素时使用这个空数组的会初始化为DEFAULT_CAPACITY（10）个元素
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    // 存储元素的数组
    transient Object[] elementData; // non-private to simplify nested class access

    // 集合中元素的个数
    private int size;

    public ArrayListMock(int initialCapacity) {
        if (initialCapacity > 0) {
            // 如果传入的初始容量大于0，就新建一个数组存储元素
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 如果传入的初始容量等于0，使用空数组EMPTY_ELEMENTDATA
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            // 如果传入的初始容量小于0，抛出异常
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    public ArrayListMock() {
        // 如果没有传入初始容量，则使用空数组DEFAULTCAPACITY_EMPTY_ELEMENTDATA
        // 使用这个数组是在添加第一个元素的时候会扩容到默认大小10
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 把传入集合的元素初始化到ArrayList中
     */
    public ArrayListMock(Collection<? extends E> c) {
        // 集合转数组
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // 检查c.toArray()返回的是不是Object[]类型，如果不是，重新拷贝成Object[].class类型
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // 如果c的空集合，则初始化为空数组EMPTY_ELEMENTDATA
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    public static void main(String[] args) {
//        ArrayList arrayList = new ArrayList();
//        arrayList.parallelStream()
    }

}
