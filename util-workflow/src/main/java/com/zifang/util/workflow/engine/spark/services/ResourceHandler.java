package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.bigdata.spark.mock.SparkDataMockUtil;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;

public class ResourceHandler extends AbstractSparkEngineService {

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
