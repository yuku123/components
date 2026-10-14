package com.zifang.util.bc.bytecode;

import com.zifang.util.bc.bytecode.model.ClassFile;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.resolver.ByteCodeResolver;
import com.zifang.util.bc.bytecode.writer.ClassWriter;
import com.zifang.util.bc.compile.MapClassLoader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 写出层的往返测试：parse → write → defineClass → 真正执行
 * <p>
 * 只断言字节相等没有意义——写错一位也可能碰巧自洽。这里把写出的字节喂回 JVM
 * 并真的调一次方法，让校验器替我们把关；同时再 parse 一遍确认结构没在往返中丢失。
 */
class ClassWriterRoundTripTest {

    private static final String SOURCE = String.join("\n",
            "package fixture;",
            "public class Greeter {",
            "    private static final String PREFIX = \"hello, \";",
            "    private final String name;",
            "    private int counter;",
            "    public Greeter(String name) { this.name = name; }",
            "    public String greet() { counter++; return PREFIX + name; }",
            "    public int fib(int n) {",
            "        if (n < 2) return n;",
            "        return fib(n - 1) + fib(n - 2);",
            "    }",
            "    public int sum(int[] xs) {",
            "        int s = 0;",
            "        for (int x : xs) { s += x; }",
            "        return s;",
            "    }",
            "    public String describe(int k) {",
            "        switch (k) {",
            "            case 0: return \"zero\";",
            "            case 1: return \"one\";",
            "            case 7: return \"seven\";",
            "            default: return \"many\";",
            "        }",
            "    }",
            "    public String guard(int k) {",
            "        try {",
            "            if (k < 0) throw new IllegalArgumentException(\"neg: \" + k);",
            "            return \"ok\";",
            "        } catch (IllegalArgumentException e) {",
            "            return \"caught \" + e.getMessage();",
            "        } finally {",
            "            counter += 10;",
            "        }",
            "    }",
            "    public static String concat(String a, String b) { return a + \"-\" + b; }",
            "}");

    private static ClassFile parseOriginal() {
        byte[] original = ModernJavaFeatureFixtures.compileOne("fixture.Greeter", SOURCE);
        return ByteCodeResolver.parseFromStream(new ByteArrayInputStream(original));
    }

    @Test
    void writtenBytesAreByteIdenticalToTheOriginal() {
        byte[] original = ModernJavaFeatureFixtures.compileOne("fixture.Greeter", SOURCE);
        ClassFile parsed = ByteCodeResolver.parseFromStream(new ByteArrayInputStream(original));
        byte[] written = new ClassWriter(parsed).toByteArray();
        assertArrayEquals(original, written,
                "解析后再写出的字节应与原文件逐位一致（没有做任何变更时）");
    }

    @Test
    void reparsedOutputKeepsEveryStructure() {
        ClassFile first = parseOriginal();
        byte[] written = new ClassWriter(first).toByteArray();
        ClassFile second = ByteCodeResolver.parseFromStream(new ByteArrayInputStream(written));

        assertEquals(first.getClassName(), second.getClassName());
        assertEquals("fixture/Greeter", second.getClassName());
        assertEquals(first.getMajorVersion().intValue(), second.getMajorVersion().intValue());
        assertEquals(first.getPoolInfo().getPoolList().size(), second.getPoolInfo().getPoolList().size());

        assertEquals(first.getFields().size(), second.getFields().size());
        assertEquals(first.getMethods().size(), second.getMethods().size());
        assertNotNull(second.findMethod("greet", "()Ljava/lang/String;"));
        assertNotNull(second.findMethod("fib", "(I)I"));
        assertNotNull(second.findMethod("sum", "([I)I"));
        assertNotNull(second.findMethod("describe", "(I)Ljava/lang/String;"));
        assertNotNull(second.findMethod("guard", "(I)Ljava/lang/String;"));
        assertNotNull(second.findMethod("concat", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"));

        assertEquals("name", second.findField("name").getName());
        assertEquals("I", second.findField("counter").getDescriptor());
        assertNotNull(second.findAttribute("SourceFile"), "SourceFile 属性应在往返后保留");
    }

    @Test
    void writtenClassDefinesAndRunsInTheJvm() throws Exception {
        ClassFile parsed = parseOriginal();
        byte[] written = new ClassWriter(parsed).toByteArray();
        Map<String, byte[]> classes = new HashMap<>();
        classes.put("fixture.Greeter", written);
        Class<?> reloaded = new MapClassLoader(classes).loadClass("fixture.Greeter");

        Object greeter = reloaded.getConstructor(String.class).newInstance("zifang");
        assertEquals("hello, zifang", reloaded.getMethod("greet").invoke(greeter));

        Object fib = reloaded.getMethod("fib", int.class).invoke(greeter, 12);
        assertEquals(144, fib);

        Object sum = reloaded.getMethod("sum", int[].class).invoke(greeter, (Object) new int[]{1, 2, 3, 4, 5});
        assertEquals(15, sum);

        Object described = reloaded.getMethod("describe", int.class).invoke(greeter, 7);
        assertEquals("seven", described);
        assertEquals("many", reloaded.getMethod("describe", int.class).invoke(greeter, 99));

        assertEquals("caught neg: -3", reloaded.getMethod("guard", int.class).invoke(greeter, -3));
        assertEquals("ok", reloaded.getMethod("guard", int.class).invoke(greeter, 5));
        // finally 块在两条路径上都跑过一次，说明异常表的处理器范围没在往返中丢失
        Field counter = reloaded.getDeclaredField("counter");
        counter.setAccessible(true);
        // greet() 记 1，两次 guard 的 finally 各加 10
        assertEquals(21, counter.get(greeter));

        assertEquals("a-b", reloaded
                .getMethod("concat", String.class, String.class).invoke(null, "a", "b"));
    }

    @Test
    void writesToDiskAndReloadsThroughTheSystemLoader() throws Exception {
        ClassFile parsed = parseOriginal();
        Path dir = Files.createTempDirectory("zutil-bc-writer");
        try {
            Path target = dir.resolve("Greeter.class");
            new ClassWriter(parsed).writeTo(target.toString());
            assertTrue(Files.size(target) > 0);

            Map<String, byte[]> classes = new HashMap<>();
            classes.put("fixture.Greeter", Files.readAllBytes(target));
            Class<?> reloaded = new MapClassLoader(classes).loadClass("fixture.Greeter");
            assertEquals("hello, world",
                    reloaded.getMethod("greet").invoke(reloaded.getConstructor(String.class).newInstance("world")));
        } finally {
            Files.deleteIfExists(dir.resolve("Greeter.class"));
            Files.deleteIfExists(dir);
        }
    }

    @Test
    void codeBytesRoundTripThroughTheStaticHelper() throws Exception {
        ClassFile parsed = parseOriginal();
        Code greet = (Code) parsed.findMethod("greet", "()Ljava/lang/String;").getAttributes().get(0);
        byte[] before = ClassWriter.codeBytes(greet);
        assertTrue(before.length > 0);
        ClassWriter.setCodeBytes(greet, before);
        assertArrayEquals(before, ClassWriter.codeBytes(greet));

        byte[] written = new ClassWriter(parsed).toByteArray();
        Map<String, byte[]> classes = Collections.singletonMap("fixture.Greeter", written);
        Class<?> reloaded = new MapClassLoader(classes).loadClass("fixture.Greeter");
        assertEquals("hello, kiro",
                reloaded.getMethod("greet").invoke(reloaded.getConstructor(String.class).newInstance("kiro")));
    }

    @Test
    void anEmptyClassFileStillWrites() throws Exception {
        byte[] original = ModernJavaFeatureFixtures.compileOne("fixture.Marker", String.join("\n",
                "package fixture;",
                "public interface Marker {}"));
        ClassFile parsed = ByteCodeResolver.parseFromStream(new ByteArrayInputStream(original));
        byte[] written = new ClassWriter(parsed).toByteArray();
        assertArrayEquals(original, written);

        Path dir = Files.createTempDirectory("zutil-bc-writer");
        try {
            Path target = dir.resolve("Marker.class");
            Files.write(target, written);
            assertEquals(written.length, Files.size(target));
            assertTrue(Paths.get(target.toString()).toAbsolutePath().endsWith("Marker.class"));
        } finally {
            Files.deleteIfExists(dir.resolve("Marker.class"));
            Files.deleteIfExists(dir);
        }
    }
}