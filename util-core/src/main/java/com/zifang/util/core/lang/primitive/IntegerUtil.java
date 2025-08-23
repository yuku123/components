package com.zifang.util.core.lang.primitive;

/**
 * @author: zifang
 * @time: 2021-12-02 12:56:00
 * @description: integer util
 * @version: JDK 1.8
 */
public class IntegerUtil {

    public static Integer parseInteger(Object object) {
        if (null == object) {
            return null;
        }
        return Integer.parseInt(object.toString());
    }

    public static Integer parseIntegerOrDefault(Object object, Integer defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return Integer.parseInt(object.toString());
    }

    public static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return value < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) value;
        }
    }

}
