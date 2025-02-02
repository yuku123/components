package com.zifang.util.zex.bust.chapter11;

public class Singleton1_3 {

    private static Singleton1_3 singleton = null;

    public int f1 = 1; // 触发部分初始化问题

    public int f2 = 2;
    private Singleton1_3() {}

    public static Singleton1_3 getInstance() {

        if (singleton == null) {
            synchronized (Singleton1_3.class) {
                // must be a complete instance
                if (singleton == null) {
                    singleton = new Singleton1_3();
                }
            }
        }
        return singleton;
    }
}