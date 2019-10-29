package com.zifang.util.bigdata.spark.context;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClusterSparkContext implements java.io.Serializable{

    private SparkConf sparkConf;

    private SQLContext sqlContext;

    private JavaSparkContext javaSparkContext;

    private SparkContext sparkContext;

    private SparkSession sparkSession;

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

    public SparkContext getSparkContext() {
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

    public ClusterSparkContext(){
        sparkConf = new SparkConf();
        sparkConf.setMaster("spark://192.168.1.103:7077").setAppName("test");
        sparkConf.set("spark.sql.warehouse.dir","hdfs://192.168.1.103:9000/user/hive/warehouse");
        sparkConf.set("hive.metastore.uris","thrift://192.168.1.103:9083");
        sparkConf.set("spark.executor.memory","2g");
        sparkConf.set("spark.driver.maxResultSize","2g");
        sparkConf.set("spark.cores.max","1");
        sparkConf.setJars(new String[]{"/home/zifang/workplace/idea_workplace/components/util-bigdata/target/util-bigdata-1.0-SNAPSHOT.jar"});
        sparkSession = SparkSession.builder()
                .config(sparkConf)
                .enableHiveSupport()
                .getOrCreate();
        sparkContext = sparkSession.sparkContext();
        sparkContext.setLogLevel("ERROR");
        javaSparkContext = JavaSparkContext.fromSparkContext(sparkContext);
        sqlContext = sparkSession.sqlContext();
    }

}
