package com.zifang.util.zex.bust.charpter12;

public class NoVisibility {

    private static int n1 = 0;

    private static int number;

    private static void test1() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                n1 = 1;
            }
        }.start();

        while (n1 == 0) {
        }
    }

    public static void main(String[] args) {

        test1();

    }

}