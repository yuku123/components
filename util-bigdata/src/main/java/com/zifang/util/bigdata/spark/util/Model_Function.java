package com.zifang.util.bigdata.spark.util;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.feature.ChiSqSelector;
import org.apache.spark.ml.feature.ChiSqSelectorModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Tuple2;
import org.apache.spark.mllib.linalg.Matrix;


import java.util.*;

public class Model_Function {
    public static String[] getFeatureNames(Dataset<Row> modeldataste,String userid,String target){
        String[] colums = modeldataste.columns();
        List<String> col_names_list = new ArrayList<>(Arrays.asList(colums));
        col_names_list.remove(userid);
        col_names_list.remove(target);
        String[] var_names_new = col_names_list.toArray(new String[]{});
        return var_names_new;
    }

    public static Dataset<Row>  createVectorAssembler(Dataset<Row> modeldataste,String[] var_names_new,String target,String features,String userid){

        VectorAssembler vector = new VectorAssembler().setInputCols(var_names_new).setOutputCol(features);
        Dataset<Row> model_dataset = vector.transform(modeldataste).select(target, features,userid);
        return model_dataset;
    }

    public static ChiSqSelectorModel createChiqModel(Dataset<Row> dataset,int var_bnumber,String features,String target,String outputfeatures){
        ChiSqSelector selector = new ChiSqSelector()
                .setNumTopFeatures(var_bnumber)
                .setFeaturesCol(features)
                .setLabelCol(target)
                .setOutputCol(outputfeatures);
        ChiSqSelectorModel model = selector.fit(dataset);
        return model;
    }

    public static void getModelImportance(RandomForestClassificationModel model, String[] var_names){
        double[] importance_value = model.featureImportances().toArray();
        System.out.println(importance_value.length);

        HashMap<String, Double> map = new HashMap<String, Double>();
        for(int i=0;i<importance_value.length;i++){
            map.put(var_names[i],importance_value[i]);
        }


        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet()); //转换为list
        list.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for(int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue());
        }
    }

    public static String[] select_model_varnames(int[] index,String[] varNames){
        ArrayList<String> var_names = new ArrayList<String>();
        for(int i=0;i<index.length;i++){
            var_names.add(varNames[index[i]]);
            System.out.println(varNames[index[i]]);
        }
        String[] selectNames = var_names.toArray(new String[var_names.size()]);
        return selectNames;
    }

    public static RandomForestClassificationModel build_rfModel(Dataset<Row> modelDataset,String target,String features,String pre_lable,String pre_problity){

        RandomForestClassifier rf = new RandomForestClassifier()
                .setLabelCol(target)
                .setFeaturesCol(features)
                .setMaxBins(32)
                .setMaxDepth(5)
                .setNumTrees(7)
                .setImpurity("gini")
                .setPredictionCol(pre_lable)
                .setProbabilityCol(pre_problity)
                .setFeatureSubsetStrategy("auto");

        RandomForestClassificationModel rfmodel = rf.fit(modelDataset);
        return rfmodel;
    }


}
