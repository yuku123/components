package com.zifang.util.ct.generater;


import com.zifang.util.core.lang.classinfo.ClassInfo;

/**
 * 标准化 java类信息配置(网状结构的类信息承载体) -> 生成真正的java代码
 *
 * in : 组件中心配置结果(从db扣出来的)
 * out : gitlab端能吃的结果物(可以直接调用他们的gitlab做批量提交)
 *
 * */
public class JavaSourceGeneratorDispatcher{

    public static String generateCodeByClassInfo(ClassInfo classInfo){
        if(classInfo.isInterface()){

            //return new JavaInterfaceSourceGenerator(classInfo).generateCode();

        } else {

            return new JavaObjectSourceGenerator(classInfo).generateCode();

        }
        return null;
    }


}
