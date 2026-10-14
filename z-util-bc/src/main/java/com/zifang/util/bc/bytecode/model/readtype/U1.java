package com.zifang.util.bc.bytecode.model.readtype;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 无符号1字节数据类型
 * <p>
 * 用于读写1字节的无符号整数，范围为0-255。
 */
public class U1 {

    public byte value;

    public U1(byte value) {
        this.value = value;
    }

    public static U1 read(InputStream stream) {
        return new ByteReader(stream).readU1();
    }

    /**
     * 大端写出，不带长度前缀
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        out.writeByte(value);
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
