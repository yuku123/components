package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * ModuleMainClass 属性（4.7.24）：java -m 启动时使用的入口类。
 */
public class ModuleMainClassAttribute extends AbstractAttribute {

    private U2 mainClassIndex;

    public ModuleMainClassAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        mainClassIndex = U2.read(inputStream);
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        mainClassIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 2;
    }

    public U2 getMainClassIndex() {
        return mainClassIndex;
    }
}
