package com.zifang.util.bigdata.spark.context;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

public class SparkContextInstance {

    private static String cluster = "engine.spark.mode.cluster";
    private static String local = "engine.spark.mode.local";

    private SparkConf sparkConf;

    private SQLContext sqlContext;

    private JavaSparkContext javaSparkContext;

    private org.apache.spark.SparkContext sparkContext;

    private SparkSession sparkSession;

    public SparkContextInstance(){
        doLocalInitial(null);
    }
    public SparkContextInstance(String mode, Map<String, String> properties) {
        if(local.equals(mode)){
            doLocalInitial(properties);
        }else if (cluster.equals(mode)){
            doClusterInitial(properties);
        }
    }

    private void doClusterInitial(Map<String, String> properties) {
        sparkConf = new SparkConf();
        sparkConf.setMaster(properties.get("master"));
        sparkConf.setAppName(properties.get("appName") == null?"default":properties.get("appName"));
        sparkConf.set("spark.sql.warehouse.dir",properties.get("spark.sql.warehouse.dir"));
        sparkConf.set("hive.metastore.uris",properties.get("hive.metastore.uris"));
        sparkConf.set("spark.executor.memory",properties.get("spark.executor.memory"));
        sparkConf.set("spark.driver.maxResultSize",properties.get("spark.driver.maxResultSize"));
        sparkConf.set("spark.cores.max",properties.get("spark.cores.max"));
        sparkConf.setJars(properties.get("jars").split(","));
        sparkSession = SparkSession.builder()
                .config(sparkConf)
                .enableHiveSupport()
                .getOrCreate();
        sparkContext = sparkSession.sparkContext();
        sparkContext.setLogLevel(properties.get("logLevel"));
        javaSparkContext = JavaSparkContext.fromSparkContext(sparkContext);
        sqlContext = sparkSession.sqlContext();
    }

    private void doLocalInitial(Map<String, String> properties) {
        sparkConf = new SparkConf();
        sparkConf.setMaster("local[4]").setAppName("test");

        sparkSession = SparkSession.builder()
                .master("local[4]")
                .appName("appName")
                .config(sparkConf)
                .getOrCreate();
        sparkContext = sparkSession.sparkContext();
        sparkContext.setLogLevel("ERROR");
        javaSparkContext = JavaSparkContext.fromSparkContext(sparkContext);
        sqlContext = sparkSession.sqlContext();
    }

    public SparkConf getSparkConf() {
        return sparkConf;
    }

    public void setSparkConf(SparkConf sparkConf) {
        this.sparkConf = sparkConf;
    }

    public SQLContext getSqlContext() {
        return sqlContext;
    }

    public void setSqlContext(SQLContext sqlContext) {
        this.sqlContext = sqlContext;
    }

    public JavaSparkContext getJavaSparkContext() {
        return javaSparkContext;
    }

    public void setJavaSparkContext(JavaSparkContext javaSparkContext) {
        this.javaSparkContext = javaSparkContext;
    }

    public org.apache.spark.SparkContext getSparkContext() {
        return sparkContext;
    }

    public void setSparkContext(SparkContext sparkContext) {
        this.sparkContext = sparkContext;
    }

    public SparkSession getSparkSession() {
        return sparkSession;
    }

    public void setSparkSession(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }
}
