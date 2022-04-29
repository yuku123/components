package com.zifang.util.compile.bytecode.simple2.attribute;

import cn.hutool.core.util.StrUtil;
import com.zifang.util.compile.bytecode.simple2.ClassFile;
import com.zifang.util.compile.bytecode.simple2.constantpool.AbstractConstantPool;
import com.zifang.util.compile.bytecode.simple2.constantpool.ConstantPoolInfo;
import com.zifang.util.compile.bytecode.simple2.readtype.U2;
import com.zifang.util.compile.bytecode.simple2.readtype.U4;

import java.io.InputStream;
import java.util.List;

/**
 * 常量描述,加了final的字段 在class解析后,会有这个属性
 */
public class ConstantValue extends AbstractAttribute{
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
        constantValueIndex=U2.read(inputStream);
    }



    @Override
    public String toString() {
        short nameIndex = getAttributeNameIndex().getValue();
        short valueIndex = constantValueIndex.getValue();
        ConstantPoolInfo poolInfo = ClassFile.poolInfo;
        List<AbstractConstantPool> poolList = poolInfo.getPoolList();
        String name=poolList.get(nameIndex-1).toString();
        String value=poolList.get(valueIndex-1).toString();
        return StrUtil.format("ConstantValue->attributeName:{},attributeLength:{},constantValue:{}",name,getAttributeLength().getValue(),value);
    }
}
