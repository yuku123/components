package com.zifang.util.core.lang.validator;


import com.zifang.util.core.lang.exception.BaseException;

/**
 * 断言 的工具类
 */
public class AssertUtil {

    BaseException newException(Object... args) {
        return null;
    }

    BaseException newException(Throwable t, Object... args) {
        return null;
    }

    public void notNull(Object obj) {
        if (obj == null) {
            throw newException(obj);
        }
    }

    public void notNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    public static void notNull(Object object, String message) {
        if (Conditions.IS_NULL.test(object)) {
            throw new IllegalArgumentException(message);
        }
    }
}
