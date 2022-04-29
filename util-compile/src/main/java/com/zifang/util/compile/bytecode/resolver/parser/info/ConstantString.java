package com.zifang.util.compile.bytecode.a.parser.info;

public class ConstantString extends ConstantPoolItem {
    private int utf8Index;

    public ConstantString(ClassFile classFile, int index, int utf8Index) {
        super(classFile, index);
        this.utf8Index = utf8Index;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = String\t\t\t\t");
        result.append("#").append(utf8Index).append("\t\t// ");
        result.append(classFile.getString(utf8Index));
        return result.toString();
    }
}
