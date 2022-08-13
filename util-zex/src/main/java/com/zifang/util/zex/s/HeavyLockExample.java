package com.zifang.util.zex.s;


import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

public class HeavyLockExample {
    public static void main(String[] args) throws InterruptedException {
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
}