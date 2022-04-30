package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ConstantClassInfo extends AbstractConstantPool{

    private U2 stringIndex;

    public ConstantClassInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.stringIndex = U2.read(inputStream);
    }

    public U2 getStringIndex() {
        return stringIndex;
    }
}
