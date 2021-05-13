package com.zifang.util.core.lang.reflect;

import com.zifang.util.core.pattern.composite.leaf.LeafHelper;
import com.zifang.util.core.pattern.composite.leaf.LeafWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 类解析器 提供更加方便的信息抓取工具
 *
 * @author zifang
 */
public class ClassParser {

    /**
     * 解析器缓存
     */
    private static final Map<Class<?>, ClassParser> classParserCache = new ConcurrentHashMap<>();

    /**
     * 类继承关系
     * */
    private static final Map<Class<?>, LeafWrapper<Long,Long,ClassParserInfoWrapper>> classInheritableNode = new ConcurrentHashMap<>();


    /**
     * 当前针对的解析的类
     * */
    private Class<?> clazz;

    /**
     * 解析过程使用的参变量
     * */
    private Long leafIndex;


    /**
     * @param clazz 需要解析的类
     * @param forceRefreshCache 是否需要强制刷新缓存
     */
    public ClassParser(Class<?> clazz, boolean forceRefreshCache) {

        this.clazz = clazz;

        LeafWrapper<Long, Long, ClassParserInfoWrapper> leafWrapper = doParser();

        if(forceRefreshCache){
            classInheritableNode.put(clazz,leafWrapper);
            classParserCache.put(clazz,this);
        } else {
            classInheritableNode.putIfAbsent(clazz,leafWrapper);
            classParserCache.putIfAbsent(clazz,this);
        }

    }

    public ClassParser(Class<?> clazz) {
        this(clazz, false);
    }

    public List<Field> getCurrentPublicField() {
        return getCurrentAllField().stream().filter(e-> Modifier.isPublic(e.getModifiers())).collect(Collectors.toList());
    }

    public List<Field> getCurrentProtectedField() {
        return getCurrentAllField().stream().filter(e-> Modifier.isProtected(e.getModifiers())).collect(Collectors.toList());
    }

    public List<Field> getCurrentPrivateField() {
        return getCurrentAllField().stream().filter(e-> Modifier.isPrivate(e.getModifiers())).collect(Collectors.toList());
    }

    public List<Field> getCurrentAllField() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    public List<Method> getCurrentProtectedMethod() {
        return getCurrentAllMethod().stream().filter(e->Modifier.isProtected(e.getModifiers())).collect(Collectors.toList());
    }

    public List<Method> getCurrentPublicMethod() {
        return getCurrentAllMethod().stream().filter(e->Modifier.isPublic(e.getModifiers())).collect(Collectors.toList());
    }


    public List<Method> getCurrentDefaultMethod() {
        return getCurrentAllMethod().stream().filter(e->e.getModifiers() == 0).collect(Collectors.toList());
    }



    public List<Method> getCurrentPrivateMethod() {
        return getCurrentAllMethod().stream().filter(e->Modifier.isPrivate(e.getModifiers())).collect(Collectors.toList());
    }

    public List<Method> getCurrentAllMethod() {
        return Arrays.asList(clazz.getDeclaredMethods());
    }


    private LeafWrapper<Long, Long, ClassParserInfoWrapper> doParser() {

        List<LeafWrapper<Long, Long, ClassParserInfoWrapper>> leafWrappers = loop(clazz,leafIndex);

        return  LeafHelper.treeify(leafWrappers);
    }

    private List<LeafWrapper<Long, Long, ClassParserInfoWrapper>> loop(Class clazz,Long parentId) {

        List<LeafWrapper<Long, Long, ClassParserInfoWrapper>> leafWrappers = new ArrayList<>();

        if(leafIndex == null){
            leafIndex = 0L;
        }
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
