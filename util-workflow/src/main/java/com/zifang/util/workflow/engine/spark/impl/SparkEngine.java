package com.zifang.util.workflow.engine.spark.impl;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.spark.services.ChangeColumnNameHandler;
import com.zifang.util.workflow.engine.spark.services.EmptyHandler;
import com.zifang.util.workflow.engine.spark.services.JoinHandler;
import com.zifang.util.workflow.engine.spark.services.ResourceHandler;

import java.util.HashMap;
import java.util.Map;

public class SparkEngine extends AbstractSparkEngine {

    private SparkContextInstance sparkContextInstance;

    public Map<String, Class<? extends AbstractEngineService>> registeredEngineServiceMap = new HashMap<String, Class<? extends AbstractEngineService>>(){
        {
            put("engine.service.resourceHandler",ResourceHandler.class);
            put("engine.service.empty",EmptyHandler.class);
            put("engine.service.joinHandler",JoinHandler.class);
            put("engine.service.changeColumn", ChangeColumnNameHandler.class);
        }
    };

    @Override
    public Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap() {
        return registeredEngineServiceMap;
    }

    @Override
    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
        try {
            AbstractSparkEngineService abstractSparkEngineService = (AbstractSparkEngineService)registeredEngineServiceMap.get(serviceUnit).newInstance();
            abstractSparkEngineService.setSparkContextInstance(sparkContextInstance);
            return abstractSparkEngineService;
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

    @Override
    public void doInitial() {
        sparkContextInstance = new SparkContextInstance(mode,properties);
    }
}
