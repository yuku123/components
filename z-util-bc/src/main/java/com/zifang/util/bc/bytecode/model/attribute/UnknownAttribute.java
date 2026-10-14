package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.Utf8Info;
import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 未知属性兜底（4.7）
 * <p>
 * JVMS 规定 JVM 必须忽略自己不认识且没有通过反射用到的属性，只按 attribute_length 跳过。
 * 直接丢弃会让后续属性整体错位，因此这里按原始字节完整保留，写出时原样回写。
 */
public class UnknownAttribute extends AbstractAttribute {

    private final String attributeName;
    private final byte[] rawBody;

    public UnknownAttribute(U2 attributeNameIndex, U4 attributeLength, String attributeName, byte[] rawBody) {
        super(attributeNameIndex, attributeLength);
        this.attributeName = attributeName;
        this.rawBody = rawBody;
    }

    /**
     * 按原始字节构造，供写出层复用未知属性
     *
     * @param name  属性名
     * @param bytes 属性体
     */
    public UnknownAttribute(String name, byte[] bytes) {
        super(new U2(0), new U4(0));
        this.attributeName = name;
        this.rawBody = bytes;
    }

    /**
     * 用常量池解析出属性名；名字无法解析时返回 null
     *
     * @param attributeNameIndex 属性名索引
     * @param poolList           常量池
     * @return String类型返回值
     */
    public static String resolveName(U2 attributeNameIndex, List<AbstractConstantPool> poolList) {
        if (attributeNameIndex == null || poolList == null) {
            return null;
        }
        int i = attributeNameIndex.intValue() - 1;
        if (i < 0 || i >= poolList.size() || !(poolList.get(i) instanceof Utf8Info)) {
            return null;
        }
        return ((Utf8Info) poolList.get(i)).getValue();
    }

    @Override
    public void read(InputStream inputStream) {
        // 已由 AttributeFactory 在构造前读净，此处无内容
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.write(rawBody);
    }

    @Override
    public int contentSize() {
        return rawBody.length;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public byte[] getRawBody() {
        return rawBody;
    }

    static byte[] readBody(InputStream in, int length) {
        return new ByteReader(in).readFully(length);
    }
}
