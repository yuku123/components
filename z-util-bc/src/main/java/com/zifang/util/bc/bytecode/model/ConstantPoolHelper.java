package com.zifang.util.bc.bytecode.model;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.ClassInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantInterfaceMethodRefInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantNameAndTypeInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantStringInfo;
import com.zifang.util.bc.bytecode.model.constantpool.FieldRefInfo;
import com.zifang.util.bc.bytecode.model.constantpool.MethodRefInfo;
import com.zifang.util.bc.bytecode.model.constantpool.Utf8Info;

import java.util.List;

/**
 * 常量池语义解析
 * <p>
 * class 文件里所有跨结构引用都是常量池下标，调用方真正想要的是名字或描述符文本。
 * 这里统一做「下标 → 语义」的翻译，并按规范校验下标指向的类型，
 * 越界或类型不符一律返回 null 而不抛异常——解析器面对的可能本来就是畸形类文件。
 */
public class ConstantPoolHelper {

    private final List<AbstractConstantPool> poolList;

    public ConstantPoolHelper(List<AbstractConstantPool> poolList) {
        this.poolList = poolList;
    }

    private AbstractConstantPool at(int index) {
        if (index <= 0 || poolList == null || index > poolList.size()) {
            return null;
        }
        return poolList.get(index - 1);
    }

    /**
     * 按下标取常量池项
     *
     * @param index 常量池下标（1 起）
     * @return AbstractConstantPool类型返回值
     */
    public AbstractConstantPool get(int index) {
        return at(index);
    }

    public int size() {
        return poolList == null ? 0 : poolList.size();
    }

    /**
     * 取 CONSTANT_Utf8_info 的文本
     *
     * @param index 常量池下标
     * @return String类型返回值
     */
    public String utf8(int index) {
        AbstractConstantPool p = at(index);
        return p instanceof Utf8Info ? ((Utf8Info) p).getValue() : null;
    }

    /**
     * 取 CONSTANT_Class_info 的内部名（如 java/lang/String）
     *
     * @param index 常量池下标
     * @return String类型返回值
     */
    public String classInternalName(int index) {
        AbstractConstantPool p = at(index);
        if (!(p instanceof ClassInfo)) {
            return null;
        }
        return utf8(((ClassInfo) p).getNameIndex().intValue());
    }

    /**
     * 取 CONSTANT_String_info 的字符串字面量
     *
     * @param index 常量池下标
     * @return String类型返回值
     */
    public String stringLiteral(int index) {
        AbstractConstantPool p = at(index);
        if (!(p instanceof ConstantStringInfo)) {
            return null;
        }
        return utf8(((ConstantStringInfo) p).getStringIndex().intValue());
    }

    /**
     * 解析 CONSTANT_NameAndType_info
     *
     * @param index 常量池下标
     * @return ConstantNameAndTypeInfo类型返回值
     */
    public ConstantNameAndTypeInfo nameAndType(int index) {
        AbstractConstantPool p = at(index);
        return p instanceof ConstantNameAndTypeInfo ? (ConstantNameAndTypeInfo) p : null;
    }

    /**
     * 字段引用 → 宿主内部名
     *
     * @param fieldRefIndex CONSTANT_Fieldref_info 下标
     * @return String类型返回值
     */
    public String fieldOwner(int fieldRefIndex) {
        AbstractConstantPool p = at(fieldRefIndex);
        return p instanceof FieldRefInfo
                ? classInternalName(((FieldRefInfo) p).getClassIndex().intValue()) : null;
    }

    /**
     * 字段引用 → 字段名
     *
     * @param fieldRefIndex CONSTANT_Fieldref_info 下标
     * @return String类型返回值
     */
    public String fieldName(int fieldRefIndex) {
        AbstractConstantPool p = at(fieldRefIndex);
        if (!(p instanceof FieldRefInfo)) {
            return null;
        }
        ConstantNameAndTypeInfo nat =
                nameAndType(((FieldRefInfo) p).getDescriptorIndex().intValue());
        return nat == null ? null : utf8(nat.getNameIndex().intValue());
    }

    /**
     * 字段引用 → 字段描述符
     *
     * @param fieldRefIndex CONSTANT_Fieldref_info 下标
     * @return String类型返回值
     */
    public String fieldDescriptor(int fieldRefIndex) {
        AbstractConstantPool p = at(fieldRefIndex);
        if (!(p instanceof FieldRefInfo)) {
            return null;
        }
        ConstantNameAndTypeInfo nat =
                nameAndType(((FieldRefInfo) p).getDescriptorIndex().intValue());
        return nat == null ? null : utf8(nat.getDescriptorIndex().intValue());
    }

    /**
     * 方法引用（Methodref 或 InterfaceMethodref）→ 宿主内部名
     *
     * @param methodRefIndex 常量池下标
     * @return String类型返回值
     */
    public String methodOwner(int methodRefIndex) {
        AbstractConstantPool p = at(methodRefIndex);
        if (p instanceof MethodRefInfo) {
            return classInternalName(((MethodRefInfo) p).getClassIndex().intValue());
        }
        if (p instanceof ConstantInterfaceMethodRefInfo) {
            return classInternalName(((ConstantInterfaceMethodRefInfo) p).getClassIndex().intValue());
        }
        return null;
    }

    /**
     * 方法引用 → 方法名
     *
     * @param methodRefIndex 常量池下标
     * @return String类型返回值
     */
    public String methodName(int methodRefIndex) {
        return memberName(methodRefIndex);
    }

    /**
     * 方法引用 → 方法描述符
     *
     * @param methodRefIndex 常量池下标
     * @return String类型返回值
     */
    public String methodDescriptor(int methodRefIndex) {
        return memberDescriptor(methodRefIndex);
    }

    private String memberName(int index) {
        AbstractConstantPool p = at(index);
        U2Holder nat = nameAndTypeOf(p);
        return nat == null ? null : utf8(nat.nameIndex);
    }

    private String memberDescriptor(int index) {
        AbstractConstantPool p = at(index);
        U2Holder nat = nameAndTypeOf(p);
        return nat == null ? null : utf8(nat.descriptorIndex);
    }

    private U2Holder nameAndTypeOf(AbstractConstantPool p) {
        int descriptorIndex;
        if (p instanceof MethodRefInfo) {
            descriptorIndex = ((MethodRefInfo) p).getDescriptorIndex().intValue();
        } else if (p instanceof ConstantInterfaceMethodRefInfo) {
            descriptorIndex = ((ConstantInterfaceMethodRefInfo) p).getDescriptorIndex().intValue();
        } else if (p instanceof FieldRefInfo) {
            descriptorIndex = ((FieldRefInfo) p).getDescriptorIndex().intValue();
        } else {
            return null;
        }
        ConstantNameAndTypeInfo nat = nameAndType(descriptorIndex);
        return nat == null ? null : new U2Holder(
                nat.getNameIndex().intValue(), nat.getDescriptorIndex().intValue());
    }

    private static final class U2Holder {
        final int nameIndex;
        final int descriptorIndex;

        U2Holder(int nameIndex, int descriptorIndex) {
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
        }
    }
}
