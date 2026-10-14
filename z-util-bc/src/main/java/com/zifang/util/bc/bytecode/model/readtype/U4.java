package com.zifang.util.bc.bytecode.model.readtype;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 无符号4字节数据类型
 * <p>
 * JVM 中用于表示 magic、属性长度与 float 位模式。
 */
public class U4 {

    public int value;
    public byte[] bytes;

    public U4(int value, byte[] bytes) {
        this.value = value;
        this.bytes = bytes;
    }

    public U4(int value) {
        this(value, null);
    }

    static U4 of(byte[] bytes) {
        int v = 0;
        for (int i = 0; i < 4; i++) {
            v = (v << 8) | (bytes[i] & 0xFF);
        }
        return new U4(v, bytes);
    }

    public static U4 read(InputStream stream) {
        return new ByteReader(stream).readU4();
    }

    /**
     * 大端写出，不带长度前缀
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        out.writeInt(value);
    }

    public int getValue() {
        return value;
    }

    /**
     * 按无符号语义返回 0-4294967295
     *
     * @return long类型返回值
     */
    public long longValue() {
        return value & 0xFFFFFFFFL;
    }

    public void setValue(int value) {
        this.value = value;
        this.bytes = new byte[]{
                (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
    }

    @Override
    public String toString() {
        return String.valueOf(longValue());
    }
}
