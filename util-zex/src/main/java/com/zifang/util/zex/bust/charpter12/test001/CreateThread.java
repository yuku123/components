package com.zifang.util.zex.bust.charpter12.test001;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CreateThread {
    @Test
    public void test0() throws InterruptedException {
        System.out.println("开始运行整体_" + System.currentTimeMillis());
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("线程开始运行_" + System.currentTimeMillis());
                try {
                    System.out.println("线程开始睡眠_" + System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println("线程结束睡眠_" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程执行结束_" + System.currentTimeMillis());
            }
        };
        thread.start();
        System.out.println("main开始睡眠_" + System.currentTimeMillis());
        Thread.sleep(2000);
        System.out.println("main结束睡眠_" + System.currentTimeMillis());
    }

    @Test
    public void test1() throws InterruptedException {
        System.out.println("开始运行整体_" + System.currentTimeMillis());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程开始运行_" + System.currentTimeMillis());
                try {
                    System.out.println("线程开始睡眠_" + System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println("线程结束睡眠_" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程执行结束_" + System.currentTimeMillis());
            }
        };
        new Thread(runnable).start();
        System.out.println("main开始睡眠_" + System.currentTimeMillis());
        Thread.sleep(2000);
        System.out.println("main结束睡眠_" + System.currentTimeMillis());
    }

    @Test
    public void test000_001() throws InterruptedException {
        System.out.println("开始运行整体_" + System.currentTimeMillis());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程开始运行_" + System.currentTimeMillis());
                try {
                    System.out.println("线程开始睡眠_" + System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println("线程结束睡眠_" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程执行结束_" + System.currentTimeMillis());
            }
        };
        new Thread(runnable){
            @Override
            public void run() {
                System.out.println("吃饭吃饭吃饭吃饭");
            }
        }.start();
        System.out.println("main开始睡眠_" + System.currentTimeMillis());
        Thread.sleep(2000);
        System.out.println("main结束睡眠_" + System.currentTimeMillis());
    }

    @Test
    public void test2() throws InterruptedException, ExecutionException {
        System.out.println("开始运行整体_" + System.currentTimeMillis());
        FutureTask<Long> futureTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() {
                System.out.println("线程开始运行_" + System.currentTimeMillis());
                try {
                    System.out.println("线程开始睡眠_" + System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println("线程结束睡眠_" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程执行结束_" + System.currentTimeMillis());
                return System.currentTimeMillis();
            }
        }
        );
        new Thread(futureTask).start();
        System.out.println("main开始睡眠_" + System.currentTimeMillis());
        Thread.sleep(2000);
        System.out.println("main结束睡眠_" + System.currentTimeMillis());
        System.out.println("futureTask计算得到的数据为："+futureTask.get());
    }

    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true){
                    System.out.print("");
                }
            }
        };

        thread.start();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.getName());
        thread.setName("吃饭");
        System.out.println(thread.getName());
    }
}
