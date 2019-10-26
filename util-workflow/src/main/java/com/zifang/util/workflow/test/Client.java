package com.zifang.util.workflow.test;

import com.zifang.util.workflow.interfaces.Task;
import com.zifang.util.workflow.interfaces.WorkFlowApplicationContext;

public class Client {

    //

    public static void main(String[] args) {
        String filePath = "util-workflow/src/main/resources/workflow1.json";

        //一个workflow 对应
        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext(filePath);

        Task task = workFlowApplicationContext.getTask();

        task.exec();
        //Task
    }
}
