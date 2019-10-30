package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;

import java.util.Properties;

public class ResourceHandler extends AbstractSparkEngineService {

    private static String tempName = "tempName";

    private static String localFile = "localFile";

    @Override
    public void defaultHandler() {

    }

    public void handleLocalInput(){
        try {
            dataset = sparkUtil.creatDataset(properties.get(localFile));
            dataset.registerTempTable(properties.get(tempName));
            dataset.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleLocalOutput(){
        try {
            sparkContextInstance.getSparkContext()
                    .hadoopConfiguration()
                    .set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false");
            executableWorkflowNode.getPre().get(0).getDataset()
                    .repartition(1)
                    .write()
                    .mode(SaveMode.Overwrite)
                    .format("csv")
                    .option("header","true")
                    .save(properties.get("outputDir"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleMysqlInput(){
        String dbUrl = "jdbc:mysql://localhost:3306/test";
        String table = "test";
        Properties props = new Properties();
        props.put("user", "root");
        props.put("password", "zxc123");

        //从mysql读数据
        dataset = sparkContextInstance.getSqlContext()
                .read()
                .jdbc(dbUrl, table, props);
        dataset.show();
        System.out.println("aa");

    }

    public void handleMysqlOutput(){
        String dbUrl = "jdbc:mysql://localhost:3306/test";
        String table = "test.test_a";
        Properties props = new Properties();
        props.put("user", "root");
        props.put("password", "zxc123");

        executableWorkflowNode.getPre().get(0).getDataset().write()
                .mode(SaveMode.Overwrite)
                .jdbc(dbUrl, table,props);
    }
}
