package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.annotation.ElementValue;
import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * AnnotationDefault 属性（4.7.22）：注解类型元素声明时的默认值。
 */
public class AnnotationDefaultAttribute extends AbstractAttribute {

    private ElementValue defaultValue;

    public AnnotationDefaultAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        defaultValue = ElementValue.read(new ByteReader(inputStream));
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        defaultValue.write(out);
    }

    @Override
    public int contentSize() {
        return 1 + defaultValue.contentSize();
    }

    public ElementValue getDefaultValue() {
        return defaultValue;
    }
}
