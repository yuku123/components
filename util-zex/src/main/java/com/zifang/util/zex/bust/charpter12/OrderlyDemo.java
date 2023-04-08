package com.zifang.util.zex.bust.charpter12;

import java.time.LocalDateTime;

public class OrderlyDemo {

    static int value = 1;
    private static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 199; i++) {
            value = 1;
            flag = false;
            Thread thread1 = new DisplayThread();
            Thread thread2 = new CountThread();
            thread1.start();
            thread2.start();
            System.out.println("=========================================================");
            Thread.sleep(10);
        }
    }

    static class DisplayThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " DisplayThread begin, time:" + LocalDateTime.now());
            value = 1024;
            System.out.println(Thread.currentThread().getName() + " change flag, time:" + LocalDateTime.now());
            flag = true;
            System.out.println(Thread.currentThread().getName() + " DisplayThread end, time:" + LocalDateTime.now());
        }
    }

    static class CountThread extends Thread {
        @Override
        public void run() {
            if (flag) {
                System.out.println(Thread.currentThread().getName() + " value的值是：" + value + ", time:" + LocalDateTime.now());
                System.out.println(Thread.currentThread().getName() + " CountThread flag is true,  time:" + LocalDateTime.now());
            } else {
                System.out.println(Thread.currentThread().getName() + " value的值是：" + value + ", time:" + LocalDateTime.now());
                System.out.println(Thread.currentThread().getName() + " CountThread flag is false, time:" + LocalDateTime.now());
            }
        }
    }
}
