package com.zifang.util.core.beans.classinfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 形参列表
 * */
@Data
@AllArgsConstructor
public class MethodParameterPair {

    // 形参种类 class的type
    private String paramType;

    // 形参的名字
    private String paramName;

    @Override
    public String toString(){
        return paramType+" "+paramName;
    }
}
