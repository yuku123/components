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
import java.util.concurrent.CountDownLatch;

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

        //针对每个节点，生成关联
        //针对每个节点，生成各自的同步处理器
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
            //定义 负责处理前置节点的处理器
            CountDownLatch latch = new CountDownLatch(pre.size());
            executableWorkNode.setCountDownLatch(latch);

            //初始化对后置节点的处理器
            executableWorkNode.setPostCountDownLatchList(new ArrayList<>());
        }

        //完成后置节点的处理器的连接
        for(ExecutableWorkNode executableWorkNode : executableWorkNodes){
            //从每个节点上，得到后置节点的所有同步器存到当前的同步器列表
            for(ExecutableWorkNode executableWorkNode1 : executableWorkNode.getPost()){
                executableWorkNode.getPostCountDownLatchList().add(executableWorkNode1.getCountDownLatch());
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

    public void executeTask() {
        task.exec();
    }
}
