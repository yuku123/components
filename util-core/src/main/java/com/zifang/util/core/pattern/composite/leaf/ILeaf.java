package com.zifang.util.core.pattern.composite.leaf;

import java.util.List;
import java.util.Objects;

/**
 * 叶子结点
 * */
public interface ILeaf {

    String ROOT = "ROOT";

    /**
     *  获得得到下属的叶子结点
     */
    List<ILeaf> getSubLeaves();

    /**
     * 得到父亲的叶子结点
     * */
    ILeaf getParentLeaf();

    /**
     * 判定是否为根结点
     * */
    default boolean isRoot(){
        return Objects.isNull(getParentLeaf());
    }

    /**
     * 得到当前的结点名字
     * */
    default String getName(){
        return this.getClass().getSimpleName()+"@"+this.hashCode();
    }

    /**
     * 得到当前的唯一标识号
     *
     * */
    default String getId(){
        return this.getClass().getSimpleName()+"@"+this.hashCode();
    }

    /**
     * 描述当前这个树的情况
     * */
    default String describe(){
        return this.toString(); // Todo
    }

    /**
     * 往叶子结点 增加子叶节点
     * */
    void appendSubLeaf(ILeaf leaf);

    /**
     * 叶子结点有能力接受 父节点
     * */
    void setParent(ILeaf leaf);
}
