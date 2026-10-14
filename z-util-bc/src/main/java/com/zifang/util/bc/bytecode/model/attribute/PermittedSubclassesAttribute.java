package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PermittedSubclasses 属性（4.7.31，Java 17+）：sealed 类的直接子类列表。
 */
public class PermittedSubclassesAttribute extends AbstractAttribute {

    private final List<U2> subclassIndices = new ArrayList<>();

    public PermittedSubclassesAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        int count = reader.readU2().intValue();
        for (int i = 0; i < count; i++) {
            subclassIndices.add(reader.readU2());
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeShort(subclassIndices.size());
        for (U2 idx : subclassIndices) {
            idx.write(out);
        }
    }

    @Override
    public int contentSize() {
        return 2 + subclassIndices.size() * 2;
    }

    public List<U2> getSubclassIndices() {
        return Collections.unmodifiableList(subclassIndices);
    }
}
