package com.zifang.util.workflow.config;

import com.zifang.util.bigdata.spark.context.SparkConfiguration;

public class Engine {
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public SparkConfiguration getProperties() {
        return properties;
    }

    public void setProperties(SparkConfiguration properties) {
        this.properties = properties;
    }

    private String mode;
    private SparkConfiguration properties;
}
