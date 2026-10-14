package com.zifang.util.bc.bytecode.model.readtype;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 无符号8字节数据类型
 * <p>
 * JVM 中用于表示 long 与 double 的位模式存储。
 */
public class U8 {

    private long value;

    public U8(long value) {
        this.value = value;
    }

    static U8 of(byte[] bytes) {
        long v = 0;
        for (int i = 0; i < 8; i++) {
            v = (v << 8) | (bytes[i] & 0xFFL);
        }
        return new U8(v);
    }

    public static U8 read(InputStream stream) {
        return new ByteReader(stream).readU8();
    }

    /**
     * 大端写出，不带长度前缀
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        out.writeLong(value);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
