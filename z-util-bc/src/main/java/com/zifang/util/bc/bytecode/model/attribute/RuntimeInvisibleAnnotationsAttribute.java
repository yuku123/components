package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

/**
 * RuntimeInvisibleAnnotations 属性（4.7.16）：运行时反射不可见的注解容器，
 * 结构与 {@link RuntimeVisibleAnnotationsAttribute} 一致但语义不同。
 */
public class RuntimeInvisibleAnnotationsAttribute extends AnnotationsAttribute {

    public RuntimeInvisibleAnnotationsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }
}
