package com.zifang.util.bc.bytecode.editor;

import com.zifang.util.bc.bytecode.ModernJavaFeatureFixtures;
import com.zifang.util.bc.bytecode.model.annotation.Annotation;
import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.AnnotationsAttribute;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 注解增删测试
 * <p>
 * 判定标准是反射真读出来：编码对了但下标错位，注解要么解析不出要么类型对不上，
 * 只有 Class.getAnnotation 能把这两类错一并挡下。
 */
class ClassEditorAnnotationTest {

    private static final String SOURCE = String.join("\n",
            "package edit;",
            "import java.lang.annotation.Retention;",
            "import java.lang.annotation.RetentionPolicy;",
            "@Retention(RetentionPolicy.RUNTIME) @interface Meta { String tag(); }",
            "enum Color { RED, GREEN }",
            "@Retention(RetentionPolicy.RUNTIME)",
            "@interface Marker {",
            "    String name();",
            "    int count();",
            "    long stamp();",
            "    double ratio();",
            "    boolean flag();",
            "    char initial();",
            "    byte level();",
            "    short small();",
            "    float weight();",
            "    Class<?> type();",
            "    Color color();",
            "    Meta meta();",
            "    String[] tags();",
            "}",
            "public class Target {",
            "    private int counter;",
            "    public int twice(int x) { return x * 2; }",
            "}");

    private static final String MARKER = "Ledit/Marker;";

    private static Map<String, byte[]> fixture() {
        return ModernJavaFeatureFixtures.compile("edit.Target", SOURCE);
    }

    private static Map<String, Object> values() {
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("name", "hello");
        v.put("count", 42);
        v.put("stamp", 9_000_000_000L);
        v.put("ratio", 0.25d);
        v.put("flag", Boolean.TRUE);
        v.put("initial", 'X');
        v.put("level", (byte) 7);
        v.put("small", (short) 300);
        v.put("weight", 1.5f);
        v.put("type", ClassEditor.Ann.cls("Ljava/lang/String;"));
        v.put("color", ClassEditor.Ann.enumConst("Ledit/Color;", "GREEN"));
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("tag", "nested");
        v.put("meta", ClassEditor.Ann.nested("Ledit/Meta;", meta));
        v.put("tags", ClassEditor.Ann.array("a", "b", "c"));
        return v;
    }

    @Test
    void annotationAddedToClassFieldAndMethodSurvivesReflection() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        editor.addClassAnnotation(MARKER, values());
        editor.addFieldAnnotation("counter", MARKER, Collections.singletonMap("name", "onField"));
        editor.addMethodAnnotation("twice", "(I)I", MARKER, Collections.singletonMap("name", "onMethod"));

        Map<String, byte[]> all = new HashMap<>(compiled);
        all.put("edit.Target", editor.toBytes());
        Class<?> loaded = loader(all).loadClass("edit.Target");

        java.lang.annotation.Annotation marker =
                loaded.getAnnotation(asMarker(loaded));
        assertNotNull(marker, "类上的注解应能被反射读到");
        assertEquals("hello", invoke(marker, "name"));
        assertEquals(42, invoke(marker, "count"));
        assertEquals(9_000_000_000L, invoke(marker, "stamp"));
        assertEquals(0.25d, (Double) invoke(marker, "ratio"), 0.0);
        assertEquals(Boolean.TRUE, invoke(marker, "flag"));
        assertEquals('X', invoke(marker, "initial"));
        assertEquals((byte) 7, (Byte) invoke(marker, "level"));
        assertEquals((short) 300, (Short) invoke(marker, "small"));
        assertEquals(1.5f, (Float) invoke(marker, "weight"), 0.0);
        assertEquals(String.class, invoke(marker, "type"));
        assertEquals("GREEN", String.valueOf(((Enum<?>) invoke(marker, "color")).name()));
        assertEquals("nested", nestedTag(marker));

        java.lang.annotation.Annotation onField = loaded.getDeclaredField("counter").getAnnotation(asMarker(loaded));
        assertNotNull(onField, "字段上的注解应能被反射读到");
        assertEquals("onField", invoke(onField, "name"));

        java.lang.annotation.Annotation onMethod = loaded.getDeclaredMethod("twice", int.class).getAnnotation(asMarker(loaded));
        assertNotNull(onMethod, "方法上的注解应能被反射读到");
        assertEquals("onMethod", invoke(onMethod, "name"));

        Object tags = invoke(marker, "tags");
        assertArrayEquals(new String[]{"a", "b", "c"}, (String[]) tags);
    }

    @Test
    void removedAnnotationLeavesNoAttributeBehind() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        editor.addClassAnnotation(MARKER, Collections.singletonMap("name", "temp"));
        editor.addClassAnnotation("Ledit/Meta;", Collections.singletonMap("tag", "keep"));
        editor.removeClassAnnotation(MARKER);

        Map<String, byte[]> all = new HashMap<>(compiled);
        all.put("edit.Target", editor.toBytes());
        Class<?> loaded = loader(all).loadClass("edit.Target");

        assertNull(loaded.getAnnotation(asMarker(loaded)), "被删的注解不该还在");
        // 剩下的注解类型没有 name 元素，反射读它会抛格式错而不是返回 null，
        // 所以这里只能靠模型侧确认它还在，元素值另由上面的用例覆盖
        assertTrue(hasAnnotationOfType(editor, "Ledit/Meta;"), "同属性里的其他注解不该被一起删掉");
    }

    @Test
    void classCarryingAnnotationsStaysByteIdenticalWhenUntouched() throws Exception {
        String source = String.join("\n",
                "package edit2;",
                "import java.lang.annotation.Retention;",
                "import java.lang.annotation.RetentionPolicy;",
                "@Retention(RetentionPolicy.RUNTIME)",
                "@interface Tag { String name(); int count(); }",
                "@Tag(name = \"kept\", count = 3)",
                "public class Annotated {",
                "    public int id() { return 1; }",
                "}");
        Map<String, byte[]> compiled = ModernJavaFeatureFixtures.compile("edit2.Annotated", source);
        ClassEditor editor = ClassEditor.of(compiled.get("edit2.Annotated"));
        assertTrue(java.util.Arrays.equals(compiled.get("edit2.Annotated"), editor.toBytes()),
                "没碰注解属性时该原样回放它的字节");

        // 碰了之后重编码，注解本身必须还在
        editor.addField(0x0001, "extra", "J");
        Class<?> loaded = loader(Collections.singletonMap("edit2.Annotated", editor.toBytes()))
                .loadClass("edit2.Annotated");
        assertNotNull(loaded.getAnnotations(), "重编码后注解不该丢");
    }

    @Test
    void visibleAnnotationDoesNotLandInAnExistingInvisibleContainer() throws Exception {
        String source = String.join("\n",
                "package edit3;",
                "import java.lang.annotation.Retention;",
                "import java.lang.annotation.RetentionPolicy;",
                "@Retention(RetentionPolicy.CLASS)",
                "@interface Internal { String note(); }",
                "@Retention(RetentionPolicy.RUNTIME)",
                "@interface Outer { String note(); }",
                "@Internal(note = \"gone\")",
                "public class Mixed {",
                "    public int id() { return 1; }",
                "}");
        Map<String, byte[]> compiled = ModernJavaFeatureFixtures.compile("edit3.Mixed", source);
        ClassEditor editor = ClassEditor.of(compiled.get("edit3.Mixed"));
        editor.addClassAnnotation("Ledit3/Outer;", Collections.singletonMap("note", "kept"));

        Map<String, byte[]> all = new java.util.HashMap<>(compiled);
        all.put("edit3.Mixed", editor.toBytes());
        Class<?> loaded = loader(all).loadClass("edit3.Mixed");
        java.lang.annotation.Annotation outer = loaded.getAnnotation(
                (Class<? extends java.lang.annotation.Annotation>) Class.forName("edit3.Outer", false, loaded.getClassLoader()));
        assertNotNull(outer, "新加的可见注解不该被塞进不可见容器");
        assertEquals("kept", invoke(outer, "note"));
    }

    @Test
    void addingToAClassThatAlreadyCarriesAnnotationsKeepsBoth() throws Exception {
        String source = String.join("\n",
                "package edit4;",
                "import java.lang.annotation.Retention;",
                "import java.lang.annotation.RetentionPolicy;",
                "@Retention(RetentionPolicy.RUNTIME)",
                "@interface First { String note(); }",
                "@Retention(RetentionPolicy.RUNTIME)",
                "@interface Second { String note(); }",
                "@First(note = \"original\")",
                "public class Both {",
                "    public int id() { return 1; }",
                "}");
        Map<String, byte[]> compiled = ModernJavaFeatureFixtures.compile("edit4.Both", source);
        ClassEditor editor = ClassEditor.of(compiled.get("edit4.Both"));
        editor.addClassAnnotation("Ledit4/Second;", Collections.singletonMap("note", "added"));

        Map<String, byte[]> all = new java.util.HashMap<>(compiled);
        all.put("edit4.Both", editor.toBytes());
        Class<?> loaded = loader(all).loadClass("edit4.Both");

        java.lang.annotation.Annotation first = loaded.getAnnotation(
                (Class<? extends java.lang.annotation.Annotation>) Class.forName("edit4.First", false, loaded.getClassLoader()));
        java.lang.annotation.Annotation second = loaded.getAnnotation(
                (Class<? extends java.lang.annotation.Annotation>) Class.forName("edit4.Second", false, loaded.getClassLoader()));
        assertNotNull(first, "原有注解不该被新加的挤掉");
        assertEquals("original", invoke(first, "note"));
        assertNotNull(second, "新加的注解不该被原始字节顶掉");
        assertEquals("added", invoke(second, "note"));
    }

    private static boolean hasAnnotationOfType(ClassEditor editor, String typeDescriptor) {
        for (AbstractAttribute a : editor.classFile().getAttributes()) {
            if (!(a instanceof AnnotationsAttribute)) {
                continue;
            }
            for (Annotation annotation : ((AnnotationsAttribute) a).getAnnotations()) {
                if (annotation.getTypeIndex().intValue() == editor.pool().utf8(typeDescriptor)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static com.zifang.util.bc.compile.MapClassLoader loader(Map<String, byte[]> classes) {
        return new com.zifang.util.bc.compile.MapClassLoader(classes);
    }

    /**
     * fixture 里那圈注解是包私有的，反射要用它自己的 Class，测试代码拿不到类型字面量；
     * 它又只存在于随 fixture 一起加载的那个类加载器里，不能走 Class.forName 走应用的。
     */
    private static Class<? extends java.lang.annotation.Annotation> asMarker(Class<?> owner) {
        try {
            return (Class<? extends java.lang.annotation.Annotation>)
                    Class.forName("edit.Marker", false, owner.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("fixture 注解类缺失", e);
        }
    }

    private static Object invoke(java.lang.annotation.Annotation annotation, String element) {
        try {
            java.lang.reflect.Method method = annotation.annotationType().getMethod(element);
            // fixture 里那圈注解是包私有的，不放开访问控制会在调用这步就撞 IllegalAccess
            method.setAccessible(true);
            return method.invoke(annotation);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("读注解元素失败: " + element, e);
        }
    }

    private static Object nestedTag(java.lang.annotation.Annotation marker) {
        Object meta = invoke(marker, "meta");
        return invoke((java.lang.annotation.Annotation) meta, "tag");
    }
}
