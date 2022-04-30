package com.zifang.util.compile.bytecode.resolver.parser.struct;

public class ConstantMethodref extends ConstantPoolItem {

    private int classIndex;

    private int nameAndTypeIndex;

    public ConstantMethodref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        super(classFile, index);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Methodref\t\t\t");
        result.append("#" + classIndex + ".#" + nameAndTypeIndex).append("\t\t// ");
        ConstantClass constClass = (ConstantClass) classFile.get(classIndex);
        result.append(constClass.getValue() + ".");

        ConstantNameAndType constNameAndType = (ConstantNameAndType) classFile.get(nameAndTypeIndex);
        ConstantUtf8 methodName = (ConstantUtf8) classFile.get(constNameAndType.getNameIndex());
        ConstantUtf8 methodReturnType = (ConstantUtf8) classFile.get(constNameAndType.getTypeIndex());

        result.append(methodName.getValue() + ":" + methodReturnType.getValue());

        return result.toString();
    }

}
