package com.zifang.util.core.lang.primitive;

/**
 * @author: zifang
 * @time: 2021-12-05 21:03:00
 * @description: float util
 * @version: JDK 1.8
 */
public class FloatUtil {

    public static Float parseFloat(Object object) {
        if (null == object) {
            return null;
        }
        return Float.parseFloat(object.toString());
    }

    public static Float parseFloatOrDefault(Object object, Float defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return Float.parseFloat(object.toString());
    }

}
