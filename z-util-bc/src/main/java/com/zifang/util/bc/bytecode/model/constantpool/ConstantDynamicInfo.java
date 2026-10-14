package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 动态常量
 * <p>
 * CONSTANT_Dynamic_info（Java 11+）用于 condy 指令，其值由 BootstrapMethods 静态求值。
 * 布局与 CONSTANT_InvokeDynamic_info 相同，但求值结果可以是任意类型而非仅方法调用点。
 */
public class ConstantDynamicInfo extends AbstractConstantPool {

    private U2 bootstrapMethodAttrIndex;
    private U2 nameAndTypeIndex;

    public ConstantDynamicInfo(byte tag) {
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
