package com.zifang.util.core.pattern.composite.leaf;

import com.zifang.util.core.lang.tuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class LeafWrapper<A,B,C> extends Triplet<A,B,C> implements ILeaf{

    private ILeaf parent;

    private List<ILeaf> subLeaves;

    public LeafWrapper(A currentId, B parentId, C bean){
        super(currentId,parentId,bean);
    }

    @Override
    public List<ILeaf> getSubLeaves() {
        return subLeaves;
    }

    @Override
    public ILeaf getParentLeaf() {
        return parent;
    }

    public B getParentId(){
        return getB();
    }


    public C getBean(){
        return getC();
    }

    @Override
    public void appendSubLeaf(ILeaf leaf){
        if(subLeaves == null){
            subLeaves = new ArrayList<>();
        }
        subLeaves.add(leaf);
    }

    @Override
    public void setParent(ILeaf leaf) {
        parent = leaf;
    }
}
