package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * Synthetic 属性（4.7.13）：类/字段/方法由编译器自动生成，源码中不存在对应声明。
 */
public class SyntheticAttribute extends AbstractAttribute {

    public SyntheticAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        // body 为空
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        // body 为空
    }

    @Override
    public int contentSize() {
        return 0;
    }
}
