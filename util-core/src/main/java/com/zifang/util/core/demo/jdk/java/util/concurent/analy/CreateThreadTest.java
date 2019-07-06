package com.zifang.util.core.demo.jdk.java.util.concurent.analy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CreateThreadTest {
    public static void main(String[] args) {
//        createByThread();
//        createByRunable();
        createByCallable();
    }

    private static void createByCallable() {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                //模拟一段很耗时的操作
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "callable的返回值";
            }
        };

        FutureTask<String> futureTask = new FutureTask<String>(callable);
        new Thread(futureTask).start();

        try {
            System.out.println("------------------------------------");
            String taskReturn = futureTask.get();  //FutureTask 可用于 闭锁 类似于CountDownLatch的作用，在所有的线程没有执行完成之后这里是不会执行的
            System.out.println(taskReturn);
            System.out.println("------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    private static void createByRunable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Runable创造的线程在执行哦~");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void createByThread() {
        MyThread myThread = new MyThread();
        myThread.start();
    }
}

class MyThread extends Thread{
    @Override
    public void run(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("MyThread线程在执行哦~");
    }
}