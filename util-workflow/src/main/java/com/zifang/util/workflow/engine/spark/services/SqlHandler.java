package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;
import com.zifang.util.workflow.engine.spark.impl.CacheEngineService;

import java.util.HashMap;

/**
 * 支持书写任意sql的处理器件
 * */
public class SqlHandler extends AbstractSparkEngineService {

    @Override
    public void defaultHandler() {

    }

    public void sql(){
        //得到缓存控制服务器
        CacheEngineService cacheEngineService = getWorkFlowApplicationContext().getCacheEngineService();

        HashMap<String,String> transformedParameter = GsonUtil.changeToSubClass(invokeParameter, HashMap.class);



    }



}
