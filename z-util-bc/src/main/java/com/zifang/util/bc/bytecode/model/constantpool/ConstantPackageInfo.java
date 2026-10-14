package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 包引用
 * <p>
 * CONSTANT_Package_info（Java 9+）指向模块内某个包的名字。
 */
public class ConstantPackageInfo extends AbstractConstantPool {

    private U2 index;

    public ConstantPackageInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.index = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        index.write(out);
    }

    @Override
    public int contentSize() {
        return 3;
    }

    public U2 getIndex() {
        return index;
    }

    public void setIndex(U2 index) {
        this.index = index;
    }
}
