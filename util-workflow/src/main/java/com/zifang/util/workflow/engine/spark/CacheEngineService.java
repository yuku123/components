//package com.zifang.util.workflow.engine.spark;
//
//import com.zifang.util.workflow.config.CacheEngine;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;
//
//public class CacheEngineService {
//
//    private CacheEngine cacheEngine;
//
//    public CacheEngineService(CacheEngine cacheEngine) {
//        this.cacheEngine = cacheEngine;
//    }
//
//    public void doCache(Dataset<Row> dataset, String cacheName) {
//        dataset.createOrReplaceTempView(cacheName);
//    }
//}
