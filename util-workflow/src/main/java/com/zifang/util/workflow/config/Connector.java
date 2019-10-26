package com.zifang.util.workflow.config;

import java.util.List;

public class Connector {

    /**
     * 前置节点
     * */
    private List<String> pre;

    /**
     * 后置节点
     * */
    private List<String> post;


    public List<String> getPre() {
        return pre;
    }

    public void setPre(List<String> pre) {
        this.pre = pre;
    }

    public List<String> getPost() {
        return post;
    }

    public void setPost(List<String> post) {
        this.post = post;
    }
}
