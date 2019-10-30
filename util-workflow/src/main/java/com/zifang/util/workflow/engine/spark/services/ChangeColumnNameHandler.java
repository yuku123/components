package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;

import java.util.Map;

public class ChangeColumnNameHandler extends AbstractSparkEngineService {

    @Override
    public void defaultHandler() {
        dataset = executableWorkflowNode.getDatasetPre();
        //传入的是当前的节点
        for(Map.Entry<String,String> entry : properties.entrySet()){
            dataset = dataset.withColumnRenamed(entry.getKey(),entry.getValue());
        }
        dataset.show();
    }

}
