package com.zifang.util.bigdata.test;

import com.zifang.util.bigdata.spark.context.ClusterSparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.types.DataTypes.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ApplyTest {
    public static void main(String[] args) {
        ClusterSparkContext clusterSparkContext = new ClusterSparkContext();
        Dataset<Row> dataSet = clusterSparkContext.getSqlContext().sql("select * from model_test.raw");

        String[] filteredColumnName = Arrays.asList(dataSet.columns())
                .stream()
                .filter( e -> (!"id".equals(e)) && (!"target".equals(e)))
                .collect(Collectors.toList())
                .toArray(new String[]{});

        VectorAssembler vector = new VectorAssembler().setInputCols(filteredColumnName).setOutputCol("features");

        dataSet = vector.transform(dataSet).select("target", "features","id");

        RandomForestClassificationModel model = RandomForestClassificationModel.load("hdfs://piday03:9000/user/piday/model_yj_plusv3");

        Dataset<Row> pre_data = model.transform(dataSet).select("id","target","pre_lable");
        pre_data.show();
    }
}
