package com.zifang.util.workflow.interfaces;

import com.zifang.util.core.interfaces.AbstractEngine;
import com.zifang.util.workflow.config.WorkflowNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExecutableWorkNode {

    private String nodeId;
    private AbstractEngine abstractEngine;
    private AbstractEngineService abstractEngineService;
    private WorkflowNode workflowNode;
    private Dataset<Row> dataset;

    private CountDownLatch countDownLatch;//用于控制前置节点的
    private List<CountDownLatch> postCountDownLatchList;//用于通知后置节点的

    private Future<Integer> back;

    private List<ExecutableWorkNode> post = new ArrayList<>();
    private List<ExecutableWorkNode> pre = new ArrayList<>();

    //是否被调用，因为会有很多个前置节点同时进行调用，那么就只会有一个节点会成功发起请求
    //真正开始执行需要使用countDownLatch 进行判断是否已经同时到达这个栏栅
    //0是没有被call,1是被call了
    private volatile int isCalled = 0;

    public void setEngine(AbstractEngine abstractEngine) {
        this.abstractEngine = abstractEngine;
    }

    public void setAbstractEngineService(AbstractEngineService abstractEngineService) {
        this.abstractEngineService = abstractEngineService;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setWorkflowNode(WorkflowNode workflowNode) {
        this.workflowNode = workflowNode;
    }

    public WorkflowNode getWorkflowNode() {
        return workflowNode;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void putPost(ExecutableWorkNode executableWorkNode) {
        if(!post.contains(executableWorkNode)){
            post.add(executableWorkNode);
        }
    }

    public void putPre(ExecutableWorkNode executableWorkNode) {
        if(!pre.contains(executableWorkNode)){
            pre.add(executableWorkNode);
        }
    }

    public List<ExecutableWorkNode> getPost() {
        return post;
    }

    public void setPost(List<ExecutableWorkNode> post) {
        this.post = post;
    }

    public List<ExecutableWorkNode> getPre() {
        return pre;
    }

    public void setPre(List<ExecutableWorkNode> pre) {
        this.pre = pre;
    }

    public int exec() {

        if(isCalled == 0){
            synchronized (this){
                isCalled = 1;
            }
        }else{
            //没有意义，就是纯粹的跳出方法
            return 1;
        }

        //等到前置节点到这个地方
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        //得到所有的前处理结果
//        for(ExecutableWorkNode executableWorkNode : pre){
//            try {
//                if(executableWorkNode.getBack()!=null){
//                    executableWorkNode.getBack().get();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }

        //生成当前处理的future
        back =  WorkFlowApplication.threadPool.submit(()->{
            abstractEngineService.setProperty(workflowNode.getProperties());
            abstractEngineService.exec();
            dataset = abstractEngineService.getDataset();

            //对所有的后置节点进行更新信息
            for(CountDownLatch countDownLatch : postCountDownLatchList){
                countDownLatch.countDown();
            }
            return 1;

        });

        handlePass();

        //无意义
        //之后要引入生命周期概念，目前预留
        return 1;
    }

    public void handlePass(){
        for(ExecutableWorkNode executableWorkNode : post){
             WorkFlowApplication.threadPool.submit(()->executableWorkNode.exec());
        }
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
}
