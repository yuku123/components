package com.zifang.util.zex.demo.jdk.util.collections.blocking;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {
    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(1,false,a);
    }
}
