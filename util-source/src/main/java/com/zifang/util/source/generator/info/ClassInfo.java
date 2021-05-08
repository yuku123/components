package com.zifang.util.source.generator.info;

import java.util.ArrayList;
import java.util.List;

/**
 * class的标准信息载体
 */
public class ClassInfo {

    /**
     * 标记当前classInfo是否为接口
     * <p>
     * true : 是接口
     * false : 不是接口
     */
    private Boolean interfaceType;

    /**
     * 当前类名
     */
    private String simpleClassName;

    /**
     * 当前包名
     */
    private String packageName;

    /**
     * 父类
     */
    private ClassInfo superClass;

    /**
     * 只是单纯记录类名
     */
    private List<String> imports = new ArrayList<>(); // 默认为空;

    /**
     * block注释
     */
    private List<String> comments = new ArrayList<>(); // 类注释

    /**
     * 接口集合
     */
    private List<ClassInfo> interfaces = new ArrayList<>(); // 默认为空

    /**
     * 类的域集合
     */
    private List<FieldInfo> fields = new ArrayList<>(); // 默认为空

    /**
     * 类的所有方法
     */
    private List<MethodInfo> methods = new ArrayList<>(); // 默认为空

    /**
     * 标记当前类的modifier 决定是否为public
     */
    private int modifiers;

    /**
     * 全类路径名字
     */
    public String getName() {
        return packageName + "." + simpleClassName;
    }

    public void appendFields(List<FieldInfo> fieldInfos) {
        checkField();
        fields.addAll(fieldInfos);
    }

    public void appendField(FieldInfo fieldInfo) {
        checkField();
        fields.add(fieldInfo);
    }

    public void appendMethods(List<MethodInfo> methodInfos) {
        checkMethod();
        methods.addAll(methodInfos);
    }

    public void appendMethod(MethodInfo methodInfo) {
        checkMethod();
        methods.add(methodInfo);
    }

    public void appendInterfaces(List<ClassInfo> interfaceClassInfos) {
        checkInterface();
        interfaces.addAll(interfaceClassInfos);
    }

    public void appendInterfaces(ClassInfo interfaceClassInfo) {
        checkInterface();
        interfaces.add(interfaceClassInfo);
    }

    /**
     * 将一个运行态class 直接解析转化为ClassInfo
     */
    public static ClassInfo parser(Class clazz) {
        return null; // @todo
    }

    /**
     * 最小闭环构造器
     */
    public static ClassInfo build(
            Boolean interfaceType,
            Integer modifiers,
            String packageName,
            List<String> comments,
            String simpleClassName,
            ClassInfo superClass,
            List<ClassInfo> interfaces,
            List<FieldInfo> fieldInfos,
            List<MethodInfo> methodInfos) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setInterfaceType(interfaceType);
        classInfo.setModifiers(modifiers);
        classInfo.setPackageName(packageName);
        classInfo.setComments(comments);
        classInfo.setSimpleClassName(simpleClassName);
        classInfo.setSuperClass(superClass);
        classInfo.setInterfaces(interfaces);
        classInfo.setFields(fieldInfos);
        classInfo.setMethods(methodInfos);
        return classInfo;
    }


    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ClassInfo getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassInfo superClass) {
        this.superClass = superClass;
    }

    public List<ClassInfo> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<ClassInfo> interfaces) {
        this.interfaces = interfaces;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public Boolean getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Boolean interfaceType) {
        this.interfaceType = interfaceType;
    }

    private void checkInterface() {
        if (interfaces == null) {
            throw new RuntimeException("当前interfaceList为null");
        }
    }

    private void checkMethod() {
        if (methods == null) {
            throw new RuntimeException("当前methodList为null");
        }
    }

    private void checkImports() {
        if (imports == null) {
            throw new RuntimeException("当前importList为null");
        }
    }

    private void checkField() {
        if (fields == null) {
            throw new RuntimeException("当前fieldList为null");
        }
    }
}
