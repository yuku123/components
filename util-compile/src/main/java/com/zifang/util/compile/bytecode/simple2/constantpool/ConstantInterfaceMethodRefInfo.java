package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ConstantInterfaceMethodRefInfo extends AbstractConstantPool{
    private U2 classIndex;
    private U2 nameIndex;


    public ConstantInterfaceMethodRefInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.classIndex=U2.read(inputStream);
        this.nameIndex=U2.read(inputStream);
    }


    public U2 getClassIndex() {
        return classIndex;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }
}
