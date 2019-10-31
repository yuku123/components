package com.zifang.util.workflow.engine.spark.services;

/**
 * 窗口函数，分析函数支持
 * */
public class WindowHandler {
}


/**
 *
 *
 * select
 * customerid,
 * shkzg_blank,
 * SUM(shkzg_blank) over (order by shkzg_blank rows between unbounded preceding and current row) as shkzg_blank_Sum
 *
 * from a
 *
 * */