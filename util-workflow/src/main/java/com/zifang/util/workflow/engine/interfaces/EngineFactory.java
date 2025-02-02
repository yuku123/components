package com.zifang.util.workflow.engine.interfaces;

import com.zifang.util.workflow.config.Engine;
import com.zifang.util.workflow.engine.java.JavaEngine;
import com.zifang.util.workflow.engine.python.PythonEngine;
import com.zifang.util.workflow.engine.spark.SparkEngine;

import java.util.HashMap;
import java.util.Map;

public class EngineFactory {

    public static Map<String, AbstractEngine> engineCache = new HashMap<>();

    //type : engine
    public static Map<String, Class<? extends AbstractEngine>> registeredEngineMap = new HashMap<String, Class<? extends AbstractEngine>>() {
        {
            put("spark", SparkEngine.class);
            put("python", PythonEngine.class);
            put("java", JavaEngine.class);
        }
    };

    public static AbstractEngine getEngine(Engine engine) {

        //得到引擎种类类型
        String type = engine.getType();

        //引擎缓存是否命中，命中则直接返回
        if (engineCache.containsKey(type)) {
            return engineCache.get(type);
        } else {
            // 初始化引擎类
            // 得到引擎的class类型
            Class<? extends AbstractEngine> clazz = registeredEngineMap.get(engine.getType());
            try {
                AbstractEngine abstractEngine = clazz.newInstance();

                //设置引擎所需的所有东西
                abstractEngine.setMode(engine.getMode());
                abstractEngine.setConfiguration(engine.getProperties());

                //让子类进行初始化操作
                abstractEngine.doInitial();

                //加入缓存
                engineCache.put(type, abstractEngine);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return engineCache.get(type);
    }
}
