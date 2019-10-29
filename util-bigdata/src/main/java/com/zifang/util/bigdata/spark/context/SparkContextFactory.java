package com.zifang.util.bigdata.spark.context;

/**
 * 以单例形式生产spark的上下文，保证一个jvm上只会存在一个spark的context
 * */
public class SparkContextFactory {

    public static LocalSparkContext localSparkContext = new LocalSparkContext();

    public static ClusterSparkContext clusterSparkContext;

    public static LocalSparkContext getLocalSparkContext(){
        return localSparkContext;
    }

    public static ClusterSparkContext getClusterSparkContext(SparkConfiguration clusterSparkConfiguration){
        return null;
    }
}
