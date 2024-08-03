package com.zifang.util.zex.source;

public class ThreadLocalTest {

    //共享的 ThreadLocal类，里面包裹着线程访问的值
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public void setThreadLocal(String value) {
        threadLocal.set(value);
    }

    // 打印当前的ThreadLocal包裹的数据
    public void getThreadLocal() {
        System.out.println(threadLocal.get());
    }

    public static void main(String[] args) {

        ThreadLocalTest test = new ThreadLocalTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.setThreadLocal("1");
                test.getThreadLocal();
            }
        },"t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.setThreadLocal("2");
                test.getThreadLocal();
            }
        },"t2").start();
    }
}