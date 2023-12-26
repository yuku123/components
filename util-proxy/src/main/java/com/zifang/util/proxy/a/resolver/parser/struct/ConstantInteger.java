package com.zifang.util.proxy.a.resolver.parser.struct;

public class ConstantInteger extends ConstantPoolItem {


    public ConstantInteger(ClassFile classFile, int index, int value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Integer\t\t\t");
        result.append(value);
        return result.toString();
    }
}
