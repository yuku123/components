package com.zifang.util.workflow.engine.interfaces;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@Setter
@Getter
public abstract class AbstractEngineService {

    /**
     * 每个执行服务都要传入执行上下文
     */
    private WorkFlowApplicationContext workFlowApplicationContext;

    /**
     * 因为调用的方法的参数多种多样，因此参数不能是简单的json格式，因此都会转化为object，由工作的类对此进行改造
     */
    protected Object invokeParameter;

    /**
     * 一个service只会包裹一个dataset
     */
    protected Dataset<Row> dataset;

    /**
     * 执行引擎服务的执行
     */
    public abstract void exec(ExecutableWorkflowNode executableWorkflowNode);

}
