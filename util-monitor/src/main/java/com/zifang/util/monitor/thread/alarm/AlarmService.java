package com.zifang.util.monitor.thread.alarm;

/**
 * 告警服务接口。
 * @author lijing
 * @since 2015/12/1
 */
public interface AlarmService {

    /**
     * 告警接口。
     * @param arg 参数列表，各个告警服务根据自己的需求设计参数。
     */
    void alarm(Object... arg);
}
