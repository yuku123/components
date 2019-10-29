package com.zifang.util.workflow.engine;

import java.util.HashMap;
import java.util.Map;

public class EngineFactory {
    public static Map<String, AbstractEngine> registeredEngineMap= new HashMap<String,AbstractEngine>(){
        {
            put("engine.mode.spark.local",new SparkEngine());
        }
    };

    public static AbstractEngine getEngine(String engine) {
        return registeredEngineMap.get(engine);
    }
}
