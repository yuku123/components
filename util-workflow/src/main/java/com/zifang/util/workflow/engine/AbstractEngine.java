package com.zifang.util.workflow.engine;

import java.util.Map;

public abstract class AbstractEngine {

    public abstract Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap();

    public abstract AbstractEngineService getRegisteredEngineService(String serviceUnit);

    public abstract void register(String name,Class<? extends AbstractEngineService> engineService);
}
