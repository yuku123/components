package com.zifang.util.core.pattern.composite.leaf;

import java.util.List;

/**
 * 哨兵 根节点
 */
public abstract class RootLeaf implements ILeaf {

    public abstract List<ILeaf> getSubLeaves();

    @Override
    public ILeaf getParentLeaf() {
        return null;
    }
}
