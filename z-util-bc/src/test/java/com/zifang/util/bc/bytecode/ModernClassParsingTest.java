package com.zifang.util.bc.bytecode;

import com.zifang.util.bc.bytecode.model.ClassFile;
import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.attribute.AttributeNames;
import com.zifang.util.bc.bytecode.model.attribute.MethodParametersAttribute;
import com.zifang.util.bc.bytecode.model.attribute.PermittedSubclassesAttribute;
import com.zifang.util.bc.bytecode.model.attribute.RecordAttribute;
import com.zifang.util.bc.bytecode.model.attribute.RuntimeVisibleAnnotationsAttribute;
import com.zifang.util.bc.bytecode.resolver.ByteCodeResolver;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 现代 class 解析
 * <p>
 * 每条用例都用 javac 现编一份含该特性的类，再解析回来。这些都是旧解析器会当场炸的输入：
 * 缺 CONSTANT_Dynamic/Module/Package 标签与 Record/PermittedSubclasses 属性时，
 * 解析会抛「未知的常量池类型」或把属性体错位。
 */
class ModernClassParsingTest {

    @Test
    void parsesRecordWithComponentAnnotations() {
        String source = String.join("\n",
                "package modern;",
                "import java.lang.annotation.*;",
                "@Retention(RetentionPolicy.RUNTIME)",
                "@Target(ElementType.RECORD_COMPONENT)",
                "@interface Label { String value(); }",
                "public record RecordHolder(@Label(\"first\") String name, int count) {",
                "    public String shout() { return name.toUpperCase() + count; }",
                "}");

        ClassFile cf = parse(ModernJavaFeatureFixtures.compileOne("modern.RecordHolder", source));

        RecordAttribute record = assertInstanceOf(RecordAttribute.class, cf.findAttribute("Record"));
        assertEquals(2, record.getComponents().size());
        assertNotNull(cf.findMethod("shout", "()Ljava/lang/String;"));
        // record 组件上的注解挂在 Record 属性的组件属性里
        assertTrue(record.getComponents().get(0).getAttributes().stream()
                .anyMatch(a -> "RuntimeVisibleAnnotations".equals(AttributeNames.nameOf(a))));

        // 组件注解本身可数出来
        RuntimeVisibleAnnotationsAttribute componentAnnotations = assertInstanceOf(
                RuntimeVisibleAnnotationsAttribute.class,
                record.getComponents().get(0).getAttributes().get(0));
        assertEquals(1, componentAnnotations.getNumAnnotations());
    }

    @Test
    void parsesSealedClassPermittedSubclasses() {
        String source = String.join("\n",
                "package modern;",
                "sealed interface Sealed permits Impl { }",
                "final class Impl implements Sealed { }");

        Map<String, byte[]> compiled = ModernJavaFeatureFixtures.compile("modern.Sealed", source);
        ClassFile cf = parse(compiled.get("modern.Sealed"));

        PermittedSubclassesAttribute permitted = assertInstanceOf(
                PermittedSubclassesAttribute.class, cf.findAttribute("PermittedSubclasses"));
        assertEquals(1, permitted.getSubclassIndices().size());
        assertEquals("modern/Impl",
                new ConstantPoolHelper(cf.getPoolInfo().getPoolList())
                        .classInternalName(permitted.getSubclassIndices().get(0).intValue()));
    }

    @Test
    void parsesMethodParameters() {
        String source = String.join("\n",
                "package modern;",
                "public class WithParams {",
                "    public void run(int a, String b) { System.out.println(a + b); }",
                "}");

        ClassFile cf = parse(ModernJavaFeatureFixtures.compileWithOptions(
                "modern.WithParams", source, "-parameters"));

        MethodParametersAttribute params = assertInstanceOf(MethodParametersAttribute.class,
                cf.findMethod("run").getAttributes().stream()
                        .filter(a -> a instanceof MethodParametersAttribute)
                        .findFirst()
                        .orElse(null));
        assertEquals(2, params.getParameters().size());
    }

    @Test
    void parsesLambdaCallSiteWithoutThrowing() {
        String source = String.join("\n",
                "package modern;",
                "import java.util.function.Supplier;",
                "public class WithLambda {",
                "    public Supplier<String> supplier() { return () -> \"hi\"; }",
                "}");

        ClassFile cf = parse(ModernJavaFeatureFixtures.compileOne("modern.WithLambda", source));

        assertEquals("modern/WithLambda", cf.getClassName());
        assertNotNull(cf.findAttribute("BootstrapMethods"));
    }

    private static ClassFile parse(byte[] bytes) {
        return ByteCodeResolver.parseFromStream(new ByteArrayInputStream(bytes));
    }
}
