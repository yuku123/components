package com.zifang.util.io;

public class Hello4 {
    public static void main(String[] args) {
        new Thread(()->System.out.println("吃饭")).start();
    }
}
