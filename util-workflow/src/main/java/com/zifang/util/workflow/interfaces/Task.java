package com.zifang.util.workflow.interfaces;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;

import java.util.List;
import java.util.Map;

public class Task {

    private WorkFlowApplicationContext workFlowApplicationContext;

    private ExecutableWorkflowNode start;

    private List<ExecutableWorkflowNode> executableWorkNodes;

    private Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap;

    public void exec() {
        start.exec();
    }

    public void setWorkFlowApplicationContext(WorkFlowApplicationContext workFlowApplicationContext) {
        this.workFlowApplicationContext = workFlowApplicationContext;
    }

    public void setStartExecutableWorkNode(ExecutableWorkflowNode start) {
        this.start = start;
    }

    public void setExecutableWorkNodes(List<ExecutableWorkflowNode> executableWorkNodes) {
        this.executableWorkNodes = executableWorkNodes;
    }

    public void setExecutableWorkNodeIdMap(Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap) {
        this.executableWorkNodeIdMap = executableWorkNodeIdMap;
    }
}
