package com.zifang.util.proxy.a.resolver2.field;

import com.zifang.util.proxy.a.resolver2.attribute.AbstractAttribute;
import com.zifang.util.proxy.a.resolver2.attribute.AttributeFactory;
import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FieldTable {
    public U2 accessFlags;//作用域
    public U2 nameIndex;//名字索引
    public U2 descriptorIndex;//描述索引
    public U2 attributesCount;//属性数量
    public List<AbstractAttribute> attributesInfo = new ArrayList();//属性信息

    public FieldTable(InputStream stream) {
        accessFlags = U2.read(stream);
        nameIndex = U2.read(stream);
        descriptorIndex = U2.read(stream);
        attributesCount = U2.read(stream);
        short count = attributesCount.value;//field的属性
        for (int i = 0; i < count; i++) {
            parseFieldAttribute(stream);
        }
    }

    //解析字段表属性
    public void parseFieldAttribute(InputStream inputStream) {
        AbstractAttribute attributeTable = AttributeFactory.getAttributeTable(inputStream);
        attributesInfo.add(attributeTable);
    }

}
