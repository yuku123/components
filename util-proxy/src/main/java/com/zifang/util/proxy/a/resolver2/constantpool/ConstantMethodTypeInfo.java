package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;

public class ConstantMethodTypeInfo extends AbstractConstantPool {

    private U2 descriptorIndex;


    public ConstantMethodTypeInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.descriptorIndex = U2.read(inputStream);
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }
}
