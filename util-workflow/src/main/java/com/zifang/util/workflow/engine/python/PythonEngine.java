package com.zifang.util.workflow.engine.python;

import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;

import java.util.Map;

public class PythonEngine extends AbstractEngine {

    @Override
    public Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap() {
        return null;
    }

    @Override
    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
        return null;
    }

    @Override
    public void register(String name, Class<? extends AbstractEngineService> engineService) {

    }

    @Override
    public void doInitial() {

    }
}
