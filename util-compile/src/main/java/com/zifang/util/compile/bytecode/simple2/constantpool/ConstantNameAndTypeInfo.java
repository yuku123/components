package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ConstantNameAndTypeInfo extends AbstractConstantPool{
    private U2 nameIndex;
    private U2 descriptorIndex;


    public ConstantNameAndTypeInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.nameIndex=U2.read(inputStream);
        this.descriptorIndex=U2.read(inputStream);
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    @Override
    public String toString() {
        return "ConstantNameAndTypeInfo{" +
                "nameIndex=" + nameIndex.value +
                ", descriptorIndex=" + descriptorIndex.value +
                '}';
    }
}
