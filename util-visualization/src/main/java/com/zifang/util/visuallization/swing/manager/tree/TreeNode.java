package com.zifang.util.visuallization.swing.manager.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeNode {
    private String id;
    private String name;
    private String parentId;
}