package com.zifang.util.core.lang.primitive;

/**
 * @author: zifang
 * @time: 2021-12-02 12:57:00
 * @description: long util
 * @version: JDK 1.8
 */
public class LongUtil {

    public static Long parseLong(Object object) {
        if (null == object) {
            return null;
        }
        return Long.parseLong(object.toString());
    }

    public static Long parseLongOrDefault(Object object, Long defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return Long.parseLong(object.toString());
    }

    public static int length(Long value) {
        if (null == value) {
            return 0;
        }
        return String.valueOf(value).length();
    }

}
