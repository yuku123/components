package com.zifang.util.core.pattern.composite.node;

import java.util.List;

/**
 * 网状结点 没有上下级别关系
 */
public interface INode {

    /**
     * @return
     */
    List<INode> getConbinedNode();

}
