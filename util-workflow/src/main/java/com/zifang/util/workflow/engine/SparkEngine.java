package com.zifang.util.workflow.engine;

import com.zifang.util.workflow.service.spark.ChangeColumnNameHandle;
import com.zifang.util.workflow.service.spark.EmptyHandler;
import com.zifang.util.workflow.service.spark.JoinHandler;
import com.zifang.util.workflow.service.spark.ResourceHandler;

import java.util.HashMap;
import java.util.Map;

public class SparkEngine extends AbstractEngine {

    public Map<String, Class<? extends AbstractEngineService>> registeredEngineServiceMap = new HashMap<String, Class<? extends AbstractEngineService>>(){
        {
            put("engine.service.resourceHandler",ResourceHandler.class);
            put("engine.service.empty",EmptyHandler.class);
            put("engine.service.joinHandler",JoinHandler.class);
            put("engine.service.changeColumn", ChangeColumnNameHandle.class);
        }
    };

    @Override
    public Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap() {
        return registeredEngineServiceMap;
    }

    @Override
    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
        try {
            return registeredEngineServiceMap.get(serviceUnit).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void register(String name,Class<? extends AbstractEngineService> engineService) {
        registeredEngineServiceMap.put(name,engineService);
    }
}
