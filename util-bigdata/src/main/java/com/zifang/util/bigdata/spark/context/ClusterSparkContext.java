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

    public static void main(String[] args) throws IOException {
        ClusterSparkContext clusterSparkContext = new ClusterSparkContext();
        Dataset<Row> dataSet = clusterSparkContext.getSqlContext().sql("select * from model_test.raw");
        dataSet.show();

        String[] filteredColumnName = Arrays.asList(dataSet.columns())
                .stream()
                .filter( e -> (!"id".equals(e)) && (!"target".equals(e)))
                .collect(Collectors.toList())
                .toArray(new String[]{});

        VectorAssembler vector = new VectorAssembler().setInputCols(filteredColumnName).setOutputCol("features");
        Dataset<Row> modelDataSet = vector.transform(dataSet).select("target", "features","id");

        //切分数据集
        Dataset<Row>[] splitDataset=modelDataSet.randomSplit(new double[]{0.7, 0.3});
        Dataset<Row> trainDataSet = splitDataset[0];
        Dataset<Row> testDataSet = splitDataset[1];

        //建立模型
        RandomForestClassificationModel rfModel = Model_Function.build_rfModel(trainDataSet,"target","features","pre_lable","pre_probilty");
        Model_Function.getModelImportance(rfModel,filteredColumnName);
        rfModel.write().overwrite().save("hdfs://piday03:9000/user/piday/model_yj_plusv3");
        Dataset<Row> pre_data = rfModel.transform(testDataSet);
        pre_data.show();

        //判断好坏 Confusion matrix
        JavaRDD<Tuple2<Object,Object>> javaRDD = pre_data.select("pre_lable", "target").toJavaRDD()
                .map(row -> new Tuple2<>( row.get(0), (double)row.getInt(1)));
        MulticlassMetrics metrics = new MulticlassMetrics(javaRDD.rdd());
        Matrix confusion = metrics.confusionMatrix();
        System.out.println("Confusion matrix: \n" + confusion);
    }

}
