package com.zifang.util.core.lang.object.component;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author zifang
 * @Date 2020-06-05
 * @Description 标准化类信息承载体
 */
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
}
