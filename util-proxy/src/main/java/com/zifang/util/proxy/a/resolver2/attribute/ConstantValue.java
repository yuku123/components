package com.zifang.util.proxy.a.resolver2.attribute;

import com.zifang.util.proxy.a.resolver2.ClassFile;
import com.zifang.util.proxy.a.resolver2.constantpool.AbstractConstantPool;
import com.zifang.util.proxy.a.resolver2.constantpool.ConstantPoolInfo;
import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;
import java.util.List;

/**
 * 常量描述,加了final的字段 在class解析后,会有这个属性
 */
public class ConstantValue extends AbstractAttribute {
    private U2 constantValueIndex;

    public ConstantValue(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }


    public U2 getConstantValueIndex() {
        return constantValueIndex;
    }

    public void setConstantValueIndex(U2 constantValueIndex) {
        this.constantValueIndex = constantValueIndex;
    }

    public void read(InputStream inputStream) {
        constantValueIndex = U2.read(inputStream);
    }


    @Override
    public String toString() {
        short nameIndex = getAttributeNameIndex().getValue();
        short valueIndex = constantValueIndex.getValue();
        ConstantPoolInfo poolInfo = ClassFile.poolInfo;
        List<AbstractConstantPool> poolList = poolInfo.getPoolList();
        String name = poolList.get(nameIndex - 1).toString();
        String value = poolList.get(valueIndex - 1).toString();
        return String.format("ConstantValue->attributeName:%s,attributeLength:%s,constantValue:%s", name, getAttributeLength().getValue(), value);
    }
}
