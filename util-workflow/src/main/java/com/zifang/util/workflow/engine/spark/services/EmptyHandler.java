package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.workflow.anoation.EngineService;
import com.zifang.util.workflow.engine.spark.AbstractSparkEngineService;

@EngineService(name = "engine.service.empty")
public class EmptyHandler extends AbstractSparkEngineService {

    @Override
    public void defaultHandler() {

    }
}
