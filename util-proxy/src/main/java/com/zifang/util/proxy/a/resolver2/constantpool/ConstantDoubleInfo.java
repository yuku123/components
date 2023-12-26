package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U8;

import java.io.InputStream;

public class ConstantDoubleInfo extends AbstractConstantPool {
    private U8 bytes;

    public ConstantDoubleInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.bytes = U8.read(inputStream);
    }

    public U8 getBytes() {
        return bytes;
    }
}
