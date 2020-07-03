package com.zifang.util.core.pattern.composite.leaf;

/**
 * 针对很多的叶子结点而组合成的 Tree模型
 * */
public class Tree {

    /**
     * 必须是根节点
     * */
    private ILeaf iLeaf;

    public Tree(ILeaf iLeaf){

        if(iLeaf == null){
            throw new NullPointerException("");
        }

        if(!iLeaf.isRoot()){
            throw new RuntimeException("");
        }
        this.iLeaf = iLeaf;
    }
}
