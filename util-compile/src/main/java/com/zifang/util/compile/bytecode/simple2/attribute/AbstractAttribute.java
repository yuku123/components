package com.zifang.util.compile.bytecode.simple2.attribute;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;
import com.zifang.util.compile.bytecode.simple2.readtype.U4;

import java.io.InputStream;

public abstract class AbstractAttribute {

    private U2 attributeNameIndex;
    private U4 attributeLength;//明属性值所占用的字节数

    public AbstractAttribute(U2 attributeNameIndex, U4 attributeLength) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
    }

    public abstract void read(InputStream inputStream);

    public U2 getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public void setAttributeNameIndex(U2 attributeNameIndex) {
        this.attributeNameIndex = attributeNameIndex;
    }

    public U4 getAttributeLength() {
        return attributeLength;
    }

    public void setAttributeLength(U4 attributeLength) {
        this.attributeLength = attributeLength;
    }
}
