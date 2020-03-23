package com.zifang.util.web.dto;

import com.zifang.util.web.ResultCode;
import lombok.Data;

@Data
public class CommonResponse {

    boolean success;
    int code;
    String msg;

    public CommonResponse(ResultCode resultCode){
        success = resultCode.success();
        code = resultCode.code();
        msg = resultCode.message();
    }

}
