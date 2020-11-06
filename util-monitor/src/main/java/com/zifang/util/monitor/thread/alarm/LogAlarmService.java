package com.zifang.util.monitor.thread.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志打印告警服务。
 * @author lijing
 * @since 2015/11/25
 */
public class LogAlarmService implements AlarmService {

    /**
     * 日志对象。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAlarmService.class);

    @Override
    public void alarm(Object...arg) {
        if (arg == null || arg.length <= 0) {
            return;
        }
        LOGGER.info("{} alarm. Monitorable Compoment detail:", this.getClass().getSimpleName());
        for (Object object : arg) {
            LOGGER.info(object.toString());
        }
        LOGGER.info("{} alarm finish.", this.getClass().getSimpleName());
    }
}
