package com.zifang.util.workflow.config;

import java.util.Map;

public class Configurations {

    private String engine;

    private Map<String,String> properties;

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
