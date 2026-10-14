package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 属性抽象基类
 * <p>
 * 属性在 class 文件里统一是「属性名索引 u2 + 属性长度 u4 + 属性体」三段式，
 * 长度字段是读取时的跳过依据，也是写出时的占位，因此写出时以 {@link #contentSize()} 为准重算。
 * 子类只需实现 {@link #read} 与 {@link #writeBody}。
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.7">JVM Specification - Attributes</a>
 */
public abstract class AbstractAttribute {

    private U2 attributeNameIndex;
    private U4 attributeLength;

    public AbstractAttribute(U2 attributeNameIndex, U4 attributeLength) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
    }

    /**
     * 从流中读取属性体（属性名索引与长度已由 AttributeFactory 读出）
     *
     * @param inputStream 输入流
     */
    public abstract void read(InputStream inputStream);

    /**
     * 写出属性体（不含属性名索引与长度字段）
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    protected abstract void writeBody(DataOutput out) throws IOException;

    /**
     * 属性体字节数；写出时该值会作为 attribute_length 重新计算
     *
     * @return int类型返回值
     */
    public abstract int contentSize();

    /**
     * 写出完整属性（含属性名索引与长度）
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        attributeNameIndex.write(out);
        out.writeInt(contentSize());
        writeBody(out);
    }

    public U2 getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public void setAttributeNameIndex(U2 attributeNameIndex) {
        this.attributeNameIndex = attributeNameIndex;
    }

    public U4 getAttributeLength() {
        return attributeLength;
    }

    public void setAttributeLength(U4 attributeLength) {
        this.attributeLength = attributeLength;
    }
}
