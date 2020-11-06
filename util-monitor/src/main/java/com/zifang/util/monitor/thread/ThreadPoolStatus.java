package com.zifang.util.monitor.thread;

import com.zifang.util.monitor.thread.utility.TimeUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池状态。
 * @author lijing
 * @since 2015/11/24
 */
public class ThreadPoolStatus extends Status {

    /**
     * 线程池启动时间。
     */
    protected long startTime = TimeUtil.getMillisTimestamp();

    /**
     * 提交任务数。
     */
    protected AtomicInteger submitCount = new AtomicInteger();

    /**
     * 启动任务数。
     */
    protected AtomicInteger startCount = new AtomicInteger();

    /**
     * 成功执行任务数。
     */
    protected AtomicInteger sucessCount = new AtomicInteger();

    /**
     * 失败任务数。
     */
    protected AtomicInteger failCount = new AtomicInteger();

    /**
     * 上次启动时间。
     */
    protected AtomicLong lastStartTime = new AtomicLong();

    /**
     * 上次结束时间。
     */
    protected AtomicLong lastFinishTime = new AtomicLong();

    /**
     * 总执行时间。
     */
    protected AtomicLong totalTimeConsuming = new AtomicLong();

    /**
     * 告警次数。
     */
    protected AtomicInteger alarmTimes = new AtomicInteger();

    /**
     * 每一个线程的状态。
     */
    protected ConcurrentHashMap<Thread, Map<String, Object>> threadStatusMap = new ConcurrentHashMap<>();

    /**
     * 正在执行的任务状态。
     */
    protected Map<Runnable, Map<String, Object>> taskStatusMap = new ConcurrentHashMap<>();

    /**
     * 默认10秒监测一次。
     */
    protected long monitorInterval = 10 * TimeUtil.SECOND_MILLISECONDS;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public AtomicInteger getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(AtomicInteger submitCount) {
        this.submitCount = submitCount;
    }

    public AtomicInteger getStartCount() {
        return startCount;
    }

    public void setStartCount(AtomicInteger startCount) {
        this.startCount = startCount;
    }

    public AtomicInteger getSucessCount() {
        return sucessCount;
    }

    public void setSucessCount(AtomicInteger sucessCount) {
        this.sucessCount = sucessCount;
    }

    public AtomicInteger getFailCount() {
        return failCount;
    }

    public void setFailCount(AtomicInteger failCount) {
        this.failCount = failCount;
    }

    public AtomicLong getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(AtomicLong lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public AtomicLong getLastFinishTime() {
        return lastFinishTime;
    }

    public void setLastFinishTime(AtomicLong lastFinishTime) {
        this.lastFinishTime = lastFinishTime;
    }

    public AtomicLong getTotalTimeConsuming() {
        return totalTimeConsuming;
    }

    public void setTotalTimeConsuming(AtomicLong totalTimeConsuming) {
        this.totalTimeConsuming = totalTimeConsuming;
    }

    public AtomicInteger getAlarmTimes() {
        return alarmTimes;
    }

    public void setAlarmTimes(AtomicInteger alarmTimes) {
        this.alarmTimes = alarmTimes;
    }

    public ConcurrentHashMap<Thread, Map<String, Object>> getThreadStatusMap() {
        return threadStatusMap;
    }

    public void setThreadStatusMap(ConcurrentHashMap<Thread, Map<String, Object>> threadStatusMap) {
        this.threadStatusMap = threadStatusMap;
    }

    public Map<Runnable, Map<String, Object>> getTaskStatusMap() {
        return taskStatusMap;
    }

    public void setTaskStatusMap(Map<Runnable, Map<String, Object>> taskStatusMap) {
        this.taskStatusMap = taskStatusMap;
    }

    public long getMonitorInterval() {
        return monitorInterval;
    }

    public void setMonitorInterval(long monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    @Override
    public String toString() {
        return "ThreadPoolStatus{" +
                "startTime=" + startTime +
                ", submitCount=" + submitCount +
                ", startCount=" + startCount +
                ", sucessCount=" + sucessCount +
                ", failCount=" + failCount +
                ", lastStartTime=" + lastStartTime +
                ", lastFinishTime=" + lastFinishTime +
                ", totalTimeConsuming=" + totalTimeConsuming +
                ", alarmTimes=" + alarmTimes +
                ", threadStatusMap=" + threadStatusMap +
                ", taskStatusMap=" + taskStatusMap +
                ", monitorInterval=" + monitorInterval +
                '}';
    }
}
