package com.zifang.util.bigdata.spark.context;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

public class LocalSparkContext {

    private SparkConf sparkConf;

    private SQLContext sqlContext;

    private JavaSparkContext javaSparkContext;

    LocalSparkContext(){
        sparkConf = new SparkConf();
        sparkConf.setMaster("local").setAppName("test");
        javaSparkContext = new JavaSparkContext(sparkConf);
        sqlContext = new SQLContext(javaSparkContext);
    }

    public SparkConf getSparkConf() {
        return sparkConf;
    }

    public SQLContext getSqlContext() {
        return sqlContext;
    }

    public JavaSparkContext getJavaSparkContext() {
        return javaSparkContext;
    }
}
