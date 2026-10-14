package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 动态调用点
 * <p>
 * CONSTANT_InvokeDynamic_info用于表示invokedynamic指令的调用点，
 * 实际方法实现由 BootstrapMethods 决定。
 */
public class ConstantInvokeDynamicInfo extends AbstractConstantPool {

    private U2 bootstrapMethodAttrIndex;
    private U2 nameAndTypeIndex;

    public ConstantInvokeDynamicInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.bootstrapMethodAttrIndex = U2.read(inputStream);
        this.nameAndTypeIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        bootstrapMethodAttrIndex.write(out);
        nameAndTypeIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 5;
    }

    public U2 getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public void setBootstrapMethodAttrIndex(U2 bootstrapMethodAttrIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
    }

    public U2 getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    public void setNameAndTypeIndex(U2 nameAndTypeIndex) {
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}
