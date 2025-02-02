package com.zifang.util.zex.bust.charpter12;

import java.util.concurrent.Phaser;

public class PhaserTest {

    public static void main(String[] args) {

        Phaser phaser = new Phaser(2) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("=======phase: " + phase + " finished=============");
                return super.onAdvance(phase, registeredParties);
            }
        };

        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                for (int j = 0; j < 4; j++) {
                    System.out.println(String.format("%s: phase: %d", Thread.currentThread().getName(), j));
                    phaser.arriveAndAwaitAdvance();
                }
            }, "Thread " + i).start();
        }
    }
}
