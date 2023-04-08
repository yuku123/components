package com.zifang.util.zex.bust.chapter2;

public class ASCII {
    public static void main(String[] args) {
        for(char i = 0; i < 256; i++){
            System.out.println((int)i+"->"+Integer.toBinaryString(i)+"->"+i);
        }
    }
}
