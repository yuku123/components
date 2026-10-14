package com.zifang.util.bc.bytecode.model.annotation;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U1;
import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解元素值 element_value（4.7.16.1）
 * <p>
 * 首字节是 tag，决定后续负载的形状：
 * <pre>
 * B C D F I J S Z s  -> 对应常量池索引 u2
 * e                  -> enum_const_value: type_name_index u2 + const_name_index u2
 * c                  -> class_info_index u2
 * @                  -> annotation
 * [                  -> array_value: num_values u2 + 若干 element_value
 * </pre>
 */
public class ElementValue {

    private final U1 tag;
    private U2 constValueIndex;
    private U2 enumTypeNameIndex;
    private U2 enumConstNameIndex;
    private Annotation annotation;
    private List<ElementValue> arrayValues;

    public ElementValue(U1 tag) {
        this.tag = tag;
    }

    public static ElementValue read(ByteReader reader) {
        U1 tag = reader.readU1();
        ElementValue v = new ElementValue(tag);
        int t = tag.getValue() & 0xFF;
        switch (t) {
            case 'B': case 'C': case 'D': case 'F': case 'I':
            case 'J': case 'S': case 'Z': case 's':
                v.constValueIndex = reader.readU2();
                break;
            case 'e':
                v.enumTypeNameIndex = reader.readU2();
                v.enumConstNameIndex = reader.readU2();
                break;
            case 'c':
                v.constValueIndex = reader.readU2();
                break;
            case '@':
                v.annotation = Annotation.read(reader);
                break;
            case '[':
                U2 n = reader.readU2();
                List<ElementValue> list = new ArrayList<>(n.intValue());
                for (int i = 0; i < n.intValue(); i++) {
                    list.add(read(reader));
                }
                v.arrayValues = list;
                break;
            default:
                throw new IllegalStateException("非法的 element_value tag: 0x" + Integer.toHexString(t));
        }
        return v;
    }

    public void write(DataOutput out) throws IOException {
        tag.write(out);
        int t = tag.getValue() & 0xFF;
        switch (t) {
            case 'B': case 'C': case 'D': case 'F': case 'I':
            case 'J': case 'S': case 'Z': case 's': case 'c':
                constValueIndex.write(out);
                break;
            case 'e':
                enumTypeNameIndex.write(out);
                enumConstNameIndex.write(out);
                break;
            case '@':
                annotation.write(out);
                break;
            case '[':
                out.writeShort(arrayValues.size());
                for (ElementValue child : arrayValues) {
                    child.write(out);
                }
                break;
            default:
                throw new IllegalStateException("非法的 element_value tag: 0x" + Integer.toHexString(t));
        }
    }

    public U1 getTag() {
        return tag;
    }

    public U2 getConstValueIndex() {
        return constValueIndex;
    }

    public U2 getEnumTypeNameIndex() {
        return enumTypeNameIndex;
    }

    public U2 getEnumConstNameIndex() {
        return enumConstNameIndex;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public List<ElementValue> getArrayValues() {
        return arrayValues;
    }

    /**
     * 内容字节数（不含 tag）
     *
     * @return int类型返回值
     */
    public int contentSize() {
        int t = tag.getValue() & 0xFF;
        switch (t) {
            case 'B': case 'C': case 'D': case 'F': case 'I':
            case 'J': case 'S': case 'Z': case 's': case 'c':
                return 2;
            case 'e':
                return 4;
            case '@':
                return annotation.contentSize();
            case '[':
                int n = 2;
                for (ElementValue child : arrayValues) {
                    n += 1 + child.contentSize();
                }
                return n;
            default:
                return 0;
        }
    }
}
