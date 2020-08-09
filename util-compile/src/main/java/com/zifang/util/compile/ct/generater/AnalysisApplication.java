package com.zifang.util.compile.ct.generater;

/**
 * 静态分析源码包
 * */
public class AnalysisApplication {
    public static void main(String[] args) {
        String packageName = "com.zifang.util.jvm.management";

        AnalysisContext analysisContext = new AnalysisContext(packageName);
        //analysisContext.setFilterChain();过滤链条
        analysisContext.analysis();// 对当前的包做所有的分析

    }
}
