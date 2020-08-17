package com.zifang.util.source.generator.info;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MethodInfo {

    /**
     * 返回参数类型
     * */
    private String returnType;

    /**
     * 方法名字
     * */
    private String methodName;

    /**
     * 形参列表
     */
    private List<MethodParameterPair> methodParameterPairs = new ArrayList<>();

    /**
     * 方法体的语句
     * */
    private List<String> statements;

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

    /**
     * 当前方法的
     * */
    public String signature(){
        return returnType+" "+methodName+"("+getParameterStr()+")"+";";

    }

    private String getParameterStr(){
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
