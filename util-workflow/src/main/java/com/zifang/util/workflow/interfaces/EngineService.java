package com.zifang.util.workflow.interfaces;

import com.zifang.util.core.interfaces.AbstractEngine;

import java.util.HashMap;
import java.util.Map;

public class EngineService {

    public static Map<String, AbstractEngineService> registedEngineServiceMap = new HashMap<String, AbstractEngineService>(){
        {
            put("engine.service.resourceHandler",new ResourceHandler());
            put("engine.service.empty",new EmptyHandler());
        }
    };

    public static AbstractEngineService getEngineService(String serviceUnit) {
        return registedEngineServiceMap.get(serviceUnit);
    }
}
