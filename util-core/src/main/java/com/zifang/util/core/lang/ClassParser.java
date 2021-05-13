package com.zifang.util.core.lang;

import com.zifang.util.core.pattern.composite.node.INode;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类解析器 提供更加方便的信息抓取工具
 */
public class ClassParser {

    // 解析器缓存
    private static Map<Class<?>, ClassParser> classParserCache = new LinkedHashMap<>();

    // 类继承关系
    private static Map<Class<?>, INode> classInheraNode = new LinkedHashMap<>();

    // 当前针对的解析的类
    private Class<?> clazz;

    public ClassParser(Class<?> clazz, boolean forceRefreshCache) {
        this.clazz = clazz;
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


}
