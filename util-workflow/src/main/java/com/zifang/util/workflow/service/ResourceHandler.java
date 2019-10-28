package com.zifang.util.workflow.service;

import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.bigdata.spark.mock.SparkDataMockUtil;
import com.zifang.util.workflow.interfaces.AbstractEngineService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public class ResourceHandler extends AbstractEngineService {

    private static String tempName = "tempName";

    private static String localFile = "localFile";

    private Dataset<Row> dataset;
    private Map<String, String> properties;

    @Override
    public void setProperty(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void exec() {
        dataset = new SparkDataMockUtil(SparkContextFactory.getLocalSparkContext()).creatDataset(properties.get(localFile));
        dataset.registerTempTable(properties.get(tempName));
        dataset.show();
    }

    @Override
    public Dataset<Row> getDataset() {
        return dataset;
    }

    public static void main(String[] args) {
        String path = "util-workflow/src/main/resources/input1.csv";
        Dataset<Row> dataset = new SparkDataMockUtil(SparkContextFactory.getLocalSparkContext()).creatDataset(path);
        dataset.show();
    }
}
