package com.zifang.util.workflow.config;

import java.util.Map;

public class Configurations {


    private Engine engine;

    private Map<String,String> runtimeParameter;

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Map<String, String> getRuntimeParameter() {
        return runtimeParameter;
    }

    public void setRuntimeParameter(Map<String, String> runtimeParameter) {
        this.runtimeParameter = runtimeParameter;
    }

}
