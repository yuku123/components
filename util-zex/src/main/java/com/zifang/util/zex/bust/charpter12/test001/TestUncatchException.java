package com.zifang.util.zex.bust.charpter12.test001;

import org.junit.Test;

public class TestUncatchException {

    @Test
    public void test001() {
        try {
            Thread test = new Thread(() -> {
                throw new RuntimeException("run time exception");
            });
            Thread.UncaughtExceptionHandler ss = new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println(t.getName() + ": " + e.getMessage());
                    throw new RuntimeException();
                }
            };
            // 设置线程默认的异常捕获方法
            test.setUncaughtExceptionHandler(ss);
            test.start();
        } catch (Exception e) {
            System.out.println("catch thread exception");
        }
    }
}