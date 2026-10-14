package com.zifang.util.bc.bytecode.editor;

import com.zifang.util.bc.bytecode.model.annotation.Annotation;
import com.zifang.util.bc.bytecode.model.annotation.ElementValue;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.writer.ConstantPoolBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 把 Java 值编码成注解的 element_value
 * <p>
 * tag 由值的运行时类型决定，装载的常量池下标一律现算现 intern。
 * 除了八种基本类型和 String，还有四种 JDK 编码里没有直接对应、需要显式包装的形态，
 * 由 {@link ClassEditor#Ann} 提供构造入口。
 */
final class AnnotationBuilder {

    private final ConstantPoolBuilder pool;

    AnnotationBuilder(ConstantPoolBuilder pool) {
        this.pool = pool;
    }

    /**
     * 造一条注解
     *
     * @param typeDescriptor 注解类型描述符，形如 Lcom/foo/Bar;
     * @param values        元素名到元素值的映射，null 或空表示无元素
     * @return Annotation类型返回值
     */
    Annotation annotation(String typeDescriptor, Map<String, ?> values) {
        Annotation a = new Annotation(new U2(pool.utf8(typeDescriptor)));
        if (values == null) {
            return a;
        }
        for (Map.Entry<String, ?> e : values.entrySet()) {
            a.getPairs().add(new Annotation.ElementNameValue(
                    new U2(pool.utf8(e.getKey())), elementValue(e.getValue())));
        }
        return a;
    }

    private ElementValue elementValue(Object value) {
        if (value instanceof Boolean) {
            return ElementValue.constant('Z', pool.integer((Boolean) value ? 1 : 0));
        }
        if (value instanceof Byte) {
            return ElementValue.constant('B', pool.integer((Byte) value));
        }
        if (value instanceof Character) {
            return ElementValue.constant('C', pool.integer((Character) value));
        }
        if (value instanceof Short) {
            return ElementValue.constant('S', pool.integer((Short) value));
        }
        if (value instanceof Integer) {
            return ElementValue.constant('I', pool.integer((Integer) value));
        }
        if (value instanceof Long) {
            return ElementValue.constant('J', pool.longConstant((Long) value));
        }
        if (value instanceof Float) {
            return ElementValue.constant('F', pool.floatConstant(Float.floatToRawIntBits((Float) value)));
        }
        if (value instanceof Double) {
            return ElementValue.constant('D', pool.doubleConstant(Double.doubleToRawLongBits((Double) value)));
        }
        if (value instanceof String) {
            return ElementValue.constant('s', pool.utf8((String) value));
        }
        if (value instanceof ClassEditor.Ann.ClassRef) {
            return ElementValue.constant('c', pool.utf8(((ClassEditor.Ann.ClassRef) value).descriptor));
        }
        if (value instanceof ClassEditor.Ann.EnumRef) {
            ClassEditor.Ann.EnumRef e = (ClassEditor.Ann.EnumRef) value;
            return ElementValue.enumConstant(pool.utf8(e.typeDescriptor), pool.utf8(e.constName));
        }
        if (value instanceof ClassEditor.Ann.AnnRef) {
            ClassEditor.Ann.AnnRef r = (ClassEditor.Ann.AnnRef) value;
            return ElementValue.annotation(annotation(r.typeDescriptor, r.values));
        }
        if (value instanceof ClassEditor.Ann.ArrayRef) {
            List<ElementValue> items = new ArrayList<>();
            for (Object item : ((ClassEditor.Ann.ArrayRef) value).values) {
                items.add(elementValue(item));
            }
            return ElementValue.array(items);
        }
        throw new IllegalArgumentException("无法编码成注解元素值的类型: "
                + (value == null ? "null" : value.getClass().getName()));
    }
}
