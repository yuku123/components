package com.zifang.util.zex.bust.chapter3;

public class ControlLoopContinueTest {
    public static void main(String[] args) {
        for(int i = 0; i < 4;i++){
            if(i == 2){
                continue;
            }
            System.out.println("当前i值为"+i);
        }
    }
}
