package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;
import org.apache.spark.sql.SaveMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ResourceHandler extends AbstractSparkEngineService {

    private static String tempName = "tempName";

    private static String localFile = "localFile";

    @Override
    public void defaultHandler() {

    }

    public void handleLocalInput(){

        HashMap<String,String> transformedParameter = GsonUtil.changeToSubClass(invokeParameter, HashMap.class);
        try {
            dataset = sparkUtil.createDataSet(transformedParameter.get(localFile));
            dataset.createOrReplaceTempView(transformedParameter.get(tempName));
            dataset.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleLocalOutput(){
        Map<String,String> transformedParameter = GsonUtil.changeToSubClass(invokeParameter, HashMap.class);

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
                    .save(transformedParameter.get("outputDir"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleMysqlInput(){

        Properties props = new Properties();
        props.put("user", properties.get("user"));
        props.put("password", properties.get("password"));

        //从mysql读数据
        dataset = sparkContextInstance.getSqlContext()
                .read()
                .jdbc(properties.get("dbUrl"), properties.get("table"), props);
        dataset.show();
    }

    public void handleMysqlOutput(){

        Properties props = new Properties();
        props.put("user", properties.get("user"));
        props.put("password", properties.get("password"));

        executableWorkflowNode.getPre().get(0).getDataset()
                .write()
                .mode(SaveMode.Overwrite)
                .jdbc(properties.get("dbUrl"), properties.get("table"), props);
    }


    public void handleHiveInput(){
        dataset = sparkContextInstance.getSqlContext().sql("select * from "+ properties.get("tableName"));
        dataset.show();
    }


    public void handleHiveOutput(){
        String view = "t"+"_"+System.currentTimeMillis();
        executableWorkflowNode.getPre().get(0).getDataset().createOrReplaceTempView(view);
        String tableName = properties.get("tableName");
        sparkContextInstance.getSqlContext().sql("drop table if exists "+tableName);
        dataset = sparkContextInstance.getSqlContext().sql("create table "+tableName+" as select * from "+ view);
    }

}
