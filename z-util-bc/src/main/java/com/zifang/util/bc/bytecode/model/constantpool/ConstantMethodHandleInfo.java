package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U1;
import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 方法句柄
 * <p>
 * CONSTANT_MethodHandle_info（Java 7+）用于方法引用与方法调用点的绑定。
 */
public class ConstantMethodHandleInfo extends AbstractConstantPool {

    private U1 referenceKind;
    private U2 referenceIndex;

    public ConstantMethodHandleInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.referenceKind = U1.read(inputStream);
        this.referenceIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        referenceKind.write(out);
        referenceIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 4;
    }

    public U1 getReferenceKind() {
        return referenceKind;
    }

    public void setReferenceKind(U1 referenceKind) {
        this.referenceKind = referenceKind;
    }

    public U2 getReferenceIndex() {
        return referenceIndex;
    }

    public void setReferenceIndex(U2 referenceIndex) {
        this.referenceIndex = referenceIndex;
    }
}
