package com.zifang.util.core.pattern.composite;

import com.zifang.util.core.pattern.composite.define.ILeaf;

import java.util.List;

/**
 * 针对很多的叶子结点而组合成的 Tree模型
 */
public class Tree {

    /**
     * 必须是根节点
     */
    private final ILeaf iLeaf;

    public Tree(ILeaf iLeaf) {

        if (iLeaf == null) {
            throw new NullPointerException("");
        }

        if (!iLeaf.isRoot()) {
            throw new RuntimeException("");
        }
        this.iLeaf = iLeaf;
    }

    public void bfsVisit() {
        bfsV(iLeaf, "");
    }

    public void bfsV(ILeaf root, String str) {

        System.out.println(str + root.getName());

        List<ILeaf> iLeaves = root.getSubLeaves();

        if (iLeaves != null) {
            for (ILeaf iLeaf : iLeaves) {
                bfsV(iLeaf, str + " ");
            }
        }
    }
}
