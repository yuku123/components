package com.zifang.util.core.lang.primitive;

import com.zifang.util.core.lang.validator.Conditions;

/**
 * @author zifang
 */
public class Defaults {

    private static final Double DOUBLE_DEFAULT = 0d;
    private static final Float FLOAT_DEFAULT = 0f;

    /**
     * 获得默认的值
     */
    public static <T> T defaultValue(Class<T> type) {
        assert !Conditions.IS_NULL.test(type);
        if (type == boolean.class) {
            return (T) Boolean.FALSE;
        } else if (type == char.class) {
            return (T) Character.valueOf('\0');
        } else if (type == byte.class) {
            return (T) Byte.valueOf((byte) 0);
        } else if (type == short.class) {
            return (T) Short.valueOf((short) 0);
        } else if (type == int.class) {
            return (T) Integer.valueOf(0);
        } else if (type == long.class) {
            return (T) Long.valueOf(0L);
        } else if (type == float.class) {
            return (T) FLOAT_DEFAULT;
        } else if (type == double.class) {
            return (T) DOUBLE_DEFAULT;
        } else {
            try {
                return (T) type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}