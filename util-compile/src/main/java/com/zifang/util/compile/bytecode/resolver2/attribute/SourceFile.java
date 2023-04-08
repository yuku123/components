package com.zifang.util.compile.bytecode.resolver2.attribute;

import com.zifang.util.compile.bytecode.resolver2.readtype.U2;
import com.zifang.util.compile.bytecode.resolver2.readtype.U4;

import java.io.InputStream;

public class SourceFile extends AbstractAttribute {
    private U2 sourceFileIndex;//java源文件名称


    public SourceFile(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }

    public U2 getSourceFileIndex() {
        return sourceFileIndex;
    }

    public void setSourceFileIndex(U2 sourceFileIndex) {
        this.sourceFileIndex = sourceFileIndex;
    }
}
