package com.zifang.util.monitor.thread;

import com.zifang.util.monitor.thread.alarm.Alarmable;

/**
 * 可监控组件的接口。
 *
 * @author lijing
 * @since 2015/11/24
 */
public interface Monitorable extends Alarmable {

    /**
     * 获取该监控组件的状态。
     *
     * @return 状态对象。
     */
    Status status();

    /**
     * 获取组件名称。
     *
     * @return 该组件的名称。
     */
    String componentName();
}
