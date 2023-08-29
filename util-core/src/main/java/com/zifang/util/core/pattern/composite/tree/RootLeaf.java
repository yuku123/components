package com.zifang.util.core.pattern.composite.tree;

import java.util.List;

/**
 * 哨兵 根节点
 */
public abstract class RootLeaf implements ILeaf {

    @Override
    public abstract List<ILeaf> getSubLeaves();

    @Override
    public ILeaf getParentLeaf() {
        return null;
    }
}
