package com.zifang.util.monitor.thread.alarm;

import com.zifang.util.monitor.thread.Status;

/**
 * @author lijing
 * @since 2015/11/26
 */
public abstract class AlarmPolicy {

    /**
     * 根据传入的status，决定是否需要告警。
     *
     * @param status 待判断的状态体。
     * @return 如果需要告警，返回true，否则返回false。
     */
    public abstract boolean needAlarm(Status status);
}
