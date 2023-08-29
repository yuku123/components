package com.zifang.util.core.util;


import com.zifang.util.core.lang.exception.BaseException;

/**
 * 断言 的工具类
 */
public class Assert {

    /**
     * 创建异常
     *
     * @param args
     * @return
     */
    BaseException newException(Object... args) {
        return null;
    }

    /**
     * 创建异常
     *
     * @param t
     * @param args
     * @return
     */
    BaseException newException(Throwable t, Object... args) {
        return null;
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    public void assertNotNull(Object obj) {
        if (obj == null) {
            throw newException(obj);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj  待判断对象
     * @param args message占位符对应的参数列表
     */
    public void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (Conditions.IS_NULL.test(object)) {
            throw new IllegalArgumentException(message);
        }
    }
}
