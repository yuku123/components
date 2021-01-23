package com.zifang.util.core.time;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskTest {
    public static void main(String[] args) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    System.out.println(new Date().toLocaleString());
                }
            };
            timer.schedule(timerTask, 1000, 1000);
            // timerTask.run();
        }
}
