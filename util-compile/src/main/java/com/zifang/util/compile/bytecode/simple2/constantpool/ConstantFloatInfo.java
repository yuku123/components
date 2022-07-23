package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U4;

import java.io.InputStream;

public class ConstantFloatInfo extends AbstractConstantPool {
    private U4 bytes;


    public ConstantFloatInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.bytes = U4.read(inputStream);
    }

    public U4 getBytes() {
        return bytes;
    }

    public void setBytes(U4 bytes) {
        this.bytes = bytes;
    }
}
