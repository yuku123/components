package com.zifang.util.zex.bust.charpter12;

public class RecordExample1 {

    public static void main(String[] args) {

        new Thread(() -> {
            while (true) {
                int a = 1;
                int b = 2;

                try {
                    a = 3;           // A
                    b = 1 / 0;       // B
                } catch (Exception e) {

                } finally {
                    if (a == 2) {
                        System.out.println("a = " + a);
                    }
                }
            }
        }).start();

    }
}