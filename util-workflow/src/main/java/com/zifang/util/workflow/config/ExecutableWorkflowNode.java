package com.zifang.util.workflow.config;

import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import lombok.Getter;
import lombok.Setter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Setter
@Getter
public class ExecutableWorkflowNode extends WorkflowNode implements NodeLifeCycle {

    // 描述当前节点的状态
    private String status;

    // 描述当前节点的执行引擎
    private AbstractEngine abstractEngine;

    // 描述当前的执行单元服务
    private AbstractEngineService abstractEngineService;

    //每个可执行节点会有两种方式与其他节点交流：
    //1. 主动将数据集塞给后置节点，表现为主动遍历所有前置节点得到数据集合
    //2. 被动接收前置节点传入的，表现为获取当前节点内的datasetPre
    private Dataset<Row> dataset;//当前的数据结果集合

    private Dataset<Row> datasetPre; //上个节点强制push到这个节点的dataSet

    private CountDownLatch countDownLatch;//用于控制前置节点的

    private List<CountDownLatch> postCountDownLatchList = new ArrayList<>();//用于通知后置节点的

    /**
     * 集合所有的后置节点
     */
    private List<ExecutableWorkflowNode> post = new ArrayList<>();

    /**
     * 集合所有的前置节点
     */
    private List<ExecutableWorkflowNode> pre = new ArrayList<>();

    //是否被调用，因为会有很多个前置节点同时进行调用，那么就只会有一个节点会成功发起请求
    //真正开始执行需要使用countDownLatch 进行判断是否已经同时到达这个栏栅
    //0是没有被call,1是被call了
    private volatile int isCalled = 0;

    //初始化，将节点信息同步到可执行node内部
    public ExecutableWorkflowNode(WorkflowNode workflowNode) {
        super.setNodeId(workflowNode.getNodeId());
        super.setConnector(workflowNode.getConnector());
        super.setGroupId(workflowNode.getGroupId());
        super.setName(workflowNode.getName());
        super.setInvokeParameter(workflowNode.getInvokeParameter());
        super.setServiceUnit(workflowNode.getServiceUnit());
        super.setInvokeDynamic(workflowNode.getInvokeDynamic());
        super.setType(workflowNode.getType());
        super.setCache(workflowNode.getCache());
        this.setStatus(NodeLifeCycle.PREPARED);
        this.init();
    }

    //开始执行
    public void exec() {
        //默认为同步执行操作
        blockExec();
    }

    private synchronized void blockExec() {

        //当当前节点是准备状态就开始执行当前的任务
        if (PREPARED.equals(status)) {
            //为当前节点的执行服务设置参数，并执行
            abstractEngineService.setInvokeParameter(getInvokeParameter());
            abstractEngineService.exec(this);
            dataset = abstractEngineService.getDataset();

            for (CountDownLatch countDownLatch : postCountDownLatchList) {
                countDownLatch.countDown();
            }

            for (ExecutableWorkflowNode executableWorkNode : post) {
                //将当前的结果强塞给下一个节点
                executableWorkNode.setDatasetPre(dataset);

                //如果下个节点已经准备好执行条件
                if (executableWorkNode.getCountDownLatch().getCount() == 0) {
                    executableWorkNode.exec();
                }
            }

        } else if (EXECUTED.equals(status)) {
            //如果当前是已经执行过了的，就不真正的去执行当前指令

            //将当前的结果再强制往后面塞一遍，并去调用下一个节点，执行权交由下个节点
            for (ExecutableWorkflowNode executableWorkNode : post) {

                //将当前的结果强塞给下一个节点
                executableWorkNode.setDatasetPre(dataset);

                //如果下个节点已经准备好执行条件
                if (executableWorkNode.getCountDownLatch().getCount() == 0) {
                    executableWorkNode.exec();
                }
            }
        }
    }

    /**
     * 防止重复添加值
     */
    public void putPost(ExecutableWorkflowNode executableWorkNode) {
        if (!post.contains(executableWorkNode)) {
            post.add(executableWorkNode);
        }
    }

    /**
     * 防止重复添加值
     */
    public void putPre(ExecutableWorkflowNode executableWorkNode) {
        if (!pre.contains(executableWorkNode)) {
            pre.add(executableWorkNode);
        }
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute() {

    }

    @Override
    public void init() {

    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
