package com.zifang.util.strategy.rule;

/**
 * 规则求值终止模式。
 */
public enum MatchMode {

    /** 命中第一条即停（黑白名单、封禁名单） */
    FIRST_MATCH,
    /** 全跑完，收集所有命中（打标签、累计分） */
    COLLECT_ALL,
    /** 累加评分达到阈值即停（评分卡） */
    THRESHOLD
}
