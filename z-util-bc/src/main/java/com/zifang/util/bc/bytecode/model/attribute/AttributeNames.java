package com.zifang.util.bc.bytecode.model.attribute;

import java.util.List;

/**
 * 属性表按名检索
 * <p>
 * 属性实例本身不持有属性名（名字以常量池下标形式存在，读完之后就没用了），
 * 因此按名查找只能靠具体类型反推，这里集中维护这张类型 ↔ 名字的对应表。
 */
public final class AttributeNames {

    private AttributeNames() {
    }

    /**
     * 按属性名在属性表中查找
     *
     * @param attributes    属性表
     * @param attributeName 属性名
     * @return AbstractAttribute类型返回值
     */
    public static AbstractAttribute find(List<AbstractAttribute> attributes, String attributeName) {
        if (attributes == null || attributeName == null) {
            return null;
        }
        for (AbstractAttribute a : attributes) {
            if (attributeName.equals(nameOf(a))) {
                return a;
            }
        }
        return null;
    }

    /**
     * 反推属性名；未登记的类型返回 null
     *
     * @param attribute 属性实例
     * @return String类型返回值
     */
    public static String nameOf(AbstractAttribute attribute) {
        if (attribute instanceof Code) {
            return "Code";
        }
        if (attribute instanceof ConstantValue) {
            return "ConstantValue";
        }
        if (attribute instanceof StackMapTableAttribute) {
            return "StackMapTable";
        }
        if (attribute instanceof ExceptionsAttribute) {
            return "Exceptions";
        }
        if (attribute instanceof InnerClassesAttribute) {
            return "InnerClasses";
        }
        if (attribute instanceof EnclosingMethodAttribute) {
            return "EnclosingMethod";
        }
        if (attribute instanceof SyntheticAttribute) {
            return "Synthetic";
        }
        if (attribute instanceof SignatureAttribute) {
            return "Signature";
        }
        if (attribute instanceof SourceFileAttribute) {
            return "SourceFile";
        }
        if (attribute instanceof SourceDebugExtensionAttribute) {
            return "SourceDebugExtension";
        }
        if (attribute instanceof LineNumberTable) {
            return "LineNumberTable";
        }
        if (attribute instanceof LocalVariableTable) {
            return "LocalVariableTable";
        }
        if (attribute instanceof LocalVariableTypeTableAttribute) {
            return "LocalVariableTypeTable";
        }
        if (attribute instanceof DeprecatedAttribute) {
            return "Deprecated";
        }
        if (attribute instanceof RuntimeVisibleAnnotationsAttribute) {
            return "RuntimeVisibleAnnotations";
        }
        if (attribute instanceof RuntimeInvisibleAnnotationsAttribute) {
            return "RuntimeInvisibleAnnotations";
        }
        if (attribute instanceof RuntimeVisibleParameterAnnotationsAttribute) {
            return "RuntimeVisibleParameterAnnotations";
        }
        if (attribute instanceof RuntimeInvisibleParameterAnnotationsAttribute) {
            return "RuntimeInvisibleParameterAnnotations";
        }
        if (attribute instanceof RuntimeVisibleTypeAnnotationsAttribute) {
            return "RuntimeVisibleTypeAnnotations";
        }
        if (attribute instanceof RuntimeInvisibleTypeAnnotationsAttribute) {
            return "RuntimeInvisibleTypeAnnotations";
        }
        if (attribute instanceof AnnotationDefaultAttribute) {
            return "AnnotationDefault";
        }
        if (attribute instanceof BootstrapMethodsAttribute) {
            return "BootstrapMethods";
        }
        if (attribute instanceof MethodParametersAttribute) {
            return "MethodParameters";
        }
        if (attribute instanceof ModuleAttribute) {
            return "Module";
        }
        if (attribute instanceof ModulePackagesAttribute) {
            return "ModulePackages";
        }
        if (attribute instanceof ModuleMainClassAttribute) {
            return "ModuleMainClass";
        }
        if (attribute instanceof NestHostAttribute) {
            return "NestHost";
        }
        if (attribute instanceof NestMembersAttribute) {
            return "NestMembers";
        }
        if (attribute instanceof RecordAttribute) {
            return "Record";
        }
        if (attribute instanceof PermittedSubclassesAttribute) {
            return "PermittedSubclasses";
        }
        if (attribute instanceof UnknownAttribute) {
            return ((UnknownAttribute) attribute).getAttributeName();
        }
        return null;
    }
}
