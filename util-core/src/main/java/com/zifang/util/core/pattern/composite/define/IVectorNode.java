package com.zifang.util.core.pattern.composite.define;

import java.util.List;

/**
 * 在网状结构体的 基础上 增加方向属性
 */
public interface IVectorNode extends INode {

    List<IVectorNode> getInputVectorNode();

    @Override
    List<INode> getConbinedNode();


    List<IVectorNode> getOutputVectorNode();

}
