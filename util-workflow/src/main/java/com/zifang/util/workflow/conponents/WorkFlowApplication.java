package com.zifang.util.workflow.conponents;

import com.zifang.util.workflow.config.WorkflowConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkFlowApplication {

    public static Map<Integer,WorkFlowApplicationContext> workFlowContextMap = new HashMap<>();

    public static ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public static Random random = new Random(Integer.MAX_VALUE);

    /**
     * 通过一个workflowConfiguration 主动创造一个workflow的上下文
     *
     * return WorkFlowApplicationContextId
     * */
    public synchronized Integer createWorkflowContext(WorkflowConfiguration workflowConfiguration){
        Integer index = null;
        while(true){
            index = random.nextInt();
            if(!workFlowContextMap.containsKey(index)){
                WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
                workFlowApplicationContext.initialByWorkflowConfigurationInstance(workflowConfiguration);
                workFlowContextMap.put(index,workFlowApplicationContext);
                break;
            }
        }
        return index;
    }

    /**
     * 增加一个游离节点
     *
     * */
    public synchronized Boolean addSimpleWorkfloeNode(){
        return null;
    }

    /**
     * 更新某一个节点的配置 配置包含 执行单元，执行单元所需参数，上下游连接情况
     *
     * */
    public synchronized Boolean modifyWorkflowNodeConfiguration(){
        return null;
    }


    /**
     * 重置某个节点的状态，连带的所有的后续的节点全部回滚到初始状态
     * */
    public synchronized Boolean resetWorkflowNode(){
        return null;
    }


}
