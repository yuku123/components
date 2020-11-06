package com.zifang.util.monitor.thread.alarm;

import com.zifang.util.monitor.thread.Status;
import com.zifang.util.monitor.thread.ThreadPoolStatus;
import com.zifang.util.monitor.thread.utility.TimeUtil;

/**
 * 线程池任务超时告警策略。当最后一个启动的任务，超过alarmThreshold没有结束时，此策略认为该监控组件状态异常，需告警。
 * @author lijing
 * @since 2015/11/26
 */
public class ThreadPoolOvertimeAlarmPolicy extends AlarmPolicy {

    /**
     * 告警阀值，单位为毫秒。
     */
    private int alarmThreshold = 2 * TimeUtil.MINUTES_SECONDS * TimeUtil.SECOND_MILLISECONDS;

    public ThreadPoolOvertimeAlarmPolicy() {
    }

    public ThreadPoolOvertimeAlarmPolicy(int alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }

    @Override
    public boolean needAlarm(Status status) {
        ThreadPoolStatus threadPoolStatus = (ThreadPoolStatus) status;
        //当最后一个启动的任务超过10分钟没有结束时，该线程池已经异常。
        return threadPoolStatus.getLastFinishTime().get() < threadPoolStatus.getLastStartTime().get()
                && (TimeUtil.getMillisTimestamp() - threadPoolStatus.getLastStartTime().get()) >= alarmThreshold;
    }
}
