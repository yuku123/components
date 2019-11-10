package com.zifang.util.workflow.config;

import lombok.Data;

import java.util.Map;

@Data
public class Configurations {

    /**
     * 全局配置的引擎配置
     * */
    private Engine engine;

    /**
     * 运行时参数设置
     * */
    private Map<String,String> runtimeParameter;

}
