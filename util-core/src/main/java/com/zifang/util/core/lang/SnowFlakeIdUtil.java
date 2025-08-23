package com.zifang.util.core.lang;


/**
 * @author: zifang
 * @time: 2021-05-13 10:14:00
 * @description: snowFlake id util
 * @version: JDK 1.8
 */
public class SnowFlakeIdUtil {

    private static final SnowFlakeIdWorker SNOWFLAKE_ID_WORKER = SnowFlakeIdWorker.getInstance();

    public static long nextId() {
        return SNOWFLAKE_ID_WORKER.nextId();
    }

    public static long nextIdByCacheWhenClockMoved() {
        return SNOWFLAKE_ID_WORKER.nextIdByCacheWhenClockMoved();
    }

    public static String stringNextId() {
        return String.valueOf(nextId());
    }

    public static String stringNextIdByCacheWhenClockMoved() {
        return String.valueOf(nextIdByCacheWhenClockMoved());
    }

}
