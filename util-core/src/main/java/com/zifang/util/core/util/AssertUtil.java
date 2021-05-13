package com.zifang.util.core.util;


/**
 * 断言 的工具类
 */
public class AssertUtil {

    /**
     * 判断对象是否为空
     *
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (Condition.IS_NULL.test(object)) {
            throw new IllegalArgumentException(message);
        }
    }
}
