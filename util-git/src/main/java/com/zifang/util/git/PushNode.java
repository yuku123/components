package com.zifang.util.git;

import org.gitlab4j.api.models.CommitAction;

/**
 * 可提交 到gitlab上的文件结点
 * */
public interface PushNode {
    String gitPath();
    String content();
    CommitAction.Action getCommitType();// 提交的种类 是update 还是 create
}
