package com.zifang.util.workflow.engine.interfaces;

import java.util.Map;

public abstract class AbstractEngine {

    protected String mode;
    protected Map<String, String> properties;

    public abstract Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap();

    public abstract AbstractEngineService getRegisteredEngineService(String serviceUnit);

    public abstract void register(String name,Class<? extends AbstractEngineService> engineService);

    public void setMode(String mode){
        this.mode = mode;
    }

    public void setConfiguration(Map<String, String> properties){
        this.properties = properties;
    }

    public abstract void doInitial();
}
