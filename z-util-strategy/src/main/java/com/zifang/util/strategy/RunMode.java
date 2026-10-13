package com.zifang.util.strategy;

/**
 * 决策包的运行模式。
 * <p>
 * 三态划分参照阿里云决策引擎的策略状态（草稿 / 试运行 / 正式运行）：
 * <ul>
 *     <li>{@link #DRAFT} 草稿：可保存、不执行</li>
 *     <li>{@link #TRIAL} 试运行：可执行但不正式输出 —— 影子模式，全流量跑，结论进 shadow</li>
 *     <li>{@link #ACTIVE} 正式运行：执行并输出结论</li>
 * </ul>
 */
public enum RunMode {

    DRAFT,
    TRIAL,
    ACTIVE
}
