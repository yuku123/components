package com.zifang.util.workflow.service.spark;

import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.bigdata.spark.mock.SparkDataMockUtil;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.AbstractEngineService;

public class ResourceHandler extends AbstractEngineService {

    private static String tempName = "tempName";

    private static String localFile = "localFile";

    @Override
    public void exec(ExecutableWorkflowNode executableWorkflowNode) {
        try {
            dataset = new SparkDataMockUtil(SparkContextFactory.getLocalSparkContext()).creatDataset(properties.get(localFile));
            dataset.registerTempTable(properties.get(tempName));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
