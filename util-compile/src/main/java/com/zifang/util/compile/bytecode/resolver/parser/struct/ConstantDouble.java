package com.zifang.util.compile.bytecode.resolver.parser.struct;

public class ConstantDouble extends ConstantPoolItem {

    public ConstantDouble(ClassFile classFile, int index, double value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Double\t\t\t\t");
        result.append(value).append("d");
        return result.toString();
    }

}