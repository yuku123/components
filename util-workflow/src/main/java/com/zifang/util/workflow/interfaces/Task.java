package com.zifang.util.workflow.interfaces;

import java.util.List;
import java.util.Map;

public class Task {

    WorkFlowApplicationContext workFlowApplicationContext;
    ExecutableWorkNode start;
    List<ExecutableWorkNode> executableWorkNodes;
    Map<String, ExecutableWorkNode> executableWorkNodeIdMap;

    public void exec() {
        start.exec();
    }

    public void setWorkFlowApplicationContext(WorkFlowApplicationContext workFlowApplicationContext) {
        this.workFlowApplicationContext = workFlowApplicationContext;
    }

    public void setStartExecutableWorkNode(ExecutableWorkNode start) {
        this.start = start;
    }

    public void setExecutableWorkNodes(List<ExecutableWorkNode> executableWorkNodes) {
        this.executableWorkNodes = executableWorkNodes;
    }

    public void setExecutableWorkNodeIdMap(Map<String, ExecutableWorkNode> executableWorkNodeIdMap) {
        this.executableWorkNodeIdMap = executableWorkNodeIdMap;
    }
}
