package com.zifang.util.zex.bust.charpter12;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    public volatile static AtomicInteger i = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while(i++ < 10000){
                    AtomicTest.i.incrementAndGet();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while(i++ < 10000){
                    AtomicTest.i.incrementAndGet();
                }
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(AtomicTest.i);
        AtomicTest.i.
    }
}
