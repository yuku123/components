package com.zifang.util.core.demo.jdk.java.util.collections;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class ArrayDequeTest {

    //不懂的部分
    private void allocateElements(int numElements) {
        int initialCapacity = 8;
        //获得最接近2的次方的数字？？？？？
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        System.out.println(initialCapacity);
    }


    public static void testInitial1(){
        ArrayDeque<String> arrayDeque = new ArrayDeque<>(100);

    }
    public static void testInitial2(){
        List<String> arrayList = Arrays.asList("1","2","1","2","1","2","1","2","1","2","1","2","1","2","1","2","1","2","1","2","1","2","1","2");
        ArrayDeque<String> arrayDeque = new ArrayDeque(arrayList);

    }


    public static void main(String[] args) {
        testInitial2();
    }
}
