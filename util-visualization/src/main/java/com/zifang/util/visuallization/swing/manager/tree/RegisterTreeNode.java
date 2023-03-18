package com.zifang.util.visuallization.swing.manager.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegisterTreeNode {

    private List<TreeNode> treeNodes = new ArrayList<>();

    public RegisterTreeNode register(TreeNode treeNode) {
        treeNodes.add(treeNode);
        return this;
    }
}