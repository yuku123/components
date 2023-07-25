package com.zifang.util.zex.bust.chapter3;

public class ControlLoopBreakTest {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            System.out.println("当前i值为" + i);
            if (i == 2) {
                break;
            }
        }
    }
}
