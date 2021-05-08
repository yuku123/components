package com.zifang.util.bigdata.spark.context;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

public class LocalSparkContext {

    private SparkConf sparkConf;

    private SQLContext sqlContext;

    private JavaSparkContext javaSparkContext;

    private SparkContext sparkContext;

    private SparkSession sparkSession;

    public LocalSparkContext() {
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

//    public LocalSparkContext(){
//        sparkConf = new SparkConf();
//        sparkConf.setMaster("local[4]").setAppName("test");
//        javaSparkContext = new JavaSparkContext(sparkConf);
//        sqlContext = new SQLContext(javaSparkContext);
//
////        sparkSession = SparkSession.builder()
////                .master("local[4]")
////                .appName("appName")
////                .config(sparkConf)
////                .getOrCreate();
////        sparkContext = sparkSession.sparkContext();
////        sparkContext.setLogLevel("ERROR");
////        javaSparkContext = JavaSparkContext.fromSparkContext(sparkContext);
////        sqlContext = sparkSession.sqlContext();
//    }

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
