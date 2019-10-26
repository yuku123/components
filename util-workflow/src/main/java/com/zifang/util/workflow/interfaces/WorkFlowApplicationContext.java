package com.zifang.util.workflow.interfaces;

import com.zifang.util.core.interfaces.AbstractEngine;
import com.zifang.util.core.util.FileUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.engine.EngineFactory;

import java.io.IOException;

/**
 * 每个工作流的上下文，是工作引擎的工作子单元。工作引擎只负责发布命令，调度资源，调度任务相关功能
 * */
public class WorkFlowApplicationContext {

    private Task task;
    private AbstractEngine abstractEngine;

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

    }

    private void initialConfiguration() {
        try {
            String json = FileUtil.getFileContent(this.filePath);
            workflowConfiguration = GsonUtil.jsonStrToObject(json,WorkflowConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Task getTask() {
        return task;
    }
}
