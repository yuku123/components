package com.zifang.util.core.pattern.composite.define;

import java.util.List;

public class AbstractNode implements INode {

    protected String id;

    protected String name;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public List<INode> getConbinedNode() {
        return null;
    }
}
