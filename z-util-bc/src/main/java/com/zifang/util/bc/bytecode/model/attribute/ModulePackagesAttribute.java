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
 * ModulePackages 属性（4.7.25）：模块中被 exports/opens 的全部包。
 */
public class ModulePackagesAttribute extends AbstractAttribute {

    private final List<U2> packageIndices = new ArrayList<>();

    public ModulePackagesAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        int count = reader.readU2().intValue();
        for (int i = 0; i < count; i++) {
            packageIndices.add(reader.readU2());
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeShort(packageIndices.size());
        for (U2 idx : packageIndices) {
            idx.write(out);
        }
    }

    @Override
    public int contentSize() {
        return 2 + packageIndices.size() * 2;
    }

    public List<U2> getPackageIndices() {
        return Collections.unmodifiableList(packageIndices);
    }
}
