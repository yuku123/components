package com.zifang.util.zex.bust.charpter12;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlokingQueueTest {

    @Test
    public void test1_1() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.add(1);
        blockingQueue.add(1);
        blockingQueue.add(1);
    }

    @Test
    public void test2_1() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.add(1);
        blockingQueue.add(1);
        blockingQueue.remove();
        blockingQueue.remove();
        blockingQueue.remove();
    }

    @Test
    public void test3_1() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(2);
        blockingQueue.element();
    }
}
