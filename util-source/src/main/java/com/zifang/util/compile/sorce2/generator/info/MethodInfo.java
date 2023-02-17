package com.zifang.util.compile.sorce2.generator.info;

import java.util.ArrayList;
import java.util.List;


public class MethodInfo {

    /**
     * 方法的可见性
     */
    private Integer modifier;

    /**
     * 返回参数类型
     */
    private String returnType;

    /**
     * 方法名字
     */
    private String methodName;

    /**
     * 形参列表
     */
    private List<MethodParameterPair> methodParameterPairs = new ArrayList<>();

    /**
     * 方法体的语句
     */
    private List<String> statements;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodInfo) {
            MethodInfo tobeCompare = (MethodInfo) obj;
            if (returnType.equals(tobeCompare.getReturnType()) && methodName.equals(tobeCompare.getMethodName())) {
                if (methodParameterPairs.toString().equals(tobeCompare.getMethodParameterPairs().toString())) {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 当前方法的标致号 不理会modifier
     * <p>
     * String name(String cc, Double dd);
     */
    public String signature() {
        return returnType + " " + methodName + "(" + getParameterStr() + ")" + ";";
    }

    /**
     * 当前方法的标致号 理会modifier
     * <p>
     * public String name(String cc, Double dd);
     */
    public String fullSignature() {
        return returnType + " " + methodName + "(" + getParameterStr() + ")" + ";";
    }

    private String getParameterStr() {
        List<String> sub = new ArrayList<>();
        if (methodParameterPairs == null) {
            return "";
        } else {
            for (MethodParameterPair methodParameterPair : methodParameterPairs) {
                sub.add(methodParameterPair.toString());
            }
            return String.join(",", sub);
        }
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<MethodParameterPair> getMethodParameterPairs() {
        return methodParameterPairs;
    }

    public void setMethodParameterPairs(List<MethodParameterPair> methodParameterPairs) {
        this.methodParameterPairs = methodParameterPairs;
    }

    public List<String> getStatements() {
        return statements;
    }

    public void setStatements(List<String> statements) {
        this.statements = statements;
    }
}
