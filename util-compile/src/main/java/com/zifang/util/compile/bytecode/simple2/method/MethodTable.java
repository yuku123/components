package com.zifang.util.compile.bytecode.simple2.method;

import com.zifang.util.compile.bytecode.simple2.attribute.AbstractAttribute;
import com.zifang.util.compile.bytecode.simple2.attribute.AttributeFactory;
import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MethodTable {
    private U2 accessFlags;
    private U2 nameIndex;
    private U2 descriptorIndex;
    private U2 attributesCount;
    private List<AbstractAttribute> attributesInfo=new ArrayList<>();

    public MethodTable(InputStream stream) {
        accessFlags=U2.read(stream);
        nameIndex=U2.read(stream);
        descriptorIndex=U2.read(stream);
        attributesCount=U2.read(stream);
        short count = attributesCount.value;//field的属性
        for (int i = 0; i < count; i++) {
            parseFieldAttribute(stream);
        }
    }
    //解析字段表属性
    public void parseFieldAttribute(InputStream inputStream){
        AbstractAttribute attributeTable = AttributeFactory.getAttributeTable(inputStream);
        attributesInfo.add(attributeTable);
    }
    public U2 getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(U2 accessFlags) {
        this.accessFlags = accessFlags;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(U2 descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    public U2 getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(U2 attributesCount) {
        this.attributesCount = attributesCount;
    }

    public List<AbstractAttribute> getAttributesInfo() {
        return attributesInfo;
    }

    public void setAttributesInfo(List<AbstractAttribute> attributesInfo) {
        this.attributesInfo = attributesInfo;
    }
}
