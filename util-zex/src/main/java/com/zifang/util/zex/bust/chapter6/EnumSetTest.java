package com.zifang.util.zex.bust.chapter6;

public class EnumSetTest {
    public static void main(String[] args) {

        System.out.println(Long.toBinaryString(-1));

        int from = 1;
        int to = 3;

        System.out.println(Long.toBinaryString(-1L >>>  (from - to - 1)));
        System.out.println(Long.toBinaryString((-1L >>>  (from - to - 1) << from)));


    }
}
