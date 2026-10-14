package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U1;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MethodParameters 属性（4.7.24）：方法的形参名与形参级修饰符（如 ACC_MANDATED）。
 * <p>
 * 数组长度按方法的形参个数（含 synthetic 形参）分配，因此可能多于源码可见的参数个数。
 */
public class MethodParametersAttribute extends AbstractAttribute {

    /** 单个形参项 */
    public static class Parameter {

        private final U2 nameIndex;
        private final U2 accessFlags;

        public Parameter(U2 nameIndex, U2 accessFlags) {
            this.nameIndex = nameIndex;
            this.accessFlags = accessFlags;
        }

        public U2 getNameIndex() {
            return nameIndex;
        }

        public U2 getAccessFlags() {
            return accessFlags;
        }

        void writeBody(DataOutput out) throws IOException {
            nameIndex.write(out);
            accessFlags.write(out);
        }
    }

    private final List<Parameter> parameters = new ArrayList<>();

    public MethodParametersAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        int count = reader.readU1().getValue() & 0xFF;
        for (int i = 0; i < count; i++) {
            parameters.add(new Parameter(reader.readU2(), reader.readU2()));
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeByte(parameters.size());
        for (Parameter p : parameters) {
            p.writeBody(out);
        }
    }

    @Override
    public int contentSize() {
        return 1 + parameters.size() * 4;
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
