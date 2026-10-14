package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 名称和类型描述符
 * <p>
 * CONSTANT_NameAndType_info用于表示字段或方法的名称和类型的符号引用。
 * 包含name_index和descriptor_index两个指向UTF-8常量的索引。
 */
public class ConstantNameAndTypeInfo extends AbstractConstantPool {

    private U2 nameIndex;
    private U2 descriptorIndex;

    public ConstantNameAndTypeInfo(byte tag) {
        super(tag);
    }

    public ConstantNameAndTypeInfo(U2 nameIndex, U2 descriptorIndex) {
        super(CONSTANT_NAMEANDTYPE_INFO);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    @Override
    public void read(InputStream inputStream) {
        this.nameIndex = U2.read(inputStream);
        this.descriptorIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        nameIndex.write(out);
        descriptorIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 5;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(U2 descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    @Override
    public String toString() {
        return "ConstantNameAndTypeInfo{nameIndex=" + (nameIndex == null ? 0 : nameIndex.intValue())
                + ", descriptorIndex=" + (descriptorIndex == null ? 0 : descriptorIndex.intValue()) + "}";
    }
}
