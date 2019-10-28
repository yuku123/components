package com.zifang.util.workflow.service.spark;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.AbstractEngineService;

import java.util.Map;

public class ChangeColumnNameHandler extends AbstractEngineService {

    @Override
    public void exec(ExecutableWorkflowNode executableWorkflowNode) {
        dataset = executableWorkflowNode.getDatasetPre();
        //传入的是当前的节点
        for(Map.Entry<String,String> entry : properties.entrySet()){
            dataset = dataset.withColumnRenamed(entry.getKey(),entry.getValue());
        }
        dataset.show();
    }

}
