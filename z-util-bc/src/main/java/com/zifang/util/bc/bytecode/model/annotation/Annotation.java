package com.zifang.util.bc.bytecode.model.annotation;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解 annotation（4.7.16）
 * <p>
 * type_index 指向注解接口的 CONSTANT_Utf8_info（描述符形如 Lfoo/Bar;）。
 */
public class Annotation {

    /** 注解类型描述符索引 */
    private final U2 typeIndex;
    private final List<ElementNameValue> pairs = new ArrayList<>();

    public Annotation(U2 typeIndex) {
        this.typeIndex = typeIndex;
    }

    public static Annotation read(ByteReader reader) {
        Annotation a = new Annotation(reader.readU2());
        U2 numPairs = reader.readU2();
        for (int i = 0; i < numPairs.intValue(); i++) {
            a.pairs.add(new ElementNameValue(reader.readU2(), ElementValue.read(reader)));
        }
        return a;
    }

    public void write(DataOutput out) throws IOException {
        typeIndex.write(out);
        out.writeShort(pairs.size());
        for (ElementNameValue pair : pairs) {
            pair.write(out);
        }
    }

    public U2 getTypeIndex() {
        return typeIndex;
    }

    public List<ElementNameValue> getPairs() {
        return pairs;
    }

    public int contentSize() {
        // type_index(u2) + num_element_value_pairs(u2) + 每组「元素名 u2 + 元素值」
        int n = 4;
        for (ElementNameValue pair : pairs) {
            n += 2 + 1 + pair.getValue().contentSize();
        }
        return n;
    }

    /**
     * 注解的一组「元素名 = 元素值」
     */
    public static class ElementNameValue {

        private final U2 elementNameIndex;
        private final ElementValue value;

        public ElementNameValue(U2 elementNameIndex, ElementValue value) {
            this.elementNameIndex = elementNameIndex;
            this.value = value;
        }

        public void write(DataOutput out) throws IOException {
            elementNameIndex.write(out);
            value.write(out);
        }

        public U2 getElementNameIndex() {
            return elementNameIndex;
        }

        public ElementValue getValue() {
            return value;
        }
    }
}
