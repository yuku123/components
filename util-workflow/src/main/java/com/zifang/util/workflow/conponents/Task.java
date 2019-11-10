package com.zifang.util.workflow.conponents;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Task {

    private WorkFlowApplicationContext workFlowApplicationContext;

    private ExecutableWorkflowNode start;

    private List<ExecutableWorkflowNode> executableWorkNodes;

    private Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap;

    public void exec() {
        start.exec();
    }

}
