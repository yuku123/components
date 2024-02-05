package com.zifang.util.core.lang.validator;


import com.zifang.util.core.meta.StatusCode;
import com.zifang.util.core.lang.exception.BusinessException;
import com.zifang.util.core.lang.exception.ParamValidateStatusCode;
import com.zifang.util.core.lang.StringUtil;

public class Validator {

    /**
     * 防御，为true成功
     *
     * @param isTrue 是否成功
     * @param msg    错误消息
     * @param param  入参
     */
    public static void defenseIfTrue(boolean isTrue, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, isTrue, msg, param);
    }

    /**
     * 防御，为false成功
     *
     * @param isNotTrue 是否不成功
     * @param msg       错误消息
     * @param param     入参
     */
    public static void defenseIfNotTrue(boolean isNotTrue, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, !isNotTrue, msg, param);
    }

    /**
     * 直接防御
     *
     * @param msg 错误消息
     */
    public static void defenseDirectly(String msg) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, true, msg, null);
    }


    /**
     * 入参不能为空 为空抛出参数错误异常
     *
     * @param parameter 入参
     * @param msg       错误信息
     */
    public static void requireNonNull(Object parameter, String msg) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, parameter == null, msg, null);
    }


    /**
     * 入参不能为空 为空抛出参数错误异常
     *
     * @param parameter 入参
     * @param msg       错误信息
     * @param param     嵌套了parameter的外层参数
     */
    public static void requireNonNull(Object parameter, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, parameter == null, msg, param);
    }

    /**
     * 需要参数为空
     *
     * @param parameter
     * @param msg
     * @param param
     */
    public static void requireIsNull(Object parameter, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, parameter != null, msg, param);
    }


    /**
     * 防御，为true成功
     *
     * @param statusCode 错误状态码
     * @param isTrue     是否成功
     * @param msg        错误消息
     * @param param      入参
     */
    private static void defenseIfTrue(StatusCode statusCode, boolean isTrue, String msg, Object param) {
        // 防御成功，返回
        if (!isTrue) {
            return;
        }
        // 优化提示，直接将错误信息给出
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(statusCode, msg);
        } else {
            throw new BusinessException(statusCode);
        }
    }
}
