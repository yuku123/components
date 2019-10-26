package com.zifang.util.workflow.config;

import java.util.Map;

/**
 * 描述业务流节点的最小单元定义
 * */
public class WorkflowNode {

    /**
     * 此节点的唯一标识号
     * */
    private String nodeId;

    /**
     * 引入组别概念，逻辑上属于同种的处理单元
     * */
    private String groupId;

    /**
     * 此节点的别名
     * */
    private String name;

    /**
     * 描述节点的性质
     * */
    private String type;

    /**
     * 处理引擎的服务标识
     * */
    private String serviceUnit;

    /**
     * 可以被处理引擎所识别并且转换的参数合集
     * */
    private Map<String,String> properties;

    /**
     * 描述此节点与其他节点的关联情况
     * */
    private Connector connector;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }
}
