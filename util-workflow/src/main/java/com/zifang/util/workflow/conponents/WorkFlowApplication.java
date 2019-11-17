package com.zifang.util.workflow.conponents;

import com.zifang.util.workflow.config.WorkflowConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkFlowApplication {

    public static ExecutorService threadPool = Executors.newFixedThreadPool(100);

    /**
     * 通过一个workflowConfiguration 主动创造一个workflow的上下文
     *
     * return WorkFlowApplicationContextId
     * */
    public Integer createWorkflowContext(WorkflowConfiguration workflowConfiguration){
        return null;
    }

}
