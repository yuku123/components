package com.zifang.util.workflow.engine;

import com.zifang.util.workflow.interfaces.AbstractEngineService;
import com.zifang.util.workflow.service.EmptyHandler;
import com.zifang.util.workflow.service.JoinHandler;
import com.zifang.util.workflow.service.ResourceHandler;

import java.util.HashMap;
import java.util.Map;

public class SparkEngine extends AbstractEngine {

    public static Map<String, ? extends AbstractEngineService> registedEngineServiceMap = new HashMap<String, AbstractEngineService>(){
        {
            put("engine.service.resourceHandler",new ResourceHandler());
            put("engine.service.empty",new EmptyHandler());
            put("engine.service.joinHandler",new JoinHandler());
        }
    };

    @Override
    public Map<String, ? extends AbstractEngineService> getRegisteredEngineServiceMap() {
        return registedEngineServiceMap;
    }

    @Override
    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
        return registedEngineServiceMap.get(serviceUnit);
    }
}
