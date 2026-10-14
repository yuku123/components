package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 方法类型
 * <p>
 * CONSTANT_MethodType_info（Java 7+）描述方法句柄的类型签名。
 */
public class ConstantMethodTypeInfo extends AbstractConstantPool {

    private U2 descriptorIndex;

    public ConstantMethodTypeInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.descriptorIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        descriptorIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 3;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(U2 descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }
}
