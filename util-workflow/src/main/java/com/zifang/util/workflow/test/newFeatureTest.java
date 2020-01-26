package com.zifang.util.workflow.test;

import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;

public class newFeatureTest {

    public void testAll(){

        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/feature/workflow_all.json";

        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
        workFlowApplicationContext.initialByLocalFilePath(filePath);
        workFlowApplicationContext.executeTask();
    }

//    public void test1All(){
//
//        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/feature/workflow_all.json";
//
//        WorkFlowApplication workFlowApplication = new WorkFlowApplicationBuilder().
//
//
//
//
//        WorkFlowApplication workFlowApplication = new WorkFlowApplication();
//
//        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
//        workFlowApplicationContext.initialByLocalFilePath(filePath);
//        workFlowApplicationContext.executeTask();
//    }

    public static void main(String[] args) {
        new newFeatureTest().testAll();
    }
}
