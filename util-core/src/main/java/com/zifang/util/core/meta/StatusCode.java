package com.zifang.util.core.meta;


/**
 * 状态码接口
 *
 * @author zhixiang.lwt
 */
public interface StatusCode {

    /**
     * 状态码标示
     *
     * @return
     */
    int getCode();

    /**
     * 错误信息
     *
     * @return
     */
    String getMessage();
}
