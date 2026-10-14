package com.zifang.util.bc.bytecode.model;

import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.AttributeNames;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantPoolInfo;
import com.zifang.util.bc.bytecode.model.field.FieldInfo;
import com.zifang.util.bc.bytecode.model.field.FieldTable;
import com.zifang.util.bc.bytecode.model.inter.Interface;
import com.zifang.util.bc.bytecode.model.inter.InterfaceIndex;
import com.zifang.util.bc.bytecode.model.method.MethodInfo;
import com.zifang.util.bc.bytecode.model.method.MethodTable;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ClassFile结构
 * <p>
 * 代表一个完整的.class文件结构，遵循JVM规范。
 */
public class ClassFile {
    /**
     * 魔数，固定为0xCAFEBABE
     */
    public U4 magic;

    /**
     * 次版本号
     */
    public U2 minorVersion;

    /**
     * 主版本号 (52=Java8, 55=Java11, 61=Java17)
     */
    public U2 majorVersion;

    /**
     * 常量池大小
     */
    public U2 constantPoolSize;

    /**
     * 常量池内容
     */
    public ConstantPoolInfo poolInfo;

    /**
     * 类的访问标志 (ACC_PUBLIC, ACC_FINAL等)
     */
    public U2 accessFlag;

    /**
     * 类索引，指向常量池中的CONSTANT_Class_info
     */
    public U2 classIndex;

    /**
     * 父类索引，指向常量池中的CONSTANT_Class_info
     */
    public U2 superClassIndex;

    /**
     * 接口索引表
     */
    public InterfaceIndex interfaceIndex;

    /**
     * 字段表
     */
    public FieldTable fieldInfo;

    /**
     * 方法表
     */
    public MethodTable methodInfo;

    /**
     * 获取类名
     *
     * @return 类名字符串
     */
    public String getClassName() {
        return new ConstantPoolHelper(poolInfo == null ? null : poolInfo.getPoolList())
                .classInternalName(classIndex == null ? 0 : classIndex.intValue());
    }

    /**
     * 获取父类内部名
     *
     * @return String类型返回值；Object 或无父类时为 null
     */
    public String getSuperClassName() {
        if (superClassIndex == null || superClassIndex.intValue() == 0) {
            return null;
        }
        return new ConstantPoolHelper(poolInfo.getPoolList())
                .classInternalName(superClassIndex.intValue());
    }

    /**
     * 获取实现的接口内部名列表
     *
     * @return ListString类型返回值
     */
    public List<String> getInterfaceNames() {
        List<String> names = new ArrayList<>();
        if (interfaceIndex == null) {
            return names;
        }
        ConstantPoolHelper helper = new ConstantPoolHelper(poolInfo.getPoolList());
        for (Interface iface : interfaceIndex.list) {
            names.add(helper.classInternalName(iface.index.value));
        }
        return names;
    }

    /**
     * Java 主版本号
     * <p>
     * major 45=JDK1.1，49=Java5，52=Java8，55=Java11，61=Java17，65=Java21。
     *
     * @return int类型返回值
     */
    public int getJavaMajorVersion() {
        return majorVersion == null ? 0 : majorVersion.intValue();
    }

    /**
     * 是否为预览版 class（minor_version 固定为 0xFFFF）
     *
     * @return boolean类型返回值
     */
    public boolean isPreview() {
        return minorVersion != null && minorVersion.intValue() == 0xFFFF;
    }

    /**
     * 是否使用类型检查验证器（major ≥ 50 即必须有 StackMapTable）
     *
     * @return boolean类型返回值
     */
    public boolean requiresStackMapTable() {
        return getJavaMajorVersion() >= 50;
    }

    /**
     * 字段表
     *
     * @return ListFieldInfo类型返回值
     */
    public List<FieldInfo> getFields() {
        return fieldInfo == null ? Collections.emptyList() : fieldInfo.getFields();
    }

    /**
     * 方法表
     *
     * @return ListMethodInfo类型返回值
     */
    public List<MethodInfo> getMethods() {
        return methodInfo == null ? Collections.emptyList() : methodInfo.getMethods();
    }

    /**
     * 按方法名 + 描述符查找方法
     *
     * @param methodName       方法名
     * @param methodDescriptor 方法描述符
     * @return MethodInfo类型返回值
     */
    public MethodInfo findMethod(String methodName, String methodDescriptor) {
        ConstantPoolHelper helper = new ConstantPoolHelper(poolInfo.getPoolList());
        for (MethodInfo m : getMethods()) {
            if (helper.utf8(m.getNameIndex().intValue()).equals(methodName)
                    && helper.utf8(m.getDescriptorIndex().intValue()).equals(methodDescriptor)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 按方法名查找（描述符未知时的宽松匹配，重载方法只会命中第一个）
     *
     * @param methodName 方法名
     * @return MethodInfo类型返回值
     */
    public MethodInfo findMethod(String methodName) {
        ConstantPoolHelper helper = new ConstantPoolHelper(poolInfo.getPoolList());
        for (MethodInfo m : getMethods()) {
            if (helper.utf8(m.getNameIndex().intValue()).equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 按字段名查找
     *
     * @param fieldName 字段名
     * @return FieldInfo类型返回值
     */
    public FieldInfo findField(String fieldName) {
        ConstantPoolHelper helper = new ConstantPoolHelper(poolInfo.getPoolList());
        for (FieldInfo f : getFields()) {
            if (helper.utf8(f.getNameIndex().intValue()).equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 按属性名查找类级属性
     *
     * @param attributeName 属性名
     * @return AbstractAttribute类型返回值
     */
    public AbstractAttribute findAttribute(String attributeName) {
        return AttributeNames.find(attributes, attributeName);
    }

    /**
     * 获取方法数量
     *
     * @return 方法数量
     */
    public int getMethodCount() {
        if (methodInfo == null) {
            return 0;
        }
        return methodInfo.getMethodCount();
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "ClassFile{" +
                "magic=" + (magic != null ? String.format("0x%08X", magic.value) : "null") +
                ", majorVersion=" + (majorVersion != null ? majorVersion.value : 0) +
                ", minorVersion=" + (minorVersion != null ? minorVersion.value : 0) +
                ", constantPoolSize=" + (constantPoolSize != null ? constantPoolSize.value : 0) +
                '}';
    }


    /**
     * getMagic方法。
     *
     * @return U4类型返回值
     */
    public U4 getMagic() {
        return magic;
    }

    /**
     * setMagic方法。
     * * @param magic U4类型参数
     */
    public void setMagic(U4 magic) {
        this.magic = magic;
    }

    /**
     * getMinorVersion方法。
     *
     * @return U2类型返回值
     */
    public U2 getMinorVersion() {
        return minorVersion;
    }

    /**
     * setMinorVersion方法。
     * * @param minorVersion U2类型参数
     */
    public void setMinorVersion(U2 minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * getMajorVersion方法。
     *
     * @return U2类型返回值
     */
    public U2 getMajorVersion() {
        return majorVersion;
    }

    /**
     * setMajorVersion方法。
     * * @param majorVersion U2类型参数
     */
    public void setMajorVersion(U2 majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * getConstantPoolSize方法。
     *
     * @return U2类型返回值
     */
    public U2 getConstantPoolSize() {
        return constantPoolSize;
    }

    /**
     * setConstantPoolSize方法。
     * * @param constantPoolSize U2类型参数
     */
    public void setConstantPoolSize(U2 constantPoolSize) {
        this.constantPoolSize = constantPoolSize;
    }

    /**
     * getPoolInfo方法。
     *
     * @return ConstantPoolInfo类型返回值
     */
    public ConstantPoolInfo getPoolInfo() {
        return poolInfo;
    }

    /**
     * setPoolInfo方法。
     * * @param poolInfo ConstantPoolInfo类型参数
     */
    public void setPoolInfo(ConstantPoolInfo poolInfo) {
        this.poolInfo = poolInfo;
    }

    /**
     * getAccessFlag方法。
     *
     * @return U2类型返回值
     */
    public U2 getAccessFlag() {
        return accessFlag;
    }

    /**
     * setAccessFlag方法。
     * * @param accessFlag U2类型参数
     */
    public void setAccessFlag(U2 accessFlag) {
        this.accessFlag = accessFlag;
    }

    /**
     * getClassIndex方法。
     *
     * @return U2类型返回值
     */
    public U2 getClassIndex() {
        return classIndex;
    }

    /**
     * setClassIndex方法。
     * * @param classIndex U2类型参数
     */
    public void setClassIndex(U2 classIndex) {
        this.classIndex = classIndex;
    }

    /**
     * getSuperClassIndex方法。
     *
     * @return U2类型返回值
     */
    public U2 getSuperClassIndex() {
        return superClassIndex;
    }

    /**
     * setSuperClassIndex方法。
     * * @param superClassIndex U2类型参数
     */
    public void setSuperClassIndex(U2 superClassIndex) {
        this.superClassIndex = superClassIndex;
    }

    /**
     * getInterfaceIndex方法。
     *
     * @return InterfaceIndex类型返回值
     */
    public InterfaceIndex getInterfaceIndex() {
        return interfaceIndex;
    }

    /**
     * setInterfaceIndex方法。
     * * @param interfaceIndex InterfaceIndex类型参数
     */
    public void setInterfaceIndex(InterfaceIndex interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    /**
     * getFieldInfo方法。
     *
     * @return FieldTable类型返回值
     */
    public FieldTable getFieldInfo() {
        return fieldInfo;
    }

    /**
     * setFieldInfo方法。
     * * @param fieldInfo FieldTable类型参数
     */
    public void setFieldInfo(FieldTable fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    /**
     * getMethodInfo方法。
     *
     * @return MethodTable类型返回值
     */
    public MethodTable getMethodInfo() {
        return methodInfo;
    }

    /**
     * setMethodInfo方法。
     * * @param methodInfo MethodTable类型参数
     */
    public void setMethodInfo(MethodTable methodInfo) {
        this.methodInfo = methodInfo;
    }

    /**
     * 类级属性表（InnerClasses / NestHost / NestMembers / RuntimeVisibleAnnotations /
     * BootstrapMethods / Signature 等）。由 ByteCodeResolver.parse() 在方法表之后读出。
     */
    private final List<AbstractAttribute> attributes = new ArrayList<>();

    public List<AbstractAttribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    /**
     * 由 ByteCodeResolver 内部调用：往类级属性表追加一项。
     */
    public void addAttribute(AbstractAttribute a) {
        if (a != null) {
            attributes.add(a);
        }
    }

    /**
     * 摘掉一项类级属性
     * <p>
     * 删属性会留下常量池里的孤儿条目，JVM 不校验可达性，因此不用回填池。
     *
     * @param a 要摘掉的属性
     * @return 是否摘掉了
     */
    public boolean removeAttribute(AbstractAttribute a) {
        return attributes.remove(a);
    }
}
