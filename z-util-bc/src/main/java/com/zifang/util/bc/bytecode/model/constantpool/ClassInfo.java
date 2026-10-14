package com.zifang.util.bc.bytecode.model.constantpool;

import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类引用信息
 * <p>
 * CONSTANT_Class_info用于表示类或接口的完全限定名。
 * name_index指向常量池中CONSTANT_Utf8_info常量的索引。
 */
public class ClassInfo extends AbstractConstantPool {

    private U2 nameIndex;

    public ClassInfo(byte tag) {
        super(tag);
    }

    /**
     * 以内部名构造常量池项，供写出层 intern 使用
     *
     * @param internalName 内部名，如 java/lang/String
     */
    public ClassInfo(String internalName) {
        super(CONSTANT_CLASS_INFO);
        this.nameIndex = new U2(0);
        this.resolvedName = internalName;
    }

    private String resolvedName;

    @Override
    public void read(InputStream inputStream) {
        this.nameIndex = U2.read(inputStream);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(getTag());
        nameIndex.write(out);
    }

    @Override
    public int contentSize() {
        return 3;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
        this.resolvedName = null;
    }

    /**
     * 构造期直接给出的内部名，写出层无需回查常量池
     *
     * @return String类型返回值
     */
    public String getResolvedName() {
        return resolvedName;
    }

    @Override
    public String toString() {
        return "ClassInfo{nameIndex=" + (nameIndex == null ? 0 : nameIndex.intValue()) + "}";
    }
}
