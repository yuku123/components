package com.zifang.util.proxy.a.resolver.parser.struct;

public class ConstantNameAndType extends ConstantPoolItem {

    private int nameIndex;

    private int typeIndex;

    public ConstantNameAndType(ClassFile classFile, int index, int nameIndex, int typeIndex) {
        super(classFile, index);
        this.nameIndex = nameIndex;
        this.typeIndex = typeIndex;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = NameAndType\t\t\t");
        result.append("#").append(nameIndex).append(":").append(typeIndex).append("\t\t// ");
        result.append(classFile.getString(nameIndex)).append(":").append(typeIndex);
        return result.toString();
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getTypeIndex() {
        return typeIndex;
    }
}