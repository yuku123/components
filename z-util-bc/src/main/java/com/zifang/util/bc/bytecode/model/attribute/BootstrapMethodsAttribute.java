package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BootstrapMethods 属性（4.7.21）：invokedynamic 用到的引导方法表。
 */
public class BootstrapMethodsAttribute extends AbstractAttribute {

    public static final class BootstrapMethod {
        public U2 bootstrapMethodRef;
        public final List<U2> bootstrapArguments = new ArrayList<>();
    }

    private final List<BootstrapMethod> entries = new ArrayList<>();

    public BootstrapMethodsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        U2 count = U2.read(inputStream);
        for (int i = 0; i < count.value; i++) {
            BootstrapMethod bm = new BootstrapMethod();
            bm.bootstrapMethodRef = U2.read(inputStream);
            U2 numArgs = U2.read(inputStream);
            for (int j = 0; j < numArgs.value; j++) {
                bm.bootstrapArguments.add(U2.read(inputStream));
            }
            entries.add(bm);
        }
    }

    public List<BootstrapMethod> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeShort(entries.size());
        for (BootstrapMethod bm : entries) {
            bm.bootstrapMethodRef.write(out);
            out.writeShort(bm.bootstrapArguments.size());
            for (U2 arg : bm.bootstrapArguments) {
                arg.write(out);
            }
        }
    }

    @Override
    public int contentSize() {
        int n = 2;
        for (BootstrapMethod bm : entries) {
            n += 4 + bm.bootstrapArguments.size() * 2;
        }
        return n;
    }
}