package com.zifang.util.workflow.engine;

import com.zifang.util.bigdata.spark.context.SparkConfiguration;
import com.zifang.util.workflow.config.Engine;

import java.util.HashMap;
import java.util.Map;

public class EngineFactory {
    public static Map<String, AbstractEngine> registeredEngineMap= new HashMap<String,AbstractEngine>(){
        {
            put("engine.mode.spark.local",new SparkEngine());
            put("engine.mode.spark.cluster",new SparkEngine());
        }
    };

    public static AbstractEngine getEngine(Engine engine) {
        String mode = engine.getMode();
        SparkConfiguration sparkConfiguration = engine.getProperties();

        return registeredEngineMap.get(engine);
    }
}
