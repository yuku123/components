package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.ClassInfo;
import com.zifang.util.bc.bytecode.model.constantpool.Utf8Info;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exceptions 属性（4.7.5）：方法显式声明的检查型异常类型列表。
 */
public class ExceptionsAttribute extends AbstractAttribute implements PoolResolvable {

    private final List<U2> exceptionIndexTable = new ArrayList<>();
    private final List<String> exceptionClassNames = new ArrayList<>();

    public ExceptionsAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        U2 count = U2.read(inputStream);
        for (int i = 0; i < count.value; i++) {
            U2 idx = U2.read(inputStream);
            exceptionIndexTable.add(idx);
        }
    }

    /**
     * 把异常类的常量池索引解析为外部形式的全限定名（点分）。
     */
    @Override
    public void resolve(List<AbstractConstantPool> poolList) {
        exceptionClassNames.clear();
        for (U2 idx : exceptionIndexTable) {
            int i = idx.value - 1;
            if (i < 0 || i >= poolList.size()) {
                continue;
            }
            AbstractConstantPool p = poolList.get(i);
            if (!(p instanceof ClassInfo)) {
                continue;
            }
            int nameIdx = ((ClassInfo) p).getNameIndex().value - 1;
            if (nameIdx < 0 || nameIdx >= poolList.size()) {
                continue;
            }
            AbstractConstantPool utf = poolList.get(nameIdx);
            if (utf instanceof Utf8Info) {
                String internal = ((Utf8Info) utf).getValue();
                exceptionClassNames.add(internal.replace('/', '.'));
            }
        }
    }

    public List<U2> getExceptionIndexTable() {
        return Collections.unmodifiableList(exceptionIndexTable);
    }

    public List<String> getExceptionClassNames() {
        return Collections.unmodifiableList(exceptionClassNames);
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        out.writeShort(exceptionIndexTable.size());
        for (U2 idx : exceptionIndexTable) {
            idx.write(out);
        }
    }

    @Override
    public int contentSize() {
        return 2 + exceptionIndexTable.size() * 2;
    }
}