package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 字符串常量
 * <p>
 * CONSTANT_String_info用于表示字符串字面量的符号引用。
 * string_index指向常量池中CONSTANT_Utf8_info常量的索引。
 */
public class ConstantStringInfo extends AbstractConstantPool {

    private U2 stringIndex;

    public ConstantStringInfo(byte tag) {
        super(tag);
    }

    public ConstantStringInfo(U2 stringIndex) {
        super(CONSTANT_STRING_INFO);
        this.stringIndex = stringIndex;
    }

    @Override
    public void read(InputStream inputStream) {
        this.stringIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        stringIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 3;
    }

    public U2 getStringIndex() {
        return stringIndex;
    }

    public void setStringIndex(U2 stringIndex) {
        this.stringIndex = stringIndex;
    }

    @Override
    public String toString() {
        return "ConstantStringInfo{stringIndex=" + (stringIndex == null ? 0 : stringIndex.intValue()) + "}";
    }
}
