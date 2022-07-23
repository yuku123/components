package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U1;
import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.io.InputStream;

public class ConstantMethodHandleInfo extends AbstractConstantPool {
    private U1 referenceKind;
    private U2 referenceIndex;


    public ConstantMethodHandleInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.referenceKind = U1.read(inputStream);
        this.referenceIndex = U2.read(inputStream);
    }

    public U1 getReferenceKind() {
        return referenceKind;
    }

    public U2 getReferenceIndex() {
        return referenceIndex;
    }
}
