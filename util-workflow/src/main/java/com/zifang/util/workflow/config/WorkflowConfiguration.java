package com.zifang.util.workflow.config;

import lombok.Data;

import java.util.List;

/**
 * 整个流程图的定义
 */
@Data
public class WorkflowConfiguration {

    /**
     * 全局性的配置，流引擎配置
     */
    private Configurations configurations;

    /**
     * 所有控制用的节点配置
     */
    private List<WorkflowNode> workflowNodeList;

}
