package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ConstantInvokeDynamicInfo extends AbstractConstantPool{

    private U2 bootstrapMethodAttrIndex;
    private U2 nameIndex;

    public ConstantInvokeDynamicInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.bootstrapMethodAttrIndex=U2.read(inputStream);
        this.nameIndex = U2.read(inputStream);
    }

    public U2 getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }
}
