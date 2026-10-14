package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 字段引用信息
 * <p>
 * CONSTANT_Fieldref_info用于表示类中声明的字段的符号引用。
 */
public class FieldRefInfo extends AbstractConstantPool {

    private U2 classIndex;
    private U2 descriptorIndex;

    public FieldRefInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.classIndex = U2.read(inputStream);
        this.descriptorIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        classIndex.write(out);
        descriptorIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 5;
    }

    public U2 getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(U2 classIndex) {
        this.classIndex = classIndex;
    }

    /**
     * 指向 CONSTANT_NameAndType_info 的索引
     * <p>
     * 字段名与方法名都在 NameAndType 里，本字段不是「名字」而是「名与描述符」的引用。
     *
     * @return U2类型返回值
     */
    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(U2 descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    @Override
    public String toString() {
        return "FieldRefInfo{classIndex=" + (classIndex == null ? 0 : classIndex.intValue())
                + ", descriptorIndex=" + (descriptorIndex == null ? 0 : descriptorIndex.intValue()) + "}";
    }
}
