package com.zifang.util.core.lang.classinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * class的标准信息载体
 * */
public class ClassInfo{

    /**
     * 当前类名
     * */
    private String simpleClassName;

    /**
     * 当前包名
     * */
    private String packageName;

    /**
     * 父类
     * */
    private ClassInfo superClass;

    /**
     * 接口集合
     * */
    private List<ClassInfo> interfaces = new ArrayList<>(); // 默认为空

    /**
     * 类的域集合
     * */
    private List<FieldInfo> fields = new ArrayList<>(); // 默认为空

    /**
     * 类的所有方法
     * */
    private List<MethodInfo> methods = new ArrayList<>(); // 默认为空

    /**
     * 导入类
     * */
    private List<ClassInfo> imports = new ArrayList<>(); // 默认为空;

    /**
     * 标记当前类是否为接口 // 默认为简单class
     * */
    private Boolean isInterface = false;

    /**
     * 标记当前类是否为public // 默认为 public
     * */
    private Boolean isPublic = true;

    /**
     * 标记当前类的modifier
     * */
    private int modifiers;

    public boolean isInterface() {
        return isInterface;
    }

    public Boolean isPublic() {
        return isPublic;
    }


    public String getFullClassName(){
        return packageName+"."+simpleClassName;
    }

    public void appendMethods(List<MethodInfo> methodInfosFromInterface) {
        if(methods == null){
            methods = new ArrayList<>();
        }

        // 唯有不为空才尝试添加
        if(methodInfosFromInterface != null){
            methods.addAll(methodInfosFromInterface);
        }
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

    public List<ClassInfo> getImports() {
        return imports;
    }

    public void setImports(List<ClassInfo> imports) {
        this.imports = imports;
    }

    public Boolean getInterface() {
        return isInterface;
    }

    public void setInterface(Boolean anInterface) {
        isInterface = anInterface;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }
}
