package com.zifang.util.workflow.service.spark;

import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.AbstractEngineService;
public class JoinHandler extends AbstractEngineService {

    @Override
    public void exec(ExecutableWorkflowNode executableWorkflowNode) {
        try{
            dataset = SparkContextFactory.getLocalSparkContext().getSqlContext().sql(properties.get("sql"));
            dataset.show();
            for(ExecutableWorkflowNode executableWorkflowNodePost : executableWorkflowNode.getPost()){
                executableWorkflowNodePost.setDatasetPre(dataset);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
