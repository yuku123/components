package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

/**
 * RuntimeVisibleAnnotations 属性（4.7.16）：运行时反射可见的注解容器。
 */
public class RuntimeVisibleAnnotationsAttribute extends AnnotationsAttribute {

    public RuntimeVisibleAnnotationsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }
}
