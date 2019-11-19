package com.zifang.util.workflow.conponents;

import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.config.WorkflowNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkFlowApplication {

    public static Map<Integer,WorkFlowApplicationContext> workFlowContextMap = new HashMap<>();

    public static ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public static AtomicInteger workflowContextId = new AtomicInteger(0);

    /**
     * 通过一个workflowConfiguration 主动创造一个workflow的上下文
     *
     * return WorkFlowApplicationContextId
     * */
    public synchronized Integer createWorkflowContext(WorkflowConfiguration workflowConfiguration){

        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();

        workFlowApplicationContext.initialByWorkflowConfigurationInstance(workflowConfiguration);

        workFlowContextMap.put(workflowContextId.getAndIncrement(),workFlowApplicationContext);

        return workflowContextId.get();
    }

    /**
     * 增加一个游离节点
     *
     * 参数需要提供全量
     * */
    public synchronized Boolean addSimpleWorkflowNode(Integer workFlowApplicationContextId,WorkflowNode workflowNode){

        //从共享上下文池内得到缓存
        WorkFlowApplicationContext workFlowApplicationContext = workFlowContextMap.get(workFlowApplicationContextId);

        //上下文自行分配nodeID
        workflowNode.setNodeId(workFlowApplicationContext.produceNodeId());

        //上下文的 元配置信息内部增加 节点信息
        workFlowApplicationContext.getWorkflowConfiguration().getWorkflowNodeList().add(workflowNode);

        //通过上下文内的更新方法，根据元配置信息更新节点
        workFlowApplicationContext.refreshByWorkflowConfiguration();

        return true;
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

    /***
     * 单步执行，执行到指定位置的node,中止
     * */
    public synchronized Boolean startReferTo(){
        return null;
    }

    /**
     * 强制这个上下文暂停，并返回状态
     * */
    public synchronized Boolean forcePause(){
        return null;
    }

    /**
     * 再重新启动这个 工作流的上下文
     * */
    public synchronized Boolean resume(){
        return null;
    }

    /**
     * 所有的上下文的状态情况
     * */
    public Boolean status(){
        return null;
    }


}
