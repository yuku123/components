package com.zifang.util.core.lang.primitive;

/**
 * @author: zifang
 * @time: 2021-12-05 21:03:00
 * @description: double util
 * @version: JDK 1.8
 */
public class DoubleUtil {

    public static Double parseDouble(Object object) {
        if (null == object) {
            return null;
        }
        return Double.parseDouble(object.toString());
    }

    public static Double parseDoubleOrDefault(Object object, Double defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return Double.parseDouble(object.toString());
    }

}
