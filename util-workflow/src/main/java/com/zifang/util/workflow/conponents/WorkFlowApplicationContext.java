package com.zifang.util.workflow.conponents;

import com.zifang.util.core.util.FileUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.config.WorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.interfaces.EngineFactory;
import org.apache.log4j.Logger;

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

    private static Logger logger = Logger.getLogger(WorkFlowApplicationContext.class);

    //存储所有的执行节点
    private List<ExecutableWorkflowNode> executableWorkNodes = new ArrayList<>();

    //执行引擎
    private AbstractEngine abstractEngine;

    //存储所有的执行单元，id:执行单元
    private Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap = new LinkedHashMap<>();

    //工作流的配置文件地
    private String filePath;

    //执行单元
    private Task task = new Task();

    //当前的配置信息
    private WorkflowConfiguration workflowConfiguration;

    public WorkFlowApplicationContext(String filePath){
        this.filePath = filePath;
        initial();
    }

    //初始化该上下文
    private void initial() {

        //从文件内初始化配置
        initialConfiguration();

        abstractEngine = EngineFactory.getEngine(this.workflowConfiguration.getConfigurations().getEngine());

        //转换所有的节点定义，生成可执行的松散节点，这个时候节点间没有任何关联
        transformWorkFlowNode();

        //连接已经注册好了的可执行node，这个时候将所有的节点在概念上进行逻辑关联，为后续执行做铺垫
        connectWorkFlowNode();

        //生成可执行task组件
        produceExecutableTask();
    }

    private void produceExecutableTask() {
        task.setWorkFlowApplicationContext(this);
        task.setStartExecutableWorkNode(executableWorkNodeIdMap.get("start"));
        task.setExecutableWorkNodes(executableWorkNodes);
        task.setExecutableWorkNodeIdMap(executableWorkNodeIdMap);
    }

    /**
     * 1. 初始化每个可执行的 引擎服务
     * 2. 初始化前后关联性
     * 3. 针对每个节点，生成各自的同步处理器
     * */
    private void connectWorkFlowNode() {

        // 初始化每个工作节点的执行服务单元
        initialEngineService();

        // 初始化工作节点的逻辑关联
        initialConnectionNetWord();

        // 初始化每个执行单元的同步器
        initialCountDownLatchConfiguration();

    }

    private void initialCountDownLatchConfiguration() {
        for(ExecutableWorkflowNode executableWorkNode : executableWorkNodes){

            //定义 负责处理前置节点的处理器
            CountDownLatch latch = new CountDownLatch(executableWorkNode.getPre().size());
            executableWorkNode.setCountDownLatch(latch);

        }

        for(ExecutableWorkflowNode executableWorkNode : executableWorkNodes){

            //从每个节点上，得到后置节点的所有同步器存到当前的同步器列表
            for(ExecutableWorkflowNode executableWorkNodePost : executableWorkNode.getPost()){
                executableWorkNode.getPostCountDownLatchList().add(executableWorkNodePost.getCountDownLatch());
            }

        }
    }

    private void initialConnectionNetWord() {
        for(ExecutableWorkflowNode executableWorkNode : executableWorkNodes){

            List<String> pre = executableWorkNode.getConnector().getPre();

            List<String> post = executableWorkNode.getConnector().getPost();

            //将每个前置节点内的后置节点列表增加自身
            for(String connectNodeId: pre){
                executableWorkNodeIdMap.get(connectNodeId).putPost(executableWorkNode);
            }

            //将每个后置节点的前置节点列表增加自身
            for(String connectNodeId: post){
                executableWorkNodeIdMap.get(connectNodeId).putPre(executableWorkNode);
            }
        }
    }

    private void initialEngineService() {
        for(ExecutableWorkflowNode executableWorkNode : executableWorkNodes){
            //通过每个节点自己的引擎与 执行单元， 得到真正的单元执行服务者
            AbstractEngineService abstractEngineService = executableWorkNode
                    .getAbstractEngine()
                    .getRegisteredEngineService(executableWorkNode.getServiceUnit());
            //对执行单元赋予单元执行服务者
            executableWorkNode.setAbstractEngineService(abstractEngineService);
        }
    }

    private void transformWorkFlowNode() {

        //遍历nodeList,将每一个节点的信息使用可执行node进行包装
        for(WorkflowNode workflowNode : workflowConfiguration.getWorkflowNodeList()){

            ExecutableWorkflowNode executableWorkNode = new ExecutableWorkflowNode(workflowNode);

            executableWorkNode.setEngine(abstractEngine);

            executableWorkNodeIdMap.put(workflowNode.getNodeId(),executableWorkNode);

            executableWorkNodes.add(executableWorkNode);
        }
    }

    private void initialConfiguration() {
        try {
            String json = FileUtil.getFileContent(this.filePath);
            workflowConfiguration = GsonUtil.jsonStrToObject(json,WorkflowConfiguration.class);
        } catch (IOException e) {
            logger.error("解析文件出现问题:"+filePath);
            e.printStackTrace();
        }
    }

    public void executeTask() {
        task.exec();
    }
}
