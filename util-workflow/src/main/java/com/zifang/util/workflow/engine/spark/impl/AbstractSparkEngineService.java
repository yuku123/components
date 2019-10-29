package com.zifang.util.workflow.engine.spark.impl;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import com.zifang.util.bigdata.spark.util.SparkUtil;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;

public abstract class AbstractSparkEngineService extends AbstractEngineService {

    protected SparkContextInstance sparkContextInstance;
    protected SparkUtil sparkUtil;

    public void setSparkContextInstance(SparkContextInstance sparkContextInstance){
        this.sparkContextInstance = sparkContextInstance;
        sparkUtil = new SparkUtil(this.sparkContextInstance);
    };
}
