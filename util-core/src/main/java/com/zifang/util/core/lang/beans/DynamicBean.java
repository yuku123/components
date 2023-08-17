package com.zifang.util.core.lang.beans;

import lombok.Data;

import java.util.List;

/**
 * @author zifang
 */
@Data
public class DynamicBean {

    /**
     * 继承的bean
     */
    private DynamicBean inheritedBean;

    /**
     * 动态bean字段
     */
    private List<DynamicField> fields;

    /**
     * 动态方法
     */
    private List<DynamicMethod> methods;
}
