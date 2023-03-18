package com.zifang.util.visuallization.swing.manager.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterTreeNodeHelper {
    public static DefaultMutableTreeNode solve(RegisterTreeNode registerTreeNode) {

        List<TreeNode> roots = registerTreeNode.getTreeNodes().stream().filter(e -> null == e.getParentId()).collect(Collectors.toList());

        if (roots.size() != 1) {
            throw new RuntimeException("有多个根节点或者游离节点");
        }

        TreeNode root = roots.get(0);// 得到根节点
        DefaultMutableTreeNode rootDefaultMutableTreeNode = getDefaultMutableTreeNodeByTreeNode(root); //通过根节点得到节点数据

        solveRoot(root, rootDefaultMutableTreeNode, registerTreeNode.getTreeNodes());
        // 找到根节点

        return rootDefaultMutableTreeNode;
    }

    private static DefaultMutableTreeNode getDefaultMutableTreeNodeByTreeNode(TreeNode root) {
        return new DefaultMutableTreeNode(root.getName());
    }

    private static void solveRoot(TreeNode root, DefaultMutableTreeNode rootDefaultMutableTreeNode, List<TreeNode> registerTreeNode) {
        // 得到这个集合的下等级别的数据
        String id = root.getId();
        List<TreeNode> treeNodes = registerTreeNode.stream().filter(e -> id.equals(e.getParentId())).collect(Collectors.toList());
        for (TreeNode treeNode : treeNodes) {
            DefaultMutableTreeNode defaultMutableTreeNode = getDefaultMutableTreeNodeByTreeNode(treeNode);
            rootDefaultMutableTreeNode.add(defaultMutableTreeNode);
            solveRoot(treeNode, defaultMutableTreeNode, registerTreeNode);
        }
    }
}
