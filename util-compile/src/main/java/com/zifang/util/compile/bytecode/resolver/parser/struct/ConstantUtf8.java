package com.zifang.util.compile.bytecode.resolver.parser.struct;

public class ConstantUtf8 extends ConstantPoolItem {

    public ConstantUtf8(ClassFile classFile, int index, String value) {
        super(classFile, index);
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Utf8\t\t\t\t");
        result.append(value);
        return result.toString();
    }

}