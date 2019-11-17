package com.zifang.util.workflow.config;

import lombok.Data;

import java.util.Map;

@Data
public class Configurations {

    /**
     * 全局配置的执行引擎配置
     * */
    private Engine engine;

    /**
     * 配置的作为每步缓存的执行引擎
     * */
    private CacheEngine cacheEngine;

    /**
     * 为每个操作者提供专属的参数
     * */
    private Map<String,String> personalEnvironment;

    /**
     * 运行时参数设置
     * */
    private Map<String,String> runtimeParameter;

}
