//package com.zifang.util.workflow.engine.spark.services;
//
//import com.zifang.util.core.util.GsonUtil;
//import com.zifang.util.workflow.annoation.EngineService;
//import com.zifang.util.workflow.engine.spark.AbstractSparkEngineService;
//import com.zifang.util.workflow.engine.spark.services.praser.PivotA;
//import com.zifang.util.workflow.engine.spark.services.praser.PivotANode;
//import org.apache.spark.sql.Column;
//import org.apache.spark.sql.functions;
//
//import java.util.List;
//import java.util.Map;
//
//@EngineService(name = "engine.service.pivot")
//public class PivotHandler extends AbstractSparkEngineService {
//
//    @Override
//    public void defaultHandler() {
//
//    }
//
//    public void pivot() {
//        PivotA pivotA = GsonUtil.changeToSubClass(invokeParameter, PivotA.class);
//
//        dataset = executableWorkflowNode.getPre().get(0).getDataset();
//
//        for (PivotANode pivotANode : pivotA.getPivotColumnDefinations()) {
//
//            //得到所有的column name的列表
//            List<String> columnsStringType = sparkUtil.getColumnsStringList(dataset);
//
//            //除去所有的
//            columnsStringType.remove(pivotANode.getColumnName());
//            List<Column> columns = sparkUtil.transformStringToColumn(dataset, columnsStringType);
//
//            if (pivotANode.getValue() != null) {
//                dataset = dataset.groupBy(columns.toArray(new Column[]{}))
//                        .pivot(pivotANode.getColumnName())
//                        .agg(functions.sum(pivotANode.getValue()))
//                        .na().fill(0);
//            } else {
//                dataset = dataset.groupBy(columns.toArray(new Column[]{}))
//                        .pivot(pivotANode.getColumnName())
//                        .count()
//                        .na().fill(0);
//            }
//        }
//
//        if (pivotA.getColumnMap() != null) {
//            // 外部介入,将最终的数据进行列明映射作用
//            for (Map.Entry<String, String> entry : pivotA.getColumnMap().entrySet()) {
//                dataset = dataset.withColumnRenamed(entry.getKey(), entry.getValue());
//            }
//        }
//        dataset.show();
//    }
//
//    public void unpivot() {
//
//    }
//}
