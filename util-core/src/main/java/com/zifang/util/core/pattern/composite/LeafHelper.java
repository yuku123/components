package com.zifang.util.core.pattern.composite;

import com.zifang.util.core.pattern.composite.define.ILeaf;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 针对 叶子结点的操作
 *
 * @author zifang
 */
public class LeafHelper {

    /**
     * 转换 平铺的叶子结点 得到一个根节点
     *
     * @param leaves 堆放在一起的叶子结点
     */
    public static ILeaf pickRootLeaf(List<? extends ILeaf> leaves) {

        if (leaves == null) {
            throw new NullPointerException();
        }

        return leaves.stream().filter(ILeaf::isRoot).findFirst().orElse(null);
    }

    /**
     * 是否包含游离的根节点，就是说这么多结点内 有多个根节点的意思
     */
    public static boolean hasDissociateLeaf(List<? extends ILeaf> leaves) {

        if (leaves == null) {
            throw new NullPointerException();
        }

        return leaves.stream().filter(ILeaf::isRoot).count() > 1;
    }

    /**
     * 获得 平铺的叶子结点(里面可能包含多个叶子根结点) 里面的所有的根节点串
     */
    public static List<ILeaf> solveStackLeaves(List<ILeaf> leaves) {
        if (leaves == null) {
            throw new NullPointerException();
        }
        return leaves.stream()
                .filter(ILeaf::isRoot)
                .collect(Collectors.toList());
    }

    /**
     * 对一般化object 进行leaf的包装
     */
    public static <ID, C> LeafWrapper<ID, ID, C> wrapper(ID currentId, ID parentId, C bean) {
        return new LeafWrapper<>(currentId, parentId, bean);
    }

    /**
     * 树化结点包装
     */
    public static <ID, C> LeafWrapper<ID, ID, C> treeify(List<LeafWrapper<ID, ID, C>> leafWrappers) {
        Map<ID, LeafWrapper<ID, ID, C>> leafWrapperMap = leafWrappers.stream().collect(Collectors.toMap(LeafWrapper::getA, e -> e));

        LeafWrapper<ID, ID, C> root = null;
        for (LeafWrapper<ID, ID, C> leafWrapper : leafWrappers) {
            LeafWrapper parent = leafWrapperMap.get(leafWrapper.getB());
            if (parent == null) {
                root = leafWrapper;
            } else {
                leafWrapper.setParent(leafWrapper);
                parent.appendSubLeaf(leafWrapper);
            }
        }
        return root;
    }
}
