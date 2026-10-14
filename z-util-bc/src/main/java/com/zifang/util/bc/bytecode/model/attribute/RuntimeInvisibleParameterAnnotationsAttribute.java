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
 * RuntimeInvisibleParameterAnnotations 属性（4.7.18）：CLASS 可见的形参注解。
 */
public class RuntimeInvisibleParameterAnnotationsAttribute extends AbstractAttribute {

    /** 每个形参对应一组注解，形参数可能多于源码可见参数个数 */
    private final List<List<Annotation>> parameterAnnotations = new ArrayList<>();

    public RuntimeInvisibleParameterAnnotationsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        int numParameters = reader.readU1().getValue() & 0xFF;
        for (int i = 0; i < numParameters; i++) {
            int count = reader.readU2().intValue();
            List<Annotation> list = new ArrayList<>(count);
            for (int j = 0; j < count; j++) {
                list.add(Annotation.read(reader));
            }
            parameterAnnotations.add(list);
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeByte(parameterAnnotations.size());
        for (List<Annotation> list : parameterAnnotations) {
            out.writeShort(list.size());
            for (Annotation a : list) {
                a.write(out);
            }
        }
    }

    @Override
    public int contentSize() {
        int n = 1;
        for (List<Annotation> list : parameterAnnotations) {
            n += 2;
            for (Annotation a : list) {
                n += a.contentSize();
            }
        }
        return n;
    }

    public List<List<Annotation>> getParameterAnnotations() {
        return Collections.unmodifiableList(parameterAnnotations);
    }
}
