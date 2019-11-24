package com.zifang.util.zex.demo.jdk.util.concurent.packages.executor.threadpool;

public class Client {
    public static void main(String[] args) throws Exception {
        ThreadPool pool =new ThreadPool(5);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    System.out.println("执行耗时任务1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    System.out.println("执行耗时任务2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        System.out.println("End");
        pool.destroy();
    }

}
