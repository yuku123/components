package com.zifang.util.source.generator.info;

/**
 * 形参列表
 */
public class MethodParameterPair {

    /**
     * 形参种类 class的type
     */
    private String paramType;

    /**
     * 形参的名字
     */
    private String paramName;


    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String toString() {
        return paramType + " " + paramName;
    }
}
