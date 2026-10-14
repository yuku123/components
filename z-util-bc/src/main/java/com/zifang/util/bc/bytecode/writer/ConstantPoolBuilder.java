package com.zifang.util.bc.bytecode.writer;

import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantDoubleInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantFloatInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantIntegerInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantLongInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantMethodHandleInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantMethodTypeInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ClassInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantDynamicInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantInterfaceMethodRefInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantInvokeDynamicInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantModuleInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantNameAndTypeInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantPackageInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantStringInfo;
import com.zifang.util.bc.bytecode.model.constantpool.FieldRefInfo;
import com.zifang.util.bc.bytecode.model.constantpool.MethodRefInfo;
import com.zifang.util.bc.bytecode.model.constantpool.Utf8Info;
import com.zifang.util.bc.bytecode.model.readtype.U1;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;
import com.zifang.util.bc.bytecode.model.readtype.U8;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量池构建器
 * <p>
 * 策略是「保留原池、按需追加」而不是重建：重建会让所有下标漂移，
 * 追加则让旧下标永远有效。删成员留下的孤儿条目在规范上是合法的——
 * 常量池允许有不可达项，JVM 不校验是否被引用。
 * 这样一来写出方几乎不需要重映射，唯一的风险点是双向循环依赖
 * （bootstrap 方法引用到的常量又指向别的常量），递归重映射天然解不开，
 * 因此改成显式两阶段：第一阶段递归解析 bootstrap 依赖并建立占位，第二阶段补齐。
 */
public class ConstantPoolBuilder {

    /** 常量池下标从 1 开始，list 下标 = 下标 - 1 */
    private final List<AbstractConstantPool> pool = new ArrayList<>();

    private final Map<String, Integer> utf8Index = new LinkedHashMap<>();
    private final Map<String, Integer> classIndex = new LinkedHashMap<>();
    private final Map<String, Integer> stringIndex = new LinkedHashMap<>();
    private final Map<Integer, Integer> integerIndex = new LinkedHashMap<>();
    private final Map<Integer, Integer> floatIndex = new LinkedHashMap<>();
    private final Map<Long, Integer> longIndex = new LinkedHashMap<>();
    private final Map<Long, Integer> doubleIndex = new LinkedHashMap<>();
    private final Map<String, Integer> nameAndTypeIndex = new LinkedHashMap<>();
    private final Map<String, Integer> fieldRefIndex = new LinkedHashMap<>();
    private final Map<String, Integer> methodRefIndex = new LinkedHashMap<>();
    private final Map<String, Integer> interfaceMethodRefIndex = new LinkedHashMap<>();
    private final Map<String, Integer> methodHandleIndex = new LinkedHashMap<>();
    private final Map<String, Integer> methodTypeIndex = new LinkedHashMap<>();
    private final Map<String, Integer> invokeDynamicIndex = new LinkedHashMap<>();
    private final Map<String, Integer> dynamicIndex = new LinkedHashMap<>();
    private final Map<String, Integer> moduleIndex = new LinkedHashMap<>();
    private final Map<String, Integer> packageIndex = new LinkedHashMap<>();

    private int nextIndex() {
        // long/double 之后的空槽也要占位，保持「list 下标 == 常量池下标 - 1」不变式
        return pool.size() + 1;
    }

    private int add(AbstractConstantPool entry) {
        pool.add(entry);
        return nextIndex();
    }

    private int addWide(AbstractConstantPool entry) {
        pool.add(entry);
        // 双槽常量后面的空槽
        pool.add(null);
        return nextIndex() - 1;
    }

    /**
     * intern 一个 Utf8
     *
     * @param value 字符串
     * @return 常量池下标
     */
    public int utf8(String value) {
        Integer existing = utf8Index.get(value);
        if (existing != null) {
            return existing;
        }
        int index = add(new Utf8Info(value));
        utf8Index.put(value, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_Class
     *
     * @param internalName 内部名，如 java/lang/String
     * @return 常量池下标
     */
    public int clazz(String internalName) {
        Integer existing = classIndex.get(internalName);
        if (existing != null) {
            return existing;
        }
        ClassInfo info = new ClassInfo(AbstractConstantPool.CONSTANT_CLASS_INFO);
        info.setNameIndex(new U2(utf8(internalName)));
        int index = add(info);
        classIndex.put(internalName, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_String
     *
     * @param literal 字符串字面量
     * @return 常量池下标
     */
    public int string(String literal) {
        Integer existing = stringIndex.get(literal);
        if (existing != null) {
            return existing;
        }
        int index = add(new ConstantStringInfo(new U2(utf8(literal))));
        stringIndex.put(literal, index);
        return index;
    }

    public int integer(int value) {
        Integer existing = integerIndex.get(value);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantIntegerInfo info =
                new ConstantIntegerInfo(
                        AbstractConstantPool.CONSTANT_INTEGER_INFO);
        info.setBytes(new U4(value));
        int index = add(info);
        integerIndex.put(value, index);
        return index;
    }

    public int floatConstant(int bits) {
        Integer existing = floatIndex.get(bits);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantFloatInfo info =
                new ConstantFloatInfo(
                        AbstractConstantPool.CONSTANT_FLOAT_INFO);
        info.setBytes(new U4(bits));
        int index = add(info);
        floatIndex.put(bits, index);
        return index;
    }

    public int longConstant(long value) {
        Integer existing = longIndex.get(value);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantLongInfo info =
                new ConstantLongInfo(
                        AbstractConstantPool.CONSTANT_LONG_INFO);
        info.setBytes(new U8(value));
        int index = addWide(info);
        longIndex.put(value, index);
        return index;
    }

    public int doubleConstant(long bits) {
        Integer existing = doubleIndex.get(bits);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantDoubleInfo info =
                new ConstantDoubleInfo(
                        AbstractConstantPool.CONSTANT_DOUBLE_INFO);
        info.setBytes(new U8(bits));
        int index = addWide(info);
        doubleIndex.put(bits, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_NameAndType
     *
     * @param name       名称
     * @param descriptor 描述符
     * @return 常量池下标
     */
    public int nameAndType(String name, String descriptor) {
        String key = name + ":" + descriptor;
        Integer existing = nameAndTypeIndex.get(key);
        if (existing != null) {
            return existing;
        }
        int index = add(new ConstantNameAndTypeInfo(new U2(utf8(name)), new U2(utf8(descriptor))));
        nameAndTypeIndex.put(key, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_Fieldref
     *
     * @param ownerInternalName 宿主内部名
     * @param name               字段名
     * @param descriptor         字段描述符
     * @return 常量池下标
     */
    public int fieldRef(String ownerInternalName, String name, String descriptor) {
        String key = ownerInternalName + "#" + name + ":" + descriptor;
        Integer existing = fieldRefIndex.get(key);
        if (existing != null) {
            return existing;
        }
        FieldRefInfo info = new FieldRefInfo(AbstractConstantPool.CONSTANT_FIELDREF_INFO);
        info.setClassIndex(new U2(clazz(ownerInternalName)));
        info.setDescriptorIndex(new U2(nameAndType(name, descriptor)));
        int index = add(info);
        fieldRefIndex.put(key, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_Methodref
     *
     * @param ownerInternalName 宿主内部名
     * @param name               方法名
     * @param descriptor         方法描述符
     * @return 常量池下标
     */
    public int methodRef(String ownerInternalName, String name, String descriptor) {
        String key = ownerInternalName + "#" + name + descriptor;
        Integer existing = methodRefIndex.get(key);
        if (existing != null) {
            return existing;
        }
        MethodRefInfo info = new MethodRefInfo(AbstractConstantPool.CONSTANT_METHODREF_INFO);
        info.setClassIndex(new U2(clazz(ownerInternalName)));
        info.setDescriptorIndex(new U2(nameAndType(name, descriptor)));
        int index = add(info);
        methodRefIndex.put(key, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_InterfaceMethodref
     *
     * @param ownerInternalName 宿主内部名
     * @param name               方法名
     * @param descriptor         方法描述符
     * @return 常量池下标
     */
    public int interfaceMethodRef(String ownerInternalName, String name, String descriptor) {
        String key = ownerInternalName + "#" + name + descriptor;
        Integer existing = interfaceMethodRefIndex.get(key);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantInterfaceMethodRefInfo info =
                new ConstantInterfaceMethodRefInfo(
                        AbstractConstantPool.CONSTANT_INTERFACEMETHODREF_INFO);
        info.setClassIndex(new U2(clazz(ownerInternalName)));
        info.setDescriptorIndex(new U2(nameAndType(name, descriptor)));
        int index = add(info);
        interfaceMethodRefIndex.put(key, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_MethodHandle
     *
     * @param referenceKind 引用种类 1-9
     * @param referenceIndex 指向 Fieldref/Methodref/InterfaceMethodref 的下标
     * @return 常量池下标
     */
    public int methodHandle(int referenceKind, int referenceIndex) {
        String key = referenceKind + ":" + referenceIndex;
        Integer existing = methodHandleIndex.get(key);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantMethodHandleInfo info =
                new ConstantMethodHandleInfo(
                        AbstractConstantPool.CONSTANT_METHODHANDLE_INFO);
        info.setReferenceKind(new U1((byte) referenceKind));
        info.setReferenceIndex(new U2(referenceIndex));
        int index = add(info);
        methodHandleIndex.put(key, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_MethodType
     *
     * @param descriptor 方法描述符
     * @return 常量池下标
     */
    public int methodType(String descriptor) {
        Integer existing = methodTypeIndex.get(descriptor);
        if (existing != null) {
            return existing;
        }
        com.zifang.util.bc.bytecode.model.constantpool.ConstantMethodTypeInfo info =
                new ConstantMethodTypeInfo(
                        AbstractConstantPool.CONSTANT_METHODTYPE_INFO);
        info.setDescriptorIndex(new U2(utf8(descriptor)));
        int index = add(info);
        methodTypeIndex.put(descriptor, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_InvokeDynamic
     *
     * @param bootstrapMethodAttrIndex BootstrapMethods 表下标
     * @param name                     方法名
     * @param descriptor               方法描述符
     * @return 常量池下标
     */
    public int invokeDynamic(int bootstrapMethodAttrIndex, String name, String descriptor) {
        String key = bootstrapMethodAttrIndex + ":" + name + descriptor;
        Integer existing = invokeDynamicIndex.get(key);
        if (existing != null) {
            return existing;
        }
        ConstantInvokeDynamicInfo info = new ConstantInvokeDynamicInfo(
                AbstractConstantPool.CONSTANT_INVOKEDYNAMIC_INFO);
        info.setBootstrapMethodAttrIndex(new U2(bootstrapMethodAttrIndex));
        info.setNameAndTypeIndex(new U2(nameAndType(name, descriptor)));
        int index = add(info);
        invokeDynamicIndex.put(key, index);
        return index;
    }

    /**
     * intern 一个 CONSTANT_Dynamic
     *
     * @param bootstrapMethodAttrIndex BootstrapMethods 表下标
     * @param name                     名称
     * @param descriptor               描述符
     * @return 常量池下标
     */
    public int dynamic(int bootstrapMethodAttrIndex, String name, String descriptor) {
        String key = bootstrapMethodAttrIndex + ":" + name + descriptor;
        Integer existing = dynamicIndex.get(key);
        if (existing != null) {
            return existing;
        }
        ConstantDynamicInfo info = new ConstantDynamicInfo(AbstractConstantPool.CONSTANT_DYNAMIC_INFO);
        info.setBootstrapMethodAttrIndex(new U2(bootstrapMethodAttrIndex));
        info.setNameAndTypeIndex(new U2(nameAndType(name, descriptor)));
        int index = add(info);
        dynamicIndex.put(key, index);
        return index;
    }

    public int module(String moduleName) {
        Integer existing = moduleIndex.get(moduleName);
        if (existing != null) {
            return existing;
        }
        ConstantModuleInfo info = new ConstantModuleInfo(AbstractConstantPool.CONSTANT_MODULE_INFO);
        info.setIndex(new U2(utf8(moduleName)));
        int index = add(info);
        moduleIndex.put(moduleName, index);
        return index;
    }

    public int packageInfo(String packageName) {
        Integer existing = packageIndex.get(packageName);
        if (existing != null) {
            return existing;
        }
        ConstantPackageInfo info = new ConstantPackageInfo(AbstractConstantPool.CONSTANT_PACKAGE_INFO);
        info.setIndex(new U2(utf8(packageName)));
        int index = add(info);
        packageIndex.put(packageName, index);
        return index;
    }

    /**
     * 直接登记一条已构造好的常量项（用于无法用结构化参数表达的场景）
     *
     * @param entry 常量项
     * @return 常量池下标
     */
    public int register(AbstractConstantPool entry) {
        return entry.getTag() == AbstractConstantPool.CONSTANT_LONG_INFO
                || entry.getTag() == AbstractConstantPool.CONSTANT_DOUBLE_INFO
                ? addWide(entry) : add(entry);
    }

    /**
     * 以既有常量池为基底继续构建
     * <p>
     * 原顺序原样保留（long/double 的空槽也原样保留），因此池内所有旧下标不变；
     * 同时把已有条目登记进 intern 表，避免追加重复项。
     *
     * @param existing 既有常量池
     * @param helper   既有常量池的语义解析器
     * @return ConstantPoolBuilder类型返回值
     */
    public ConstantPoolBuilder seed(List<AbstractConstantPool> existing, ConstantPoolHelper helper) {
        pool.addAll(existing);
        for (int i = 0; i < existing.size(); i++) {
            int index = i + 1;
            AbstractConstantPool entry = existing.get(i);
            if (entry == null) {
                continue;
            }
            switch (entry.getTag()) {
                case AbstractConstantPool.CONSTANT_UTF8_INFO:
                    utf8Index.putIfAbsent(((Utf8Info) entry).getValue(), index);
                    break;
                case AbstractConstantPool.CONSTANT_CLASS_INFO:
                    classIndex.putIfAbsent(helper.classInternalName(index), index);
                    break;
                case AbstractConstantPool.CONSTANT_STRING_INFO:
                    stringIndex.putIfAbsent(helper.stringLiteral(index), index);
                    break;
                case AbstractConstantPool.CONSTANT_INTEGER_INFO:
                    integerIndex.putIfAbsent(
                            ((ConstantIntegerInfo) entry).getBytes().getValue(), index);
                    break;
                case AbstractConstantPool.CONSTANT_FLOAT_INFO:
                    floatIndex.putIfAbsent(
                            ((ConstantFloatInfo) entry).getBytes().getValue(), index);
                    break;
                case AbstractConstantPool.CONSTANT_LONG_INFO:
                    longIndex.putIfAbsent(
                            ((ConstantLongInfo) entry).getBytes().getValue(), index);
                    break;
                case AbstractConstantPool.CONSTANT_DOUBLE_INFO:
                    doubleIndex.putIfAbsent(
                            ((ConstantDoubleInfo) entry).getBytes().getValue(), index);
                    break;
                case AbstractConstantPool.CONSTANT_NAMEANDTYPE_INFO: {
                    ConstantNameAndTypeInfo nat = (ConstantNameAndTypeInfo) entry;
                    String key = helper.utf8(nat.getNameIndex().intValue()) + ":"
                            + helper.utf8(nat.getDescriptorIndex().intValue());
                    nameAndTypeIndex.putIfAbsent(key, index);
                    break;
                }
                case AbstractConstantPool.CONSTANT_FIELDREF_INFO:
                    fieldRefIndex.putIfAbsent(fieldRefKey(helper, index), index);
                    break;
                case AbstractConstantPool.CONSTANT_METHODREF_INFO:
                    methodRefIndex.putIfAbsent(memberKey(helper, index), index);
                    break;
                case AbstractConstantPool.CONSTANT_INTERFACEMETHODREF_INFO:
                    interfaceMethodRefIndex.putIfAbsent(memberKey(helper, index), index);
                    break;
                case AbstractConstantPool.CONSTANT_METHODHANDLE_INFO: {
                    ConstantMethodHandleInfo mh = (ConstantMethodHandleInfo) entry;
                    methodHandleIndex.putIfAbsent(
                            mh.getReferenceKind().getValue() + ":" + mh.getReferenceIndex().intValue(), index);
                    break;
                }
                case AbstractConstantPool.CONSTANT_METHODTYPE_INFO:
                    methodTypeIndex.putIfAbsent(
                            helper.utf8(((ConstantMethodTypeInfo) entry).getDescriptorIndex().intValue()), index);
                    break;
                case AbstractConstantPool.CONSTANT_INVOKEDYNAMIC_INFO: {
                    ConstantInvokeDynamicInfo indy = (ConstantInvokeDynamicInfo) entry;
                    invokeDynamicIndex.putIfAbsent(invokeDynamicKey(helper, indy), index);
                    break;
                }
                case AbstractConstantPool.CONSTANT_DYNAMIC_INFO: {
                    ConstantDynamicInfo dyn = (ConstantDynamicInfo) entry;
                    dynamicIndex.putIfAbsent(dynamicKey(helper,
                            dyn.getBootstrapMethodAttrIndex().intValue(),
                            dyn.getNameAndTypeIndex().intValue()), index);
                    break;
                }
                default:
                    break;
            }
        }
        return this;
    }

    private String fieldRefKey(ConstantPoolHelper helper, int index) {
        return helper.fieldOwner(index) + "#" + helper.fieldName(index) + ":" + helper.fieldDescriptor(index);
    }

    private String memberKey(ConstantPoolHelper helper, int index) {
        return helper.methodOwner(index) + "#" + helper.methodName(index) + helper.methodDescriptor(index);
    }

    private String invokeDynamicKey(ConstantPoolHelper helper, ConstantInvokeDynamicInfo indy) {
        return dynamicKey(helper,
                indy.getBootstrapMethodAttrIndex().intValue(),
                indy.getNameAndTypeIndex().intValue());
    }

    private String dynamicKey(ConstantPoolHelper helper, int bootstrapIndex, int nameAndTypeIndex) {
        ConstantNameAndTypeInfo nat = (ConstantNameAndTypeInfo) helper.get(nameAndTypeIndex);
        return bootstrapIndex + ":"
                + helper.utf8(nat.getNameIndex().intValue())
                + helper.utf8(nat.getDescriptorIndex().intValue());
    }

    /**
     * constant_pool_count = 实际条目数 + 1
     *
     * @return int类型返回值
     */
    public int constantPoolCount() {
        return pool.size() + 1;
    }

    /**
     * 构建完成后的常量池
     *
     * @return ListAbstractConstantPool类型返回值
     */
    public List<AbstractConstantPool> build() {
        return pool;
    }

    /**
     * 已 intern 的 Utf8 文本
     *
     * @return MapString,类型参数
     */
    public Map<String, Integer> internedUtf8() {
        return utf8Index;
    }
}
