package com.zifang.util.core.lang.reflect;

import com.zifang.util.core.pattern.composite.leaf.ILeaf;
import com.zifang.util.core.pattern.composite.leaf.LeafHelper;
import com.zifang.util.core.pattern.composite.leaf.LeafWrapper;
import com.zifang.util.core.pattern.composite.node.INode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类解析器 提供更加方便的信息抓取工具
 *
 * @author zifang
 */
public class ClassParser {

    /**
     * 解析器缓存
     */
    private static Map<Class<?>, ClassParser> classParserCache = new LinkedHashMap<>();

    /**
     * 类继承关系
     * */
    private static Map<Class<?>, INode> classInheraNode = new LinkedHashMap<>();

    private ILeaf iLeaf;

    /**
     * 当前针对的解析的类
     * */
    private Class<?> clazz;

    public ClassParser(Class<?> clazz, boolean forceRefreshCache) {
        this.clazz = clazz;
        doParser();
    }


    public ClassParser(Class<?> clazz) {
        this(clazz, false);
    }

    public List<Field> getCurrentPublicField() {
        return null;
    }

    public List<Field> getCurrentProtectedField() {
        return null;
    }

    public List<Field> getCurrentPrivateField() {
        return null;
    }

    public List<Field> getCurrentAllField() {
        return null;
    }

    private long leafIndex = 0;

    private void doParser() {

        List<LeafWrapper<Long, Long, ClassParserInfoWrapper>> leafWrappers = loop(clazz,leafIndex);

        iLeaf = LeafHelper.treeify(leafWrappers);
    }

    private List<LeafWrapper<Long, Long, ClassParserInfoWrapper>> loop(Class clazz,long parentId) {

        List<LeafWrapper<Long, Long, ClassParserInfoWrapper>> leafWrappers = new ArrayList<>();

        long current = leafIndex;
        leafWrappers.add(LeafHelper.wrapper(current, parentId, new ClassParserInfoWrapper(clazz)));

        for(Class interfaceItem : clazz.getInterfaces()){
            ++leafIndex;
            leafWrappers.addAll(loop(interfaceItem,current));
        }

        if(clazz.getSuperclass() != null){
            ++leafIndex;
            leafWrappers.addAll(loop(clazz.getSuperclass(),current));
        }

        return leafWrappers;

    }


}
