package com.zifang.util.bigdata.spark.util;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zifang
 */
public class SparkUtil {

    private SparkContextInstance sparkContextInstance;

    public SparkUtil(SparkContextInstance sparkContextInstance){
        this.sparkContextInstance = sparkContextInstance;
    }

    /**
     * 传入绝对路径
     *
     * @param fileLocation 单个本地文件
     */
    public Dataset<Row> createDataSet(String fileLocation){

        Dataset<Row> dataSet = sparkContextInstance.getSqlContext().read()
                .format("csv")
                .option("header","true")
                .option("inferSchema", "true")
                .load(fileLocation);
        return dataSet;
    }

    public List<String> getColumnsStringList(Dataset<Row> dataset) {
        List<String> columnList = new ArrayList<>();
        for(StructField field :dataset.schema().fields()){
            columnList.add(field.name());
        }
        return columnList;
    }

    public List<Column> getColumnList(Dataset<Row> dataset) {
        List<Column> columnList = new ArrayList<>();
        for(StructField field :dataset.schema().fields()){
            columnList.add(dataset.col(field.name()));
        }
        return columnList;
    }

    public List<Column> transformStringToColumn(Dataset<Row> dataset, List<String> columnsStringType) {
        List<Column> columnList = new ArrayList<>();
        for(String column : columnsStringType){
            columnList.add(dataset.col(column));
        }
        return columnList;
    }

    public JavaRDD<String> readFromLocalToJavaRDD(String filePath){
        return sparkContextInstance.getJavaSparkContext().textFile(filePath);
    }

    public static void main(String[] args) {
        SparkUtil sparkUtil = new SparkUtil(new SparkContextInstance());
        Long a = sparkUtil.readFromLocalToJavaRDD("/Users/zifang/workplace/idea_workplace/components/util-bigdata/src/main/resources/input1.csv")
                .count();
        System.out.println(a);
        JavaRDD<Row> b = sparkUtil.readFromLocalToJavaRDD("/Users/zifang/workplace/idea_workplace/components/util-bigdata/src/main/resources/input1.csv")
                .map(e -> RowFactory.create(e.split(",")));
        System.out.println(b.count());

    }
}
