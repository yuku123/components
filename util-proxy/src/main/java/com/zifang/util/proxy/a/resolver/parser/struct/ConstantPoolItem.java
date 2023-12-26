package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * @author zifang
 */
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
