package com.zifang.util.zex.source;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行："+System.currentTimeMillis());
            }
        }, 1000, 1000);
        System.out.println("开始执行："+System.currentTimeMillis());
    }
}
