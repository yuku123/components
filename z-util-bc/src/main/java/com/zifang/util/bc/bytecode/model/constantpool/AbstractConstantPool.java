package com.zifang.util.bc.bytecode.model.constantpool;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 常量池抽象基类
 * <p>
 * JVM常量池中所有常量项的父类，定义了常量池项的基本结构。
 * 每个常量池项都有一个tag字段标识其类型。
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.4">JVM Specification - Constant Pool</a>
 */
public abstract class AbstractConstantPool {

    public static final byte CONSTANT_UTF8_INFO = 1;
    public static final byte CONSTANT_INTEGER_INFO = 3;
    public static final byte CONSTANT_FLOAT_INFO = 4;
    public static final byte CONSTANT_LONG_INFO = 5;
    public static final byte CONSTANT_DOUBLE_INFO = 6;
    public static final byte CONSTANT_CLASS_INFO = 7;
    public static final byte CONSTANT_STRING_INFO = 8;
    public static final byte CONSTANT_FIELDREF_INFO = 9;
    public static final byte CONSTANT_METHODREF_INFO = 10;
    public static final byte CONSTANT_INTERFACEMETHODREF_INFO = 11;
    public static final byte CONSTANT_NAMEANDTYPE_INFO = 12;
    public static final byte CONSTANT_METHODHANDLE_INFO = 15;
    public static final byte CONSTANT_METHODTYPE_INFO = 16;
    public static final byte CONSTANT_DYNAMIC_INFO = 17;
    public static final byte CONSTANT_INVOKEDYNAMIC_INFO = 18;
    public static final byte CONSTANT_MODULE_INFO = 19;
    public static final byte CONSTANT_PACKAGE_INFO = 20;

    private byte tag;

    public AbstractConstantPool(byte tag) {
        this.tag = tag;
    }

    public abstract void read(InputStream inputStream);

    /**
     * 按 class 文件格式写出本常量项（含 tag 字节，不含双槽占位）
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public abstract void write(DataOutput out) throws IOException;

    /**
     * 本常量项占用的字节数（long/double 为 10）
     *
     * @return int类型返回值
     */
    public abstract int contentSize();

    public byte getTag() {
        return tag;
    }

    public void setTag(byte tag) {
        this.tag = tag;
    }
}
