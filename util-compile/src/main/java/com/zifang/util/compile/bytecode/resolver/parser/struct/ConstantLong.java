package com.zifang.util.compile.bytecode.resolver.parser.struct;

public class ConstantLong extends ConstantPoolItem {



    public ConstantLong(ClassFile classFile, int index, long value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Long\t\t\t\t");
        result.append(value).append("l");
        return result.toString();
    }
}
