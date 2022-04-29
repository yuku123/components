package com.zifang.util.compile.bytecode.simple2.attribute;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;
import com.zifang.util.compile.bytecode.simple2.readtype.U4;

import java.io.InputStream;

public class Synthetic extends AbstractAttribute{

    public Synthetic(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }
}
