package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;

import java.util.List;

/**
 * 需要常量池二次解析的属性
 * <p>
 * 属性体里存的全是常量池索引，字符串化的结果要在读完之后由
 * {@link AttributeFactory} 用常量池回填。
 */
public interface PoolResolvable {

    /**
     * 用常量池把索引回填为可读文本
     *
     * @param poolList 常量池
     */
    void resolve(List<AbstractConstantPool> poolList);
}
