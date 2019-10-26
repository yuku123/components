package com.zifang.util.workflow.config;

import java.util.List;

/**
 * 整个流程图的定义
 * */
public class WorkflowConfiguration {

    /**
     * 全局性的配置，流引擎配置
     * */
    private Configurations configurations;

    /**
     * 所有控制用的节点配置
     * */
    private List<WorkflowNode> workflowNodeList;


    public Configurations getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }

    public List<WorkflowNode> getWorkflowNodeList() {
        return workflowNodeList;
    }

    public void setWorkflowNodeList(List<WorkflowNode> workflowNodeList) {
        this.workflowNodeList = workflowNodeList;
    }
}
