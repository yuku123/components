package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.Mutf8;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * UTF-8编码的字符串常量
 * <p>
 * CONSTANT_Utf8_info用于存储字符串、类名、方法名等文本数据。
 * JVM规范定义的MUTF-8与标准UTF-8不同，编解码走 {@link Mutf8}。
 */
public class Utf8Info extends AbstractConstantPool {

    private short length;
    private byte[] bytes;
    private String bytesValue;

    public Utf8Info(byte tag) {
        super(tag);
    }

    /**
     * 以给定字符串构造常量池项，供写出层 intern 使用
     *
     * @param value 字符串内容
     */
    public Utf8Info(String value) {
        super(CONSTANT_UTF8_INFO);
        setValue(value);
    }

    @Override
    public void read(InputStream in) {
        byte[] lenBytes = new ByteReader(in).readFully(2);
        this.length = (short) (((lenBytes[0] & 0xFF) << 8) | (lenBytes[1] & 0xFF));
        this.bytes = new ByteReader(in).readFully(length & 0xFFFF);
        this.bytesValue = Mutf8.decode(this.bytes, 0, this.bytes.length);
    }

    /**
     * 写出 tag + length + MUTF-8 字节
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    @Override
    public void write(DataOutput out) throws IOException {
        byte[] encoded = Mutf8.encode(getValue());
        out.writeByte(getTag());
        out.writeShort(encoded.length);
        out.write(encoded);
    }

    public String getValue() {
        return bytesValue;
    }

    /**
     * 覆盖内容并同步刷新字节缓存
     *
     * @param value 新内容
     */
    public void setValue(String value) {
        this.bytesValue = value;
        this.bytes = Mutf8.encode(value);
        this.length = (short) this.bytes.length;
    }

    @Override
    public String toString() {
        return "Utf8Info{bytesValue='" + bytesValue + "'}";
    }

    @Override
    public int contentSize() {
        return 3 + Mutf8.encode(getValue()).length;
    }
}
