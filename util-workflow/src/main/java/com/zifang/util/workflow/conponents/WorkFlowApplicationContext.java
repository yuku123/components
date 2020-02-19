package com.zifang.util.workflow.conponents;

import com.zifang.util.core.io.FileUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.config.WorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.interfaces.EngineFactory;
import com.zifang.util.workflow.engine.spark.CacheEngineService;
import lombok.Data;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每个工作流的上下文，是工作引擎的工作子单元。工作引擎只负责发布命令，调度资源，调度任务相关功能
 * */
@Data
public class WorkFlowApplicationContext {

    private static Logger logger = Logger.getLogger(WorkFlowApplicationContext.class);

    private volatile AtomicInteger nodeId = new AtomicInteger(0);

    public static Random random = new Random(System.currentTimeMillis());

    private Integer workFlowApplicationContextId;

    //执行引擎
    private AbstractEngine abstractEngine;

    //缓存引擎
    private CacheEngineService cacheEngineService;

    //存储元配置节点信息映射表
    private Map<String,WorkflowNode> workflowNodeMap;

    //存储所有的执行节点
    private List<ExecutableWorkflowNode> executableWorkNodes = new ArrayList<>();

    //存储所有的执行单元，id:执行单元
    private Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap = new LinkedHashMap<>();

    //工作流的配置文件地址
    private String filePath;

    //执行单元
    private Task task = new Task();

    //当前的配置信息,最重要的核心元配置，所有的与执行节点相关的信息全部包裹在执行单元内
    private WorkflowConfiguration workflowConfiguration;

    public WorkFlowApplicationContext(){
    }

    public WorkFlowApplicationContext(String filePath){
        this.filePath = filePath;
        initial();
    }

    public void initialByLocalFilePath(String filePath){

        try {
            String json = FileUtil.getFileContent(filePath);
            workflowConfiguration = GsonUtil.jsonStrToObject(json,WorkflowConfiguration.class);
        } catch (IOException e) {
            logger.error("解析文件出现问题:"+ this.filePath);
            e.printStackTrace();
        }
        initial();
    }

    public void initialByWorkflowConfigurationInstance(WorkflowConfiguration workflowConfiguration){
        this.workflowConfiguration = workflowConfiguration;
        initial();
    }

    public void initialByJsonStream(String json){
        workflowConfiguration = GsonUtil.jsonStrToObject(json,WorkflowConfiguration.class);
        initial();
    }

    //初始化该上下文
    private void initial() {

        // 使用各种规则判断这个入参是否是正常的
        validate();

        //初始化引擎
        initialEngine();

        //初始化缓存引擎
        initialCacheEngine();

        //转换所有的节点定义，生成可执行的松散节点，这个时候节点间没有任何关联
        transformWorkFlowNode();

        //连接已经注册好了的可执行node，这个时候将所有的节点在概念上进行逻辑关联，为后续执行做铺垫
        connectWorkFlowNode();

        //生成可执行task组件
        produceExecutableTask();
    }

    private void initialCacheEngine() {
        //暂时使用默认的Cache执行引擎
        cacheEngineService = new CacheEngineService(workflowConfiguration.getConfigurations().getCacheEngine());
    }

    private void validate() {

    }

    private void initialEngine() {
        //初始化引擎,这个方法可能面临更新
        if(abstractEngine == null){
            abstractEngine = EngineFactory.getEngine(this.workflowConfiguration.getConfigurations().getEngine());
        }
    }

    private void produceExecutableTask() {
        task.setWorkFlowApplicationContext(this);
        task.setStart(executableWorkNodeIdMap.get("start"));
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
            if (executableWorkNode.getAbstractEngineService()==null){
                //通过每个节点自己的引擎与 执行单元， 得到真正的单元执行服务者
                AbstractEngineService abstractEngineService = executableWorkNode
                        .getAbstractEngine()
                        .getRegisteredEngineService(executableWorkNode.getServiceUnit());
                //对执行上下文 放入当前的实例
                abstractEngineService.setWorkFlowApplicationContext(this);
                //对执行单元赋予单元执行服务者
                executableWorkNode.setAbstractEngineService(abstractEngineService);
            }
        }
    }

    private void transformWorkFlowNode() {

        //遍历nodeList,将每一个节点的信息使用可执行node进行包装
        for(WorkflowNode workflowNode : workflowConfiguration.getWorkflowNodeList()){

            String nodeId = workflowNode.getNodeId();

            //只有当executableWorkNodeIdMap 不存在 这个可执行Node的情况下，才可以真正地添加 节点到可执行列表内
            if(!executableWorkNodeIdMap.containsKey(nodeId)){

                ExecutableWorkflowNode executableWorkNode = new ExecutableWorkflowNode(workflowNode);

                executableWorkNode.setAbstractEngine(abstractEngine);

                // 可执行节点的对照表
                executableWorkNodeIdMap.put(workflowNode.getNodeId(),executableWorkNode);

                //可执行节点列表
                executableWorkNodes.add(executableWorkNode);
            }
        }
    }

    public void executeTask() {
        task.exec();
    }


    /**
     * 返回当前的上下文的全量信息
     * */
    public Object getDescriptionMsg(){
        return null;
    }

    /**
     * 更新源信息，并相互关联
     * */
    public void refreshWorkflowConfiguration() {

        //需要更新一下辅助用的map: nodeId:workflowNode
        refreshHelperMapList();

        // 使用各种规则判断这个入参是否是正常的
        validate();

        for(WorkflowNode workflowNode : workflowConfiguration.getWorkflowNodeList()){

            List<String> pre = workflowNode.getConnector().getPre();

            List<String> post = workflowNode.getConnector().getPost();

            //将每个前置节点内的后置节点列表增加自身
            for(String connectNodeId: pre){
                workflowNodeMap.get(connectNodeId).putPost(workflowNode.getNodeId());
            }

            //将每个后置节点的前置节点列表增加自身
            for(String connectNodeId: post){
                executableWorkNodeIdMap.get(connectNodeId).putPre(connectNodeId);
            }
        }
    }

    private synchronized void refreshHelperMapList() {
        Map<String,WorkflowNode> workflowNodeMap = new LinkedHashMap<>();
        for(WorkflowNode workflowNode : workflowConfiguration.getWorkflowNodeList()){
            workflowNodeMap.put(workflowNode.getNodeId(),workflowNode);
        }
        this.workflowNodeMap = workflowNodeMap;
    }

    public String produceNodeId() {
        return String.valueOf(nodeId.getAndIncrement());
    }

    public void refreshExecutableNodeByWorkflowConfiguration() {
        transformWorkFlowNode();
        connectWorkFlowNode();
    }


    public void replaceWorkflowNode(WorkflowNode workflowNode) {

        Integer nodeId = Integer.parseInt(workflowNode.getNodeId());

        List<WorkflowNode> workflowNodes = workflowConfiguration.getWorkflowNodeList();

        //先删除
        workflowNodes.removeIf( e -> nodeId.equals(e.getNodeId()));

        //再增加
        workflowNodes.add(workflowNode);
    }
}
