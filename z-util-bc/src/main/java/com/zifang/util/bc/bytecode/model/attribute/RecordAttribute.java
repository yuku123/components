package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
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
 * Record 属性（4.7.30，Java 16+）：record 类的组件声明。
 * <p>
 * 每个组件带一组属性，注解就挂在这里（record 组件上的注解必须是 RUNTIME 可见性，
 * 且注解目标要含 RECORD_COMPONENT）。
 */
public class RecordAttribute extends AbstractAttribute {

    /** 单个 record 组件 */
    public static class RecordComponent {

        private final U2 nameIndex;
        private final U2 descriptorIndex;
        private final List<AbstractAttribute> attributes;

        public RecordComponent(U2 nameIndex, U2 descriptorIndex, List<AbstractAttribute> attributes) {
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
            this.attributes = attributes;
        }

        public U2 getNameIndex() {
            return nameIndex;
        }

        public U2 getDescriptorIndex() {
            return descriptorIndex;
        }

        public List<AbstractAttribute> getAttributes() {
            return Collections.unmodifiableList(attributes);
        }

        void writeBody(DataOutput out) throws IOException {
            nameIndex.write(out);
            descriptorIndex.write(out);
            out.writeShort(attributes.size());
            for (AbstractAttribute a : attributes) {
                a.write(out);
            }
        }

        int bodySize() {
            int n = 6;
            for (AbstractAttribute a : attributes) {
                n += 6 + a.contentSize();
            }
            return n;
        }
    }

    private final List<RecordComponent> components = new ArrayList<>();

    public RecordAttribute(U2 attributeNameIndex, U4 attributeLength, List<AbstractConstantPool> poolList) {
        super(attributeNameIndex, attributeLength);
        this.poolList = poolList;
    }

    private List<AbstractConstantPool> poolList;

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        int count = reader.readU2().intValue();
        for (int i = 0; i < count; i++) {
            U2 nameIndex = reader.readU2();
            U2 descriptorIndex = reader.readU2();
            List<AbstractAttribute> attrs = new ArrayList<>();
            int attrCount = reader.readU2().intValue();
            for (int j = 0; j < attrCount; j++) {
                AbstractAttribute a = AttributeFactory.getAttributeTable(inputStream, poolList);
                if (a != null) {
                    attrs.add(a);
                }
            }
            components.add(new RecordComponent(nameIndex, descriptorIndex, attrs));
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeShort(components.size());
        for (RecordComponent c : components) {
            c.writeBody(out);
        }
    }

    @Override
    public int contentSize() {
        int n = 2;
        for (RecordComponent c : components) {
            n += c.bodySize();
        }
        return n;
    }

    public List<RecordComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }
}
