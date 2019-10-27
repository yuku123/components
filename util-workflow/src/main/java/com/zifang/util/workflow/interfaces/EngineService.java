package com.zifang.util.workflow.interfaces;

import com.zifang.util.workflow.service.EmptyHandler;
import com.zifang.util.workflow.service.JoinHandler;
import com.zifang.util.workflow.service.ResourceHandler;

import java.util.HashMap;
import java.util.Map;

public class EngineService {

    public static Map<String, AbstractEngineService> registedEngineServiceMap = new HashMap<String, AbstractEngineService>(){
        {
            put("engine.service.resourceHandler",new ResourceHandler());
            put("engine.service.empty",new EmptyHandler());
            put("engine.service.joinHandler",new JoinHandler());
        }
    };

    public static AbstractEngineService getEngineService(String serviceUnit) {
        try {
            return registedEngineServiceMap.get(serviceUnit).getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
