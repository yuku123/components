package com.zifang.util.zex;

import java.time.LocalDateTime;

public class C {
    int a = 5;
    char c = 'a';
    boolean b = true;
    float f = 5.12f;
    double d = 4.12;

//    public static void main(String[] args) {
//        int s = 0b0000_0000_0111_1101 & (1 << 4);
//        System.out.println(s);
//    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.of(1996,8,1,0,0,0).plusDays(10000));
    }

}
