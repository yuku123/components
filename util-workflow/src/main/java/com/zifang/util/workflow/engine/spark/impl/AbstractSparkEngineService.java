package com.zifang.util.workflow.engine.spark.impl;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;

import java.util.Map;

public abstract class AbstractSparkEngineService extends AbstractEngineService {

    protected SparkContextInstance sparkContextInstance;

    public void setSparkContextInstance(SparkContextInstance sparkContextInstance){
        this.sparkContextInstance = sparkContextInstance;
    };
}
