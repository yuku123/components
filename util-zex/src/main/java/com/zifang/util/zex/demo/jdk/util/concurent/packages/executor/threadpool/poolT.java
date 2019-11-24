package com.zifang.util.zex.demo.jdk.util.concurent.packages.executor.threadpool;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class poolT {
    //可能频繁增删任务，链表队列效率较高
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    private final HashSet<Work> workers = new HashSet<Work>();
    private static int num = 3;

    public poolT(int num) {
        this.num = num;
        for (int i = 0; i < num; i++) {
            Work w = new Work();
            w.start();
            workers.add(w);
        }
    }

    public void addWork(Runnable r) {
        workQueue.add(r);
    }

    public void close() throws Exception {

        while (!workQueue.isEmpty()) {
            Thread.sleep(500);
        }
        for (Work work : workers) {
            // 通知正在运行的结束
            work.setDrop();
            // 强制结束还在等待的
            if (work.getState() == Thread.State.WAITING) {
                work.interrupt();
            }
        }
        Thread.sleep(2000);
        for (Work work : workers) {
            System.out.println(work.getName() + "状态:" + work.getState());
        }
    }

    // 内部线程封装
    private class Work extends Thread {
        Runnable r = null;
        // 结束线程标志位
        private boolean hasRunning = true;

        public void setDrop() {
            this.hasRunning = false;
        }

        public void run() {
            try {
                while (hasRunning || !workQueue.isEmpty()) {
                    // 阻塞线程执行
                    r = workQueue.take();
                    if (r != null) {
                        r.run();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}