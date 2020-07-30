package com.zifang.util.core.lang.classinfo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MethodInfo {

    // 返回参数类型
    private String returnType;

    // 方法名字
    private String methodName;

    // 形参列表
    private List<MethodParameterPair> methodParameterPairs = new ArrayList<>();

    // 返回的字符串
    private String returnStr;

    @Tolerate
    public MethodInfo(){

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MethodInfo){
            MethodInfo tobeCompare = (MethodInfo)obj;
            if(returnType.equals(tobeCompare.getReturnType()) && methodName.equals(tobeCompare.getMethodName() )){
                if(methodParameterPairs.toString().equals(tobeCompare.getMethodParameterPairs().toString())){
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

    public void setReturnStr(String returnStr) {
        this.returnStr = returnStr;
    }

    @Override
    public String toString(){
        return returnType+" "+methodName+"("+toParameterString()+")"+";";
    }

    private String toParameterString(){
        List<String> sub = new ArrayList<>();
        if(methodParameterPairs == null){
            return "";
        }else{
            for(MethodParameterPair methodParameterPair : methodParameterPairs){
                sub.add(methodParameterPair.toString());
            }
            return String.join(",",sub);
        }
    }
}
