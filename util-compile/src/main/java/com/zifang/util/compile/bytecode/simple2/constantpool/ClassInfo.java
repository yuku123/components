package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ClassInfo extends AbstractConstantPool{
    private U2 nameIndex;

    public ClassInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.nameIndex=U2.read(inputStream);
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "nameIndex=" + nameIndex.value +
                '}';
    }
}
