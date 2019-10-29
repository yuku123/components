package com.zifang.util.workflow.config;

import java.util.Map;

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
