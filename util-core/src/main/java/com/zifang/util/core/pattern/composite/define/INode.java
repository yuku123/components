package com.zifang.util.core.pattern.composite.define;

import java.util.List;

/**
 * 网状结点 没有上下级别关系
 *
 * @author zifang
 */
public interface INode {

    /**
     * @return 列表
     */
    List<INode> getConbinedNode();
}