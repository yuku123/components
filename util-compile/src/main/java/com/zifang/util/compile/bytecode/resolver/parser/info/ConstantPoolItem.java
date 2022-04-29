package com.zifang.util.compile.bytecode.a.parser.info;

public class ConstantPoolItem {

    protected ClassFile classFile;

    protected int index;

    protected String value;

    public ConstantPoolItem(ClassFile classFile, int index) {
        super();
        this.classFile = classFile;
        this.index = index;
    }

    public String getValue() {
        return value;
    }
}
