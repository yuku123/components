package com.zifang.util.bc.bytecode.model.readtype;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 无符号2字节数据类型
 * <p>
 * JVM 中多使用此类型表示索引和计数。底层以 short 承载 0-65535，
 * 超过 0x7FFF 的值读出为负，取值请统一走 {@link #intValue()}。
 */
public class U2 {

    public short value;

    public U2(short value) {
        this.value = value;
    }

    public U2(int value) {
        this.value = (short) value;
    }

    static U2 of(byte[] bytes) {
        int v = ((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF);
        return new U2((short) v);
    }

    public static U2 read(InputStream stream) {
        return new ByteReader(stream).readU2();
    }

    /**
     * 大端写出，不带长度前缀
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        out.writeShort(value);
    }

    public short getValue() {
        return value;
    }

    /**
     * 按无符号语义返回 0-65535
     *
     * @return int类型返回值
     */
    public int intValue() {
        return value & 0xFFFF;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = (short) value;
    }

    @Override
    public String toString() {
        return String.valueOf(intValue());
    }
}
