package com.zifang.util.zex.s;

import org.openjdk.jol.info.ClassLayout;

public class BiasedLockExample {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000L);
        BiasedLockExample example = new BiasedLockExample();
        System.out.println("加锁之前");
        System.out.println(ClassLayout.parseInstance(example).toPrintable());
        synchronized (example) {
            System.out.println("加锁之后");
            System.out.println(ClassLayout.parseInstance(example).toPrintable());
        }
    }
}