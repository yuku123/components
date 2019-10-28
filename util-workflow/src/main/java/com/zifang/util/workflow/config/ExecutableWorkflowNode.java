package com.zifang.util.workflow.config;

import com.zifang.util.workflow.engine.AbstractEngine;
import com.zifang.util.workflow.interfaces.AbstractEngineService;
import com.zifang.util.workflow.interfaces.WorkFlowApplication;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class ExecutableWorkflowNode extends WorkflowNode{

    private AbstractEngine abstractEngine;

    private AbstractEngineService abstractEngineService;

    private Dataset<Row> dataset;

    private CountDownLatch countDownLatch;//用于控制前置节点的
    private List<CountDownLatch> postCountDownLatchList = new ArrayList<>();//用于通知后置节点的

    private Future<Integer> back;

    private List<ExecutableWorkflowNode> post = new ArrayList<>();
    private List<ExecutableWorkflowNode> pre = new ArrayList<>();

    //是否被调用，因为会有很多个前置节点同时进行调用，那么就只会有一个节点会成功发起请求
    //真正开始执行需要使用countDownLatch 进行判断是否已经同时到达这个栏栅
    //0是没有被call,1是被call了
    private volatile int isCalled = 0;

    //初始化，将节点信息同步到可执行node内部
    public ExecutableWorkflowNode(WorkflowNode workflowNode){
        super.setNodeId(workflowNode.getNodeId());
        super.setConnector(workflowNode.getConnector());
        super.setGroupId(workflowNode.getGroupId());
        super.setName(workflowNode.getName());
        super.setProperties(workflowNode.getProperties());
        super.setServiceUnit(workflowNode.getServiceUnit());
        super.setType(workflowNode.getType());
    }

    public int exec() {
        abstractEngineService.setProperty(getProperties());
        abstractEngineService.exec();
        dataset = abstractEngineService.getDataset();

        for(CountDownLatch countDownLatch : postCountDownLatchList){
            countDownLatch.countDown();
        }

        for(ExecutableWorkflowNode executableWorkNode : post){
            final ExecutableWorkflowNode executableWorkflowNodeTemp = executableWorkNode;
            if(executableWorkNode.getCountDownLatch().getCount() == 0){
                executableWorkflowNodeTemp.exec();
            }
        }

        return 1;
    }

    public void putPost(ExecutableWorkflowNode executableWorkNode) {
        if(!post.contains(executableWorkNode)){
            post.add(executableWorkNode);
        }
    }

    public void putPre(ExecutableWorkflowNode executableWorkNode) {
        if(!pre.contains(executableWorkNode)){
            pre.add(executableWorkNode);
        }
    }

    public List<ExecutableWorkflowNode> getPost() {
        return post;
    }

    public void setPost(List<ExecutableWorkflowNode> post) {
        this.post = post;
    }

    public List<ExecutableWorkflowNode> getPre() {
        return pre;
    }

    public void setPre(List<ExecutableWorkflowNode> pre) {
        this.pre = pre;
    }

    public Future<Integer> getBack() {
        return back;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public List<CountDownLatch> getPostCountDownLatchList() {
        return postCountDownLatchList;
    }

    public void setPostCountDownLatchList(List<CountDownLatch> postCountDownLatchList) {
        this.postCountDownLatchList = postCountDownLatchList;
    }

    public AbstractEngine getAbstractEngine() {
        return abstractEngine;
    }

    public void setAbstractEngine(AbstractEngine abstractEngine) {
        this.abstractEngine = abstractEngine;
    }

    public AbstractEngineService getAbstractEngineService() {
        return abstractEngineService;
    }

    public void setEngine(AbstractEngine abstractEngine) {
        this.abstractEngine = abstractEngine;
    }

    public void setAbstractEngineService(AbstractEngineService abstractEngineService) {
        this.abstractEngineService = abstractEngineService;
    }
}
