package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;

import java.util.HashMap;
import java.util.Map;

public class JoinHandler extends AbstractSparkEngineService {

    @Override
    public void defaultHandler() {
        Map<String,String> properties = GsonUtil.changeToSubClass(invokeParameter, HashMap.class);

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
