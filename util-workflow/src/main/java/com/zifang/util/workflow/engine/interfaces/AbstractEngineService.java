package com.zifang.util.workflow.engine.interfaces;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public abstract class AbstractEngineService {

    protected Map<String, String> properties;
    /**
     * 因为调用的方法的参数多种多样，因此参数不能是简单的json格式，因此都会转化为object，由工作的类对此进行改造
     * */
    protected Object invokeParameter;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    protected Dataset<Row> dataset;

    public void setProperty(Map<String, String> properties) {
        this.properties = properties;
    }

    public abstract void exec(ExecutableWorkflowNode executableWorkflowNode);


    public Dataset<Row> getDataset() {
        return dataset;
    }


    public Object getInvokeParameter() {
        return invokeParameter;
    }

    public void setInvokeParameter(Object invokeParameter) {
        this.invokeParameter = invokeParameter;
    }
}
