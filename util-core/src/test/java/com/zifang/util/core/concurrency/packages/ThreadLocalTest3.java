package com.zifang.util.core.concurrency.packages;

public class ThreadLocalTest3 {
    ThreadLocal<String> a = new ThreadLocal<>();

    public void a() {
        a.set("dsadadasdas");
    }

    public void b() {
        System.out.println(a.get());
    }

    public static void main(String[] args) {
        ThreadLocalTest3 threadLocalTest3 = new ThreadLocalTest3();
        threadLocalTest3.a();
        threadLocalTest3.b();
    }
}
