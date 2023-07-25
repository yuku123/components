package com.zifang.util.zex.bust.chapter3;

public class LoopMarkTest {
    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            outer:
            for (int j = 0; j < 3; j++) {
                for (int z = 0; z < 3; z++) {
                    System.out.println("i:" + i + "|j:" + j + "|z:" + z);
                    if (z == 1) {
                        break outer;
                    }
                }
            }
        }
    }
}
