package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.annotation.Annotation;
import com.zifang.util.bc.bytecode.model.annotation.TypeAnnotation;
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
 * RuntimeInvisibleTypeAnnotations 属性（4.7.21）：CLASS 可见的类型注解。
 */
public class RuntimeInvisibleTypeAnnotationsAttribute extends AbstractAttribute {

    private final List<TypeAnnotation> typeAnnotations = new ArrayList<>();

    public RuntimeInvisibleTypeAnnotationsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        int count = reader.readU2().intValue();
        for (int i = 0; i < count; i++) {
            typeAnnotations.add(TypeAnnotation.read(reader));
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeShort(typeAnnotations.size());
        for (TypeAnnotation t : typeAnnotations) {
            t.write(out);
        }
    }

    @Override
    public int contentSize() {
        int n = 2;
        for (TypeAnnotation t : typeAnnotations) {
            n += t.contentSize();
        }
        return n;
    }

    public List<TypeAnnotation> getTypeAnnotations() {
        return Collections.unmodifiableList(typeAnnotations);
    }
}
