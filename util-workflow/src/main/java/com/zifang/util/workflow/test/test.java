package com.zifang.util.workflow.test;

import com.zifang.util.core.util.FileUtil;
import com.zifang.util.core.util.GsonUtil;
import com.zifang.util.workflow.config.WorkflowConfiguration;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        String s ="/Users/zifang/workplace/idea_workplace/components/util-workflow/src/main/java/workflow.json";
        String json = FileUtil.getFileContent(s);
        System.out.println(json);

        WorkflowConfiguration workflowConfiguration = GsonUtil.jsonStrToObject(json,WorkflowConfiguration.class);

        System.out.println(GsonUtil.objectToJsonStr(workflowConfiguration));

    }
}
