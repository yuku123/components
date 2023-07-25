package com.zifang.util.zex.bust.charpter12;

public class VolatileExample {
    public static boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            int i = 0;
            while (!stop) {
                i++;
            }
        });
        t1.start();
        System.out.println("begin start thread");
        Thread.sleep(1000);
        stop = true;
    }
}