package com.zifang.util.workflow.engine.spark.impl;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import com.zifang.util.bigdata.spark.util.SparkUtil;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractSparkEngineService extends AbstractEngineService {

    protected SparkContextInstance sparkContextInstance;
    protected SparkUtil sparkUtil;
    protected ExecutableWorkflowNode executableWorkflowNode;

    private static String defaultInvokeDynamicMethod = "defaultHandler";

    public void setSparkContextInstance(SparkContextInstance sparkContextInstance){
        this.sparkContextInstance = sparkContextInstance;
        sparkUtil = new SparkUtil(this.sparkContextInstance);
    }

    @Override
    public void exec(ExecutableWorkflowNode executableWorkflowNode) {
        this.executableWorkflowNode = executableWorkflowNode;
        String invokeDynamic = executableWorkflowNode.getInvokeDynamic();
        if(invokeDynamic == null){
            invokeDynamic = defaultInvokeDynamicMethod;
        }
        try {
            Method method = this.getClass().getMethod(invokeDynamic);
            method.invoke(this);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public abstract void defaultHandler();
}
