package com.zifang.util.core.pattern.composite.define;

import java.util.Collection;

public abstract class AbstractCluster implements ICluster{

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Collection<INode> members() {
        return null;
    }
}
