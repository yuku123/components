package com.zifang.util.core.lang.reflect;

import com.zifang.util.core.pattern.composite.leaf.LeafHelper;
import com.zifang.util.core.pattern.composite.leaf.LeafWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类解析器 提供更加方便的信息抓取工具
 *
 * @author zifang
 */
public class ClassParser {

    /**
     * 当前针对的解析的类
     * */
    private final Class<?> clazz;

    /**
     * 解析过程使用的参变量
     * */
    private Long leafIndex;

    /**
     * 解析class类
     * */
    private final LeafWrapper<Long, Long, ClassParserInfoWrapper> leafWrapper;

    /**
     * @param clazz 需要解析的类
     */
    public ClassParser(Class<?> clazz) {

        this.clazz = clazz;

        leafWrapper = doParser();
    }

    /**
     * 是否是普通的类
     * */
    public boolean isNormalClass() {
        return null != clazz //
                && !clazz.isInterface() //
                && !Modifier.isAbstract(clazz.getModifiers()) //
                && !clazz.isEnum() //
                && !clazz.isArray() //
                && !clazz.isAnnotation() //
                && !clazz.isSynthetic() //
                && !clazz.isPrimitive();//
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

        for(Class<?> interfaceItem : clazz.getInterfaces()){
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
