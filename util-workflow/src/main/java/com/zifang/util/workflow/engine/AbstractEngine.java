package com.zifang.util.workflow.engine;

import com.zifang.util.workflow.interfaces.AbstractEngineService;

import java.util.Map;

public abstract class AbstractEngine {

    public Map<String, ? extends AbstractEngineService> getRegisteredEngineServiceMap() {
        return null;
    }

    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
        return null;
    }
}
