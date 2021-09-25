package com.zifang.util.zex;

public class C {
    int a = 5;
    char c = 'a';
    boolean b = true;
    float f = 5.12f;
    double d = 4.12;

    public static void main(String[] args) {
        int s = 0b0000_0000_0111_1101 & (1 << 4);
        System.out.println(s);
    }
}
