package com.zifang.util.core.validate;

public class StringValidator {


    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return boolean
     */
    public static boolean isEmptyString(String string){
        return string == null || string.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param string
     * @return boolean
     */
    public static boolean isNotEmptyString(String string){
        return string != null && string.length() > 0;
    }

}
