package com.zifang.util.workflow.interfaces;

import com.zifang.util.core.interfaces.AbstractEngine;
import com.zifang.util.workflow.config.WorkflowNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

public class ExecutableWorkNode {

    private String nodeId;
    private AbstractEngine abstractEngine;
    private AbstractEngineService abstractEngineService;
    private WorkflowNode workflowNode;
    private Dataset<Row> dataset;

    private List<ExecutableWorkNode> post = new ArrayList<>();
    private List<ExecutableWorkNode> pre = new ArrayList<>();

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

    public void exec() {
        abstractEngineService.setProperty(workflowNode.getProperties());
        abstractEngineService.exec();
        dataset = abstractEngineService.getDataset();
        handlePass();
    }

    public void handlePass(){
        for(ExecutableWorkNode executableWorkNode : post){
            executableWorkNode.exec();
//            Runnable runnable = () -> executableWorkNode.exec();
//            new Thread(runnable).start();
        }

    }
}
