package com.zifang.util.proxy.a.resolver.parser.struct;

public class ConstantClass extends ConstantPoolItem {

    private int utf8Index;

    public ConstantClass(ClassFile classFile, int index, int utf8Index) {
        super(classFile, index);
        this.utf8Index = utf8Index;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Class\t\t\t\t");
        result.append("#").append(index).append("\t\t// ");
        result.append(classFile.getString(utf8Index));
        return result.toString();
    }

    public int getUtf8Index() {
        return utf8Index;
    }

    @Override
    public String getValue() {
        return classFile.getString(utf8Index);
    }
}
