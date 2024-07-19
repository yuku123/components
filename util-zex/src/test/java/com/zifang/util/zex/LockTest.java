package com.zifang.util.zex;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class LockTest {

     static class BiasedLockExample{}
     static class HeavyLockExample{}

    @Test
    public void test001() throws InterruptedException {
        Thread.sleep(5000L);
        BiasedLockExample example = new BiasedLockExample();
        System.out.println("加锁之前");
        System.out.println(ClassLayout.parseInstance(example).toPrintable());
        synchronized (example) {
            System.out.println("加锁之后");
            System.out.println(ClassLayout.parseInstance(example).toPrintable());
        }
    }

    @Test
    public void test002() throws InterruptedException {
        HeavyLockExample heavy = new HeavyLockExample();
        System.out.println("加锁之前");
        System.out.println(ClassLayout.parseInstance(heavy).toPrintable());
        Thread t1 = new Thread(() -> {
            synchronized (heavy) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();        //确保t1线程已经运行
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("t1线程抢占了锁");
        System.out.println(ClassLayout.parseInstance(heavy).toPrintable());
        synchronized (heavy) {
            System.out.println("main线程来抢占锁");
            System.out.println(ClassLayout.parseInstance(heavy).toPrintable());
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
