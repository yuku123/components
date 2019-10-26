package com.zifang.util.workflow.interfaces;

import com.zifang.util.core.interfaces.AbstractEngine;
import com.zifang.util.core.util.FileUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.config.WorkflowNode;
import com.zifang.util.workflow.engine.EngineFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 每个工作流的上下文，是工作引擎的工作子单元。工作引擎只负责发布命令，调度资源，调度任务相关功能
 * */
public class WorkFlowApplicationContext {

    private List<ExecutableWorkNode> executableWorkNodes = new ArrayList<>();
    private Task task = new Task();
    private AbstractEngine abstractEngine;
    private Map<String,ExecutableWorkNode> executableWorkNodeIdMap = new LinkedHashMap<>();

    private String filePath;

    private WorkflowConfiguration workflowConfiguration;

    public WorkFlowApplicationContext(String filePath){
        this.filePath = filePath;
        refresh();
    }

    //初始化该上下文
    private void refresh() {

        //从文件内初始化配置
        initialConfiguration();

        initialWorkableTask();
    }

    private void initialWorkableTask() {

        abstractEngine = EngineFactory.getEngine(this.workflowConfiguration.getConfigurations().getEngine());

        //注册所有的可执行节点定义
        registerWorkFlowNode();

        //连接已经注册好了的可执行node
        connectWorkFlowNode();
        
        produceExecutableTask();
    }

    private void produceExecutableTask() {
        task.setWorkFlowApplicationContext(this);
        task.setStartExecutableWorkNode(executableWorkNodeIdMap.get("start"));
        task.setExecutableWorkNodes(executableWorkNodes);
        task.setExecutableWorkNodeIdMap(executableWorkNodeIdMap);
    }

    private void connectWorkFlowNode() {

        for(ExecutableWorkNode executableWorkNode : executableWorkNodes){
            
            List<String> pre = executableWorkNode.getWorkflowNode().getConnector().getPre();
            List<String> post = executableWorkNode.getWorkflowNode().getConnector().getPost();

            for(String connectNodeId: pre){
                executableWorkNodeIdMap.get(connectNodeId).putPost(executableWorkNode);
            }
            for(String connectNodeId: post){
                try {
                    executableWorkNodeIdMap.get(connectNodeId).putPre(executableWorkNode);
                }catch (Exception e){
                    System.out.println("");
                }
            }
        }
    }

    private void registerWorkFlowNode() {

        for(WorkflowNode workflowNode : workflowConfiguration.getWorkflowNodeList()){

            ExecutableWorkNode executableWorkNode = new ExecutableWorkNode();

            executableWorkNode.setEngine(abstractEngine);

            executableWorkNode.setAbstractEngineService(EngineService.getEngineService(workflowNode.getServiceUnit()));

            executableWorkNode.setNodeId(workflowNode.getNodeId());

            executableWorkNode.setWorkflowNode(workflowNode);

            executableWorkNodeIdMap.put(workflowNode.getNodeId(),executableWorkNode);

            executableWorkNodes.add(executableWorkNode);
        }
    }

    private void initialConfiguration() {
        try {
            String json = FileUtil.getFileContent(this.filePath);
            workflowConfiguration = GsonUtil.jsonStrToObject(json,WorkflowConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Task getTask() {
        return task;
    }
}
