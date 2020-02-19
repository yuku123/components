package com.zifang.util.core.validate;

/**
 * 数组相关的验证器
 * */
public class ArrayValidator {

    /**
     * 判断数组是否 为空
     *
     * @param array
     * @return boolean
     */
    private static <T> boolean isEmptyArray(T[] array){
        return array == null || array.length == 0;
    }

    /**
     *  判断数组是否 不为空
     *
     * @param array
     * @return boolean
     */
    public static <T> boolean isNotEmptyArray(T[] array){
        return array != null && array.length > 0;
    }

}
