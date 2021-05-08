package com.zifang.util.core.util.concurrency.package1;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class DaemonDemo {
    public static void main(String[] args) {
        // 创建使用 Deque 类的queue 来保存事件。
        Deque<Event> deque = new ArrayDeque<Event>();
        // 创建 和开始3个 WriterTask 线程和一个 CleanerTask.
        WriterTask writer = new WriterTask(deque);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(writer);
            thread.start();
        }
        CleanerTask cleaner = new CleanerTask(deque);
        cleaner.start();
    }
}

class WriterTask implements Runnable {
    // 声明queue，储存事件并实现类的构造函数，初始化queue。
    private Deque<Event> deque;

    public WriterTask(Deque<Event> deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        for (int i = 1; i < 100; i++) {
            Event event = new Event();
            event.setDate(new Date());
            event.setEvent(String.format("The thread %s has generated an   event", Thread.currentThread().getId()));
            deque.addFirst(event);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// 创建 CleanerTask 类并一定扩展Thread类。
class CleanerTask extends Thread {
    // 声明 queue，储存事件并实现类的构造函数，初始化queue，在这个构造函数，用setDaemon() 方法让此线程成为守护线程。
    private Deque<Event> deque;

    public CleanerTask(Deque<Event> deque) {
        this.deque = deque;
        setDaemon(true);
    }

    // 实现run()方法。它是无限循环来获取当前日期并调用 clean() 方法.
    @Override
    public void run() {
        while (true) {
            Date date = new Date();
            clean(date);
        }
    }

    // 实现 clean() 方法.
    // 它获取最后的事件，如果它在10秒前被创建，就删除它并查看下一个事件。如果一个事件被删除，它会写一个事件信息和queue的新的大小，为了让你看到变化过程。
    private void clean(Date date) {
        long difference;
        boolean delete;
        if (deque.size() == 0) {
            return;
        }
        delete = false;
        do {
            Event e = deque.getLast();
            difference = date.getTime() - e.getDate().getTime();
            if (difference > 10000) {
                System.out.printf("Cleaner: %s\n", e.getEvent());
                deque.removeLast();
                delete = true;
            }
        } while (difference > 10000);
        if (delete) {
            System.out.printf("Cleaner: Size of the queue: %d\n", deque.size());
        }
    }
}

class Event {
    private Date date;
    private String event;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}