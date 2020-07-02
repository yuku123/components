package com.zifang.util.core.pattern.composite.leaf;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 针对 叶子结点的操作
 * */
public class LeafHelper {

    /**
     * 转换 平铺的叶子结点 得到一个根节点
     *
     * @param leaves 堆放在一起的叶子结点
     * */
    public static ILeaf pickRootLeaf(List<? extends ILeaf> leaves){

        if(leaves == null){
            throw new NullPointerException();
        }

        return leaves.stream().filter(ILeaf::isRoot)
                .findFirst().orElse(null);
    }

    /**
     * 是否包含游离的根节点，就是说这么多结点内 有多个根节点的意思
     *
     * */
    public static boolean hasDissociateLeaf(List<? extends ILeaf> leaves){

        if(leaves == null){
            throw new NullPointerException();
        }

        return leaves.stream()
                .filter(ILeaf::isRoot).count() > 1 ;
    }

    /**
     * 获得 平铺的叶子结点(里面可能包含多个叶子根结点) 里面的所有的根节点串
     *
     * */
    public static List<ILeaf> solveStackLeaves(List<ILeaf> leaves){
        if(leaves == null){
            throw new NullPointerException();
        }
        return leaves.stream()
                .filter(ILeaf::isRoot)
                .collect(Collectors.toList());
    }

    /**
     * 对一般化object 进行leaf的包装
     * */
    public static <A,B,C> LeafWrapper<A,B,C> wrapper(A currentId, B parentId, C bean){
        return new LeafWrapper<>(currentId,parentId,bean);
    }

    /**
     * 组织LeafWrapper的list,进行注入
     * */
    public static <A,B,C> Tree solveLeafWrapperList(List<LeafWrapper<A,B,C>> leafWrappers){
        Map<A,LeafWrapper> leafWrapperMap = leafWrappers.stream()
                .collect(Collectors.toMap(LeafWrapper::getA,LeafWrapper -> LeafWrapper));

        LeafWrapper root = null;
        for(LeafWrapper leafWrapper : leafWrappers){
            ILeaf parent = leafWrapperMap.get(leafWrapper.getParentId());
            if(parent == null){
                root = leafWrapper;
            } else{
                leafWrapper.setParent(leafWrapper);
                parent.appendSubLeaf(leafWrapper);
            }

        }

        return new Tree(root);
    }
}
