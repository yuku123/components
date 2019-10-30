package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;

public class JoinHandler extends AbstractSparkEngineService {

    @Override
    public void defaultHandler() {
        try{
            dataset = sparkContextInstance.getSqlContext().sql(properties.get("sql"));
            dataset.show();
            for(ExecutableWorkflowNode executableWorkflowNodePost : executableWorkflowNode.getPost()){
                executableWorkflowNodePost.setDatasetPre(dataset);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
