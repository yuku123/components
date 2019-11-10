package com.zifang.util.workflow.config;

import lombok.Data;

import java.util.Map;

@Data
public class Engine {

    /**
     * 表明是什么类型的引擎
     * */
    private String type;

    /**
     * 表明在某个引擎下的种类类型
     * */
    private String mode;

    /**
     * 表明这个种类类型的引擎所需要的所有参数
     * */
    private Map<String,String> properties;

}
