package com.zifang.util.core.pattern.dynamic;

import lombok.Data;

import java.util.List;

/**
 * @author zifang
 */
@Data
public class DynamicClass {

    /**
     * 实现的接口类
     */
    private List<DynamicClass> implementClasses;

    /**
     * 继承的bean
     */
    private DynamicClass extendClass;

    /**
     * 动态bean字段
     */
    private List<DynamicField> fields;

    /**
     * 动态方法
     */
    private List<DynamicMethod> methods;
}
