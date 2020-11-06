package com.zifang.util.monitor.thread.executor;

import com.zifang.util.monitor.thread.alarm.AlarmPolicy;
import com.zifang.util.monitor.thread.alarm.AlarmService;
import com.zifang.util.monitor.thread.utility.TimeUtil;

/**
 * 线程池配置。
 * @author lijing
 * @since 2015/11/25
 */
public class ThreadPoolConfigUnit {

    /**
     * 线程池名称。
     */
    private String poolName;

    /**
     * 线程池大小。
     */
    private int poolSize;

    /**
     * 任务类型。
     */
    private Class taskType;

    /**
     * 计算类型，包括CPU密集型和IO密集型。
     */
    private byte computeType;

    /**
     * 线程超时阀值，单位是毫秒。当当前毫秒时间戳 - 线程启动毫秒时间戳 >= threadOvertimeThreshhold时，认为此线程已超时。
     * 监控管理器会在最多monitorInterval的时间之后，设置该工作线程的中断标志位。
     */
    private long threadOvertimeThreshhold = 3 * TimeUtil.MINUTES_SECONDS * TimeUtil.SECOND_MILLISECONDS;

    /**
     * 告警策略，决定何时将监控组件标记为告警状态。
     */
    private AlarmPolicy alarmPolicy;

    /**
     * 告警服务，对告警状态的监控组件进行告警。
     */
    private AlarmService alarmService;

    /**
     * 监控间隔，默认为10秒。
     */
    private long monitorInterval = 10 * TimeUtil.SECOND_MILLISECONDS;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public byte getComputeType() {
        return computeType;
    }

    public void setComputeType(byte computeType) {
        this.computeType = computeType;
    }

    public Class getTaskType() {
        return taskType;
    }

    public void setTaskType(Class taskType) {
        this.taskType = taskType;
    }

    public long getThreadOvertimeThreshhold() {
        return threadOvertimeThreshhold;
    }

    public void setThreadOvertimeThreshhold(long threadOvertimeThreshhold) {
        this.threadOvertimeThreshhold = threadOvertimeThreshhold;
    }

    public AlarmPolicy getAlarmPolicy() {
        return alarmPolicy;
    }

    public AlarmService getAlarmService() {
        return alarmService;
    }

    /**
     * 设置告警策略。
     * @param alarmPolicy 待设置告警策略。
     */
    public void setAlarmPolicy(AlarmPolicy alarmPolicy) {
        this.alarmPolicy = alarmPolicy;
    }

    /**
     * 设置告警服务。
     * @param alarmService 待设置告警服务。
     */
    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    public long getMonitorInterval() {
        return monitorInterval;
    }

    public void setMonitorInterval(long monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    @Override
    public String toString() {
        return "ThreadPoolConfigUnit{" +
                "poolName='" + poolName + '\'' +
                ", poolSize=" + poolSize +
                ", taskType=" + taskType +
                ", computeType=" + computeType +
                ", threadOvertimeThreshhold=" + threadOvertimeThreshhold +
                ", alarmPolicy=" + alarmPolicy +
                ", alarmService=" + alarmService +
                ", monitorInterval=" + monitorInterval +
                '}';
    }
}
