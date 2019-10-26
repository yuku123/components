package com.zifang.util.workflow.interfaces;

import java.util.HashMap;
import java.util.Map;

public class EngineService {

    public static Map<String, AbstractEngineService> registedEngineServiceMap = new HashMap<String, AbstractEngineService>(){
        {
            put("engine.service.resourceHandler",new ResourceHandler());
        }
    };

}
