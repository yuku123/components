package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ConstantMethodTypeInfo extends AbstractConstantPool{

    private U2 descriptorIndex;


    public ConstantMethodTypeInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.descriptorIndex=U2.read(inputStream);
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }
}
