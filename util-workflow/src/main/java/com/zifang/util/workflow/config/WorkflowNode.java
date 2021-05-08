package com.zifang.util.workflow.config;

import lombok.Data;

import java.util.HashMap;

/**
 * 描述业务流节点的最小单元定义
 */
@Data
public class WorkflowNode {

    /**
     * 此节点的唯一标识号
     */
    private String nodeId;

    /**
     * 引入组别概念，逻辑上属于同种的处理单元
     */
    private String groupId;

    /**
     * 此节点的别名
     */
    private String name;

    /**
     * 描述节点的性质
     */
    private String type;

    /**
     * 处理引擎的服务标识
     */
    private String serviceUnit;

    /**
     * 处理引擎的处理实例方法
     */
    private String invokeDynamic;

    /**
     * 可以被处理引擎所识别并且转换的参数合集
     */
    private Object invokeParameter;

    /**
     * 描述此节点与其他节点的关联情况
     */
    private Connector connector;

    private HashMap<String, String> cache;


    /**
     * 防止重复添加值
     */
    public void putPost(String nodeId) {
        if (!connector.getPost().contains(nodeId) && !this.nodeId.equals(nodeId)) {
            connector.getPost().add(nodeId);
        }
    }

    /**
     * 防止重复添加值
     */
    public void putPre(String nodeId) {
        if (!connector.getPre().contains(nodeId) && !this.nodeId.equals(nodeId)) {
            connector.getPre().add(nodeId);
        }
    }
}
