package com.zifang.util.workflow.engine.spark;

import com.zifang.util.workflow.annoation.EngineService;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.spark.services.ChangeColumnNameHandler;

import java.util.HashMap;
import java.util.Map;

public class SparkEngine extends AbstractSparkEngine {

//    private SparkContextInstance sparkContextInstance;

//    {
//            put("engine.service.empty",EmptyHandler.class);
//            put("engine.service.resourceHandler",ResourceHandler.class);//数据的出入
//            put("engine.service.joinHandler",JoinHandler.class);//专门用作数据整合，join相关
//            put("engine.service.changeColumn", ChangeColumnNameHandler.class);//专门用作数据映射
//            put("engine.service.pivot", PivotHandler.class);//专门处理数据透视功能
//            put("engine.service.codification", CodificationHandler.class);//数据转换的一个环节，将列向数据转化为标定量
//            put("engine.service.sql",SqlHandler.class);
//            put("engine.service.castColumn", CodificationHandler.class);//更改列的类型性质
    //横向 行数据 过滤(数据过滤)
    //纵向 列数据 过滤(挑选列)
    //对表的统计，列统计，行统计，缺失值统计，提供的统计参考提供多个--> 可能需要变成一个util而不是一个服务性质的存在 当操作是需要统计量参与的就执行方法调用
    //窗口方法：组内排序，全量组排序，窗口累加，分片打标识
    //split 节点内对数据进行分开
    //

    //    }
    public Map<String, Class<? extends AbstractEngineService>> registeredEngineServiceMap = new HashMap<String, Class<? extends AbstractEngineService>>() {
    };

    @Override
    public Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap() {
        return registeredEngineServiceMap;
    }

    @Override
    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
//        try {
//            AbstractSparkEngineService abstractSparkEngineService = (AbstractSparkEngineService) registeredEngineServiceMap.get(serviceUnit).newInstance();
//            abstractSparkEngineService.setSparkContextInstance(sparkContextInstance);
//            return abstractSparkEngineService;
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public void register(String name, Class<? extends AbstractEngineService> engineService) {
        registeredEngineServiceMap.put(name, engineService);
    }

    @Override
    public void doInitial() {
//        sparkContextInstance = new SparkContextInstance(mode, properties);
//
//        //注册引擎方法
//        Set<Class<?>> classSet = ClassUtil.searchClasses(this.getClass().getPackage().getName(), e -> e.isAnnotationPresent(EngineService.class));
//
//        for (Class<?> clazz : classSet) {
//            String value = AnnotationUtil.getAnnotationValue(clazz, EngineService.class, "name");
//            register(value, (Class<? extends AbstractEngineService>) clazz);
//        }
    }

    public static void main(String[] args) {
        System.out.println(ChangeColumnNameHandler.class.isAnnotationPresent(EngineService.class));
    }
}
