package com.zifang.util.bc.bytecode.editor;

import com.zifang.util.bc.bytecode.builder.InstructionBuilder;
import com.zifang.util.bc.compile.MapClassLoader;
import com.zifang.util.bc.bytecode.model.ClassFile;
import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.AnnotationsAttribute;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.attribute.RuntimeVisibleAnnotationsAttribute;
import com.zifang.util.bc.bytecode.model.attribute.StackMapTableAttribute;
import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.field.FieldInfo;
import com.zifang.util.bc.bytecode.model.method.MethodInfo;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;
import com.zifang.util.bc.bytecode.resolver.ByteCodeResolver;
import com.zifang.util.bc.bytecode.verify.FrameComputer;
import com.zifang.util.bc.bytecode.verify.StackMapFrameEntry;
import com.zifang.util.bc.bytecode.writer.ClassWriter;
import com.zifang.util.bc.bytecode.writer.ConstantPoolBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * class 变更门面
 * <p>
 * 解析出来的模型是「只读快照」，直接改字段列表能改，但改完还有两件事必须跟着做：
 * 新加的名字/描述符得进常量池，方法体动过就得重算 max_stack、max_locals 和栈帧表。
 * 这三件事散在各处最容易漏，本类把它们收成一条链——任何变更只管登记「脏」，
 * 输出前统一补齐。
 * <p>
 * 输出有三条通道：{@link #toBytes()} 拿字节、{@link #writeTo(String)} 落盘、
 * {@link #defineAndLoad()} 直接内存加载可运行。
 */
public class ClassEditor {

    private static final int ACC_STATIC = 0x0008;

    private final ClassFile classFile;
    private final ConstantPoolBuilder poolBuilder;

    /** 方法体被改过、输出前需要重算栈帧的方法 */
    private final Set<String> dirtyMethods = new LinkedHashSet<>();

    /**
     * @param classFile 待变更的类模型
     */
    public ClassEditor(ClassFile classFile) {
        this.classFile = classFile;
        this.poolBuilder = new ConstantPoolBuilder().seed(classFile.getPoolInfo().getPoolList(),
                new ConstantPoolHelper(classFile.getPoolInfo().getPoolList()));
        // 模型里的常量池换成构造器这份活的：查找方法（如 findField）要靠它解析名字，
        // 指向原池快照的话新加的成员一律解析成 null
        classFile.getPoolInfo().setPoolList(poolBuilder.build());
    }

    /**
     * 从 class 字节直接开一个变更器
     *
     * @param bytes class 文件字节
     * @return ClassEditor类型返回值
     */
    public static ClassEditor of(byte[] bytes) {
        return new ClassEditor(ByteCodeResolver.parseFromStream(new ByteArrayInputStream(bytes)));
    }

    /**
     * 被编辑的类模型
     *
     * @return ClassFile类型返回值
     */
    public ClassFile classFile() {
        return classFile;
    }

    /**
     * 常量池构造器：变更过程中要新增常量（如方法引用）时经它取下标
     *
     * @return ConstantPoolBuilder类型返回值
     */
    public ConstantPoolBuilder pool() {
        return poolBuilder;
    }

    // ---------------------------------------------------------------- 类级

    /**
     * 打开访问标志位
     *
     * @param flag JVMS §4.1 的类访问标志
     * @return ClassEditor类型返回值
     */
    public ClassEditor addClassFlag(int flag) {
        classFile.getAccessFlag().setValue(classFile.getAccessFlag().intValue() | flag);
        return this;
    }

    /**
     * 关闭访问标志位
     *
     * @param flag 访问标志
     * @return ClassEditor类型返回值
     */
    public ClassEditor removeClassFlag(int flag) {
        classFile.getAccessFlag().setValue(classFile.getAccessFlag().intValue() & ~flag);
        return this;
    }

    /**
     * 直接设置类访问标志
     *
     * @param flags 新标志值
     * @return ClassEditor类型返回值
     */
    public ClassEditor setClassFlags(int flags) {
        classFile.getAccessFlag().setValue(flags);
        return this;
    }

    /**
     * 改类名
     *
     * @param internalName 新的内部名
     * @return ClassEditor类型返回值
     */
    public ClassEditor renameClass(String internalName) {
        classFile.setClassIndex(new U2(poolBuilder.clazz(internalName)));
        return this;
    }

    // ---------------------------------------------------------------- 字段

    /**
     * 加一个字段
     *
     * @param accessFlags 字段访问标志
     * @param name         字段名
     * @param descriptor   字段描述符
     * @return ClassEditor类型返回值
     */
    public ClassEditor addField(int accessFlags, String name, String descriptor) {
        if (classFile.findField(name) != null) {
            throw new IllegalStateException("字段已存在: " + name);
        }
        classFile.getFields().add(new FieldInfo(new U2(accessFlags), new U2(poolBuilder.utf8(name)),
                new U2(poolBuilder.utf8(descriptor)), new U2(0), new ArrayList<AbstractAttribute>(),
                poolList()));
        return this;
    }

    /**
     * 删一个字段
     *
     * @param name 字段名
     * @return ClassEditor类型返回值
     */
    public ClassEditor removeField(String name) {
        boolean removed = classFile.getFields().removeIf(f -> name.equals(f.getName()));
        if (!removed) {
            throw new IllegalStateException("字段不存在: " + name);
        }
        return this;
    }

    /**
     * 改字段名
     *
     * @param oldName 旧名
     * @param newName 新名
     * @return ClassEditor类型返回值
     */
    public ClassEditor renameField(String oldName, String newName) {
        FieldInfo field = requireField(oldName);
        field.getNameIndex().setValue(poolBuilder.utf8(newName));
        return this;
    }

    /**
     * 改字段访问标志
     *
     * @param name         字段名
     * @param accessFlags  新标志
     * @return ClassEditor类型返回值
     */
    public ClassEditor setFieldFlags(String name, int accessFlags) {
        requireField(name).getAccessFlags().setValue(accessFlags);
        return this;
    }

    private FieldInfo requireField(String name) {
        FieldInfo field = classFile.findField(name);
        if (field == null) {
            throw new IllegalStateException("字段不存在: " + name);
        }
        return field;
    }

    // ---------------------------------------------------------------- 方法

    /**
     * 加一个方法
     *
     * @param accessFlags 方法访问标志
     * @param name        方法名
     * @param descriptor  方法描述符
     * @param body        方法体；传 null 表示抽象方法（无 Code 属性）
     * @return ClassEditor类型返回值
     */
    public ClassEditor addMethod(int accessFlags, String name, String descriptor, InstructionBuilder body) {
        if (classFile.findMethod(name, descriptor) != null) {
            throw new IllegalStateException("方法已存在: " + name + descriptor);
        }
        List<AbstractAttribute> attributes = new ArrayList<>();
        if (body != null) {
            attributes.add(newCodeAttribute(body));
            dirtyMethods.add(key(name, descriptor));
        }
        classFile.getMethods().add(new MethodInfo(new U2(accessFlags), new U2(poolBuilder.utf8(name)),
                new U2(poolBuilder.utf8(descriptor)), new U2(attributes.size()), attributes, poolList()));
        return this;
    }

    /**
     * 删一个方法
     *
     * @param name       方法名
     * @param descriptor 方法描述符
     * @return ClassEditor类型返回值
     */
    public ClassEditor removeMethod(String name, String descriptor) {
        boolean removed = classFile.getMethods()
                .removeIf(m -> name.equals(m.getName()) && descriptor.equals(m.getDescriptor()));
        if (!removed) {
            throw new IllegalStateException("方法不存在: " + name + descriptor);
        }
        dirtyMethods.remove(key(name, descriptor));
        return this;
    }

    /**
     * 改方法名
     *
     * @param oldName      旧名
     * @param oldDescriptor 旧描述符
     * @param newName      新名
     * @return ClassEditor类型返回值
     */
    public ClassEditor renameMethod(String oldName, String oldDescriptor, String newName) {
        MethodInfo method = requireMethod(oldName, oldDescriptor);
        method.getNameIndex().setValue(poolBuilder.utf8(newName));
        if (dirtyMethods.remove(key(oldName, oldDescriptor))) {
            dirtyMethods.add(key(newName, oldDescriptor));
        }
        return this;
    }

    /**
     * 改方法访问标志
     *
     * @param name        方法名
     * @param descriptor  方法描述符
     * @param accessFlags 新标志
     * @return ClassEditor类型返回值
     */
    public ClassEditor setMethodFlags(String name, String descriptor, int accessFlags) {
        requireMethod(name, descriptor).getAccessFlags().setValue(accessFlags);
        return this;
    }

    /**
     * 整个替换某个方法的方法体
     *
     * @param name       方法名
     * @param descriptor 方法描述符
     * @param body       新的方法体
     * @return ClassEditor类型返回值
     */
    public ClassEditor rewriteMethodBody(String name, String descriptor, InstructionBuilder body) {
        MethodInfo method = requireMethod(name, descriptor);
        Code code = codeOf(method);
        if (code == null) {
            throw new IllegalStateException("方法没有 Code 属性，无法改方法体: " + name + descriptor);
        }
        InstructionBuilder.applyTo(code, body.toByteArray(), body.resolveExceptionTable());
        dirtyMethods.add(key(name, descriptor));
        return this;
    }

    private MethodInfo requireMethod(String name, String descriptor) {
        MethodInfo method = classFile.findMethod(name, descriptor);
        if (method == null) {
            throw new IllegalStateException("方法不存在: " + name + descriptor);
        }
        return method;
    }

    private static Code codeOf(MethodInfo method) {
        for (AbstractAttribute a : method.getAttributes()) {
            if (a instanceof Code) {
                return (Code) a;
            }
        }
        return null;
    }

    private Code newCodeAttribute(InstructionBuilder body) {
        Code code = new Code(new U2(poolBuilder.utf8("Code")), new U4(0), new U2(0), new U2(0), poolList());
        InstructionBuilder.applyTo(code, body.toByteArray(), body.resolveExceptionTable());
        return code;
    }

    // ---------------------------------------------------------------- 注解

    /**
     * 给类加一条运行时可见注解
     *
     * @param typeDescriptor 注解类型描述符，形如 Lcom/foo/Bar;
     * @param values         元素名到元素值；无元素传 null
     * @return ClassEditor类型返回值
     */
    public ClassEditor addClassAnnotation(String typeDescriptor, Map<String, ?> values) {
        classAnnotationsAttribute().addAnnotation(annotations().annotation(typeDescriptor, values));
        return this;
    }

    /**
     * 删掉类上的某条注解，类上没有该属性或没有这条注解都静默放过
     *
     * @param typeDescriptor 注解类型描述符
     * @return ClassEditor类型返回值
     */
    public ClassEditor removeClassAnnotation(String typeDescriptor) {
        for (AnnotationsAttribute emptied : stripAnnotation(classFile.getAttributes(), typeDescriptor)) {
            classFile.removeAttribute(emptied);
        }
        return this;
    }

    /**
     * 给字段加一条运行时可见注解
     *
     * @param fieldName      字段名
     * @param typeDescriptor 注解类型描述符
     * @param values         元素名到元素值；无元素传 null
     * @return ClassEditor类型返回值
     */
    public ClassEditor addFieldAnnotation(String fieldName, String typeDescriptor, Map<String, ?> values) {
        annotationsAttribute(requireField(fieldName).getAttributes()).addAnnotation(annotations().annotation(typeDescriptor, values));
        return this;
    }

    /**
     * 删掉字段上的某条注解
     *
     * @param fieldName      字段名
     * @param typeDescriptor 注解类型描述符
     * @return ClassEditor类型返回值
     */
    public ClassEditor removeFieldAnnotation(String fieldName, String typeDescriptor) {
        removeMemberAnnotation(requireField(fieldName).getAttributes(), typeDescriptor);
        return this;
    }

    /**
     * 给方法加一条运行时可见注解
     *
     * @param name           方法名
     * @param descriptor     方法描述符
     * @param typeDescriptor 注解类型描述符
     * @param values         元素名到元素值；无元素传 null
     * @return ClassEditor类型返回值
     */
    public ClassEditor addMethodAnnotation(String name, String descriptor, String typeDescriptor,
                                           Map<String, ?> values) {
        annotationsAttribute(requireMethod(name, descriptor).getAttributes()).addAnnotation(annotations().annotation(typeDescriptor, values));
        return this;
    }

    /**
     * 删掉方法上的某条注解
     *
     * @param name           方法名
     * @param descriptor     方法描述符
     * @param typeDescriptor 注解类型描述符
     * @return ClassEditor类型返回值
     */
    public ClassEditor removeMethodAnnotation(String name, String descriptor, String typeDescriptor) {
        removeMemberAnnotation(requireMethod(name, descriptor).getAttributes(), typeDescriptor);
        return this;
    }

    private AnnotationBuilder annotations() {
        return new AnnotationBuilder(poolBuilder);
    }

    /** 取类上的运行时可见注解容器属性，没有就新建一个挂上去 */
    private AnnotationsAttribute classAnnotationsAttribute() {
        AnnotationsAttribute existing = visibleAnnotationsAttribute(classFile.getAttributes());
        if (existing != null) {
            return existing;
        }
        AnnotationsAttribute attr = newAnnotationsAttribute();
        classFile.addAttribute(attr);
        return attr;
    }

    /** 取成员的运行时可见注解容器属性，没有就新建一个挂上去 */
    private AnnotationsAttribute annotationsAttribute(List<AbstractAttribute> attributes) {
        AnnotationsAttribute existing = visibleAnnotationsAttribute(attributes);
        if (existing != null) {
            return existing;
        }
        AnnotationsAttribute attr = newAnnotationsAttribute();
        attributes.add(attr);
        return attr;
    }

    /**
     * 只认运行时可见的那一个
     * <p>
     * 一个类上完全可能同时挂着可见与不可见两个容器（SOURCE 保留的注解就落在后者），
     * 认错了会把新注解塞进不可见容器，反射读不到，而字节本身看着一点毛病都没有。
     */
    private static AnnotationsAttribute visibleAnnotationsAttribute(List<AbstractAttribute> attributes) {
        for (AbstractAttribute a : attributes) {
            if (a instanceof RuntimeVisibleAnnotationsAttribute) {
                return (AnnotationsAttribute) a;
            }
        }
        return null;
    }

    private AnnotationsAttribute newAnnotationsAttribute() {
        return new RuntimeVisibleAnnotationsAttribute(
                new U2(poolBuilder.utf8("RuntimeVisibleAnnotations")), new U4(0));
    }

    /**
     * 按类型描述符摘掉注解
     *
     * @return 因为摘空而该连带摘掉的注解容器属性
     */
    private List<AnnotationsAttribute> stripAnnotation(List<AbstractAttribute> attributes, String typeDescriptor) {
        List<AnnotationsAttribute> emptied = new ArrayList<>();
        int typeIndex = poolBuilder.utf8(typeDescriptor);
        for (AbstractAttribute a : attributes) {
            if (!(a instanceof AnnotationsAttribute)) {
                continue;
            }
            AnnotationsAttribute attr = (AnnotationsAttribute) a;
            if (!attr.getAnnotations().removeIf(x -> x.getTypeIndex().intValue() == typeIndex)) {
                continue;
            }
            if (attr.getAnnotations().isEmpty()) {
                emptied.add(attr);
            } else {
                // 走 setAnnotations 才会把「原样回放原始字节」的开关翻掉，
                // 否则改过的注解列表写出去的还是老字节
                attr.setAnnotations(new ArrayList<>(attr.getAnnotations()));
            }
        }
        return emptied;
    }

    private void removeMemberAnnotation(List<AbstractAttribute> attributes, String typeDescriptor) {
        for (AnnotationsAttribute emptied : stripAnnotation(attributes, typeDescriptor)) {
            attributes.remove(emptied);
        }
    }

    /**
     * 四种没有直接 Java 对应形态的注解元素值构造入口
     */
    public static final class Ann {

        private Ann() {
        }

        /**
         * Class 类型的元素值，descriptor 走返回描述符形制（Ljava/lang/String;、[I、V）
         *
         * @param descriptor 类描述符
         * @return Object类型返回值
         */
        public static Object cls(String descriptor) {
            return new ClassRef(descriptor);
        }

        /**
         * 枚举常量元素值
         *
         * @param typeDescriptor 枚举类型描述符，形如 Lcom/foo/Color;
         * @param constName      枚举常量名
         * @return Object类型返回值
         */
        public static Object enumConst(String typeDescriptor, String constName) {
            return new EnumRef(typeDescriptor, constName);
        }

        /**
         * 嵌套注解元素值
         *
         * @param typeDescriptor 被嵌套注解的类型描述符
         * @param values         嵌套注解的元素
         * @return Object类型返回值
         */
        public static Object nested(String typeDescriptor, Map<String, ?> values) {
            return new AnnRef(typeDescriptor, values == null
                    ? new java.util.LinkedHashMap<String, Object>() : new java.util.LinkedHashMap<>(values));
        }

        /**
         * 数组元素值
         *
         * @param items 各项
         * @return Object类型返回值
         */
        public static Object array(Object... items) {
            return new ArrayRef(java.util.Arrays.asList(items));
        }

        static final class ClassRef {
            final String descriptor;

            ClassRef(String descriptor) {
                this.descriptor = descriptor;
            }
        }

        static final class EnumRef {
            final String typeDescriptor;
            final String constName;

            EnumRef(String typeDescriptor, String constName) {
                this.typeDescriptor = typeDescriptor;
                this.constName = constName;
            }
        }

        static final class AnnRef {
            final String typeDescriptor;
            final Map<String, ?> values;

            AnnRef(String typeDescriptor, Map<String, ?> values) {
                this.typeDescriptor = typeDescriptor;
                this.values = values;
            }
        }

        static final class ArrayRef {
            final List<Object> values;

            ArrayRef(List<Object> values) {
                this.values = values;
            }
        }
    }

    // ---------------------------------------------------------------- 输出

    /**
     * 补齐所有变更的副产物后写出字节
     * <p>
     * 方法体动过的要重算栈帧与 max_stack/max_locals；没动过的新方法也得算一次。
     *
     * @return byte[]类型返回值
     */
    public byte[] toBytes() {
        refreshDirtyMethods();
        return new ClassWriter(classFile, poolBuilder).toByteArray();
    }

    /**
     * 写出到文件
     *
     * @param path 目标路径
     * @throws IOException 写出失败
     */
    public void writeTo(String path) throws IOException {
        java.nio.file.Files.write(java.nio.file.Paths.get(path), toBytes());
    }

    /**
     * 直接在内存里定义并加载改完的类
     *
     * @param companionClasses 伴生类字节（内部类等），键为二进制名
     * @return Class类型返回值
     * @param <T> 目标类型
     */
    public <T> Class<T> defineAndLoad(Map<String, byte[]> companionClasses) {
        String binaryName = binaryName();
        Map<String, byte[]> classes = new java.util.HashMap<>(companionClasses);
        classes.put(binaryName, toBytes());
        try {
            @SuppressWarnings("unchecked")
            Class<T> loaded = (Class<T>) new MapClassLoader(classes).loadClass(binaryName);
            return loaded;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("内存加载改后类失败: " + binaryName, e);
        }
    }

    /**
     * 只加载改后类自身
     *
     * @param <T> 目标类型
     * @return Class类型返回值
     */
    public <T> Class<T> defineAndLoad() {
        return defineAndLoad(new java.util.HashMap<String, byte[]>());
    }

    private void refreshDirtyMethods() {
        if (dirtyMethods.isEmpty()) {
            return;
        }
        for (MethodInfo method : classFile.getMethods()) {
            String id = key(method.getName(), method.getDescriptor());
            if (!dirtyMethods.contains(id)) {
                // 没动过的方法保留原始栈帧字节，重算既无必要也会把老字节码里
                // 本模块算不出来的构造（比如 jsr）抹平
                continue;
            }
            Code code = codeOf(method);
            if (code == null) {
                continue;
            }
            boolean isStatic = (method.getAccessFlags().intValue() & ACC_STATIC) != 0;
            boolean isConstructor = "<init>".equals(method.getName());
            FrameComputer computer = new FrameComputer(code, method.getDescriptor(), isStatic,
                    isConstructor, classFile.getClassIndex().intValue(), poolList(), poolBuilder);
            List<StackMapFrameEntry> frames = computer.run();
            code.getMaxStack().setValue(computer.getMaxStack());
            code.getMaxLocals().setValue(computer.getMaxLocals());
            stackMapTableOf(code).setFrames(frames);
        }
        dirtyMethods.clear();
    }

    /**
     * 取方法体的栈帧表，没有就新建一个挂上去
     */
    private StackMapTableAttribute stackMapTableOf(Code code) {
        for (AbstractAttribute a : code.getAttributes()) {
            if (a instanceof StackMapTableAttribute) {
                return (StackMapTableAttribute) a;
            }
        }
        StackMapTableAttribute table = new StackMapTableAttribute(
                new U2(poolBuilder.utf8("StackMapTable")), new U4(0));
        code.getAttributes().add(table);
        return table;
    }

    /** 新成员要能按名字/描述符反查自己，所以给的是构造器里那份活的池 */
    private List<AbstractConstantPool> poolList() {
        return poolBuilder.build();
    }

    /** 模型里的类名是内部名（斜杠），类加载器要的是二进制名（点） */
    private String binaryName() {
        return classFile.getClassName().replace('/', '.');
    }

    private static String key(String name, String descriptor) {
        return name + descriptor;
    }
}