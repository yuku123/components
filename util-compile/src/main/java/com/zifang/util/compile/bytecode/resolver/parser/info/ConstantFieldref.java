package com.zifang.util.compile.bytecode.a.parser.info;

public class ConstantFieldref extends ConstantPoolItem {

    private int classIndex;

    private int nameAndTypeIndex;

    public ConstantFieldref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        super(classFile, index);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Fieldref\t\t\t#");
        ConstantClass constClass = (ConstantClass) classFile.get(classIndex);
        ConstantNameAndType constNameAndType = (ConstantNameAndType) classFile.get(nameAndTypeIndex);

        result.append(classIndex + ".#" + nameAndTypeIndex).append("\t\t// ");
        result.append(classFile.getString(constClass.getUtf8Index()) + "."
                + classFile.getString(constNameAndType.getNameIndex()) + ":"
                + classFile.getString(constNameAndType.getTypeIndex()));
        return result.toString();
    }

}