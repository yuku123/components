package com.zifang.util.bigdata.test;

import com.zifang.util.bigdata.spark.context.ClusterSparkContext;
import com.zifang.util.bigdata.spark.context.Model_Function;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BuildTest {

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
        JavaRDD<Tuple2<Object,Object>> javaRDD = pre_data.select("pre_label", "target").toJavaRDD()
                .map(row -> new Tuple2<>( row.get(0), (double)row.getInt(1)));
        MulticlassMetrics metrics = new MulticlassMetrics(javaRDD.rdd());
        Matrix confusion = metrics.confusionMatrix();
        System.out.println("Confusion matrix: \n" + confusion);
    }


}
