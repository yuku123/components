package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class MethodRefInfo extends AbstractConstantPool{
    private U2 classIndex;
    private U2 nameIndex;

    public MethodRefInfo(byte tag) {
        super(tag);
    }


    public void read(InputStream inputStream) {
        this.classIndex=U2.read(inputStream);
        this.nameIndex=U2.read(inputStream);
    }

    public U2 getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(U2 classIndex) {
        this.classIndex = classIndex;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
    }


    @Override
    public String toString() {
        return "MethodRefInfo{" +
                "classIndex=" + classIndex.value +
                ", nameIndex=" + nameIndex.value +
                '}';
    }
}
