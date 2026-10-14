package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * SourceDebugExtension 属性（4.7.11）：javac -g 之外的额外调试信息，语义由工具自定义。
 */
public class SourceDebugExtensionAttribute extends AbstractAttribute {

    private byte[] debugExtension;

    public SourceDebugExtensionAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        debugExtension = new ByteReader(inputStream).readFully((int) getAttributeLength().longValue());
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.write(debugExtension);
    }

    @Override
    public int contentSize() {
        return debugExtension.length;
    }

    public byte[] getDebugExtension() {
        return debugExtension;
    }
}
