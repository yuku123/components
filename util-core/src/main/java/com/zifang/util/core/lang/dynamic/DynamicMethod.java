package com.zifang.util.core.lang.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zifang
 */
public class DynamicMethod {

    private String methodName;

    private String returnType;

    private String desc;

    private String body;

    private List<DynamicParameter> parameters = new ArrayList<>();
    public static DynamicMethod of(String methodName, List<DynamicParameter> parameters, String returnType, String body) {
        DynamicMethod dynamicMethod = new DynamicMethod();
        dynamicMethod.setMethodName(methodName);
        dynamicMethod.setParameters(parameters);
        dynamicMethod.setReturnType(returnType);
        dynamicMethod.setBody(body);
        return dynamicMethod;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<DynamicParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<DynamicParameter> parameters) {
        this.parameters = parameters;
    }
}
