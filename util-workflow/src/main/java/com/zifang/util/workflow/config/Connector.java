package com.zifang.util.workflow.config;

import lombok.Data;

import java.util.List;

@Data
public class Connector {

    /**
     * 前置节点列表
     * */
    private List<String> pre;

    /**
     * 后置节点列表
     * */
    private List<String> post;

}
