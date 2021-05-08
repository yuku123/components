package com.zifang.util.workflow.config;

/**
 * 表达该节点的生命周期
 */
public interface NodeLifeCycle {

    /**
     * 表达该节点准备就绪：
     * 条件：
     * 初始化之后，前置节点全部结束之后，本节点触发过设置参数操作之后
     */
    String PREPARED = "prepared";

    /**
     * 该节点处在运行状态，按道理是一个原子态
     */
    String STARTED = "started";

    /**
     * 被强行的中止操作的情况下
     */
    String INTERRRUPTED = "interrupted";

    /**
     * 已经执行完毕
     */
    String EXECUTED = "executed";

    /**
     * 获得得到当前节点的状况
     */
    String getStatus();

    /**
     * 前处理
     */
    void preExecute();

    /**
     * 后处理
     */
    void postExecute();

    /**
     * 初始化
     */
    void init();

    /**
     * 赋值
     */
    void setStatus(String status);

    /**
     * 将这个节点重置为可执行状态
     * <p>
     * 注意，这个方法只是单纯的对自身节点进行重置，上下文内节点的重置将由上下文进行
     */
    default void setRestart() {
        this.setStatus(PREPARED);
    }
}
