package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.InputStream;
import java.util.List;

/**
 * 属性工厂类
 * <p>
 * 按属性名路由到具体属性模型。JVMS 要求对不认识的属性按 attribute_length 跳过，
 * 但直接丢弃会让后续属性错位，因此统一落到 {@link UnknownAttribute} 原样保留。
 */
public class AttributeFactory {

    /**
     * 读取一个属性（含属性名索引与长度）
     *
     * @param inputStream 输入流
     * @param poolList    常量池列表，用于解析属性名称
     * @return 属性对象；nameIndex 非法（无对应 Utf8）时返回 null，此时流已按长度跳过
     */
    public static AbstractAttribute getAttributeTable(InputStream inputStream, List<AbstractConstantPool> poolList) {
        ByteReader reader = new ByteReader(inputStream);
        U2 attributeNameIndex = reader.readU2();
        U4 attributeLength = reader.readU4();
        int length = (int) attributeLength.longValue();
        String name = UnknownAttribute.resolveName(attributeNameIndex, poolList);

        AbstractAttribute attr = create(name, attributeNameIndex, attributeLength, poolList);
        if (attr == null) {
            byte[] raw = reader.readFully(length);
            return new UnknownAttribute(attributeNameIndex, attributeLength, name, raw);
        }
        attr.read(inputStream);
        if (attr instanceof PoolResolvable) {
            ((PoolResolvable) attr).resolve(poolList);
        }
        return attr;
    }

    private static AbstractAttribute create(String name, U2 nameIndex, U4 length,
                                             List<AbstractConstantPool> poolList) {
        if (name == null) {
            return null;
        }
        switch (name) {
            case "ConstantValue":
                return new ConstantValue(nameIndex, length);
            case "Code":
                return new Code(nameIndex, length, poolList);
            case "StackMapTable":
                return new StackMapTableAttribute(nameIndex, length);
            case "Exceptions":
                return new ExceptionsAttribute(nameIndex, length);
            case "InnerClasses":
                return new InnerClassesAttribute(nameIndex, length);
            case "EnclosingMethod":
                return new EnclosingMethodAttribute(nameIndex, length);
            case "Synthetic":
                return new SyntheticAttribute(nameIndex, length);
            case "Signature":
                return new SignatureAttribute(nameIndex, length);
            case "SourceFile":
                return new SourceFileAttribute(nameIndex, length);
            case "SourceDebugExtension":
                return new SourceDebugExtensionAttribute(nameIndex, length);
            case "LineNumberTable":
                return new LineNumberTable(nameIndex, length);
            case "LocalVariableTable":
                return new LocalVariableTable(nameIndex, length);
            case "LocalVariableTypeTable":
                return new LocalVariableTypeTableAttribute(nameIndex, length);
            case "Deprecated":
                return new DeprecatedAttribute(nameIndex, length);
            case "RuntimeVisibleAnnotations":
                return new RuntimeVisibleAnnotationsAttribute(nameIndex, length);
            case "RuntimeInvisibleAnnotations":
                return new RuntimeInvisibleAnnotationsAttribute(nameIndex, length);
            case "RuntimeVisibleParameterAnnotations":
                return new RuntimeVisibleParameterAnnotationsAttribute(nameIndex, length);
            case "RuntimeInvisibleParameterAnnotations":
                return new RuntimeInvisibleParameterAnnotationsAttribute(nameIndex, length);
            case "RuntimeVisibleTypeAnnotations":
                return new RuntimeVisibleTypeAnnotationsAttribute(nameIndex, length);
            case "RuntimeInvisibleTypeAnnotations":
                return new RuntimeInvisibleTypeAnnotationsAttribute(nameIndex, length);
            case "AnnotationDefault":
                return new AnnotationDefaultAttribute(nameIndex, length);
            case "BootstrapMethods":
                return new BootstrapMethodsAttribute(nameIndex, length);
            case "MethodParameters":
                return new MethodParametersAttribute(nameIndex, length);
            case "Module":
                return new ModuleAttribute(nameIndex, length);
            case "ModulePackages":
                return new ModulePackagesAttribute(nameIndex, length);
            case "ModuleMainClass":
                return new ModuleMainClassAttribute(nameIndex, length);
            case "NestHost":
                return new NestHostAttribute(nameIndex, length);
            case "NestMembers":
                return new NestMembersAttribute(nameIndex, length);
            case "Record":
                return new RecordAttribute(nameIndex, length, poolList);
            case "PermittedSubclasses":
                return new PermittedSubclassesAttribute(nameIndex, length);
            default:
                return null;
        }
    }
}
