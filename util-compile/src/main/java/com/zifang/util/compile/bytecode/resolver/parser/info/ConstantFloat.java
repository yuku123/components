package com.zifang.util.compile.bytecode.a.parser.info;

public class ConstantFloat extends ConstantPoolItem {


    public ConstantFloat(ClassFile classFile, int index, float value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Float\t\t\t\t");
        result.append(value + "f");
        return result.toString();
    }

}
