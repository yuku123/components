package com.zifang.util.bc.bytecode.verify;

import com.zifang.util.bc.bytecode.model.ClassFile;
import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.attribute.StackMapTableAttribute;
import com.zifang.util.bc.bytecode.model.method.MethodInfo;
import com.zifang.util.bc.bytecode.resolver.ByteCodeResolver;
import com.zifang.util.bc.bytecode.writer.ClassWriter;
import com.zifang.util.bc.bytecode.writer.ConstantPoolBuilder;
import com.zifang.util.bc.compile.MapClassLoader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 帧计算测试
 * <p>
 * 判定标准只有一条：把重算出来的帧写回去，JVM 自己肯不肯认。
 * 断言帧的具体形状只能说明「我们写了自己期望的东西」，
 * 只有 defineClass 成功并跑出正确结果，才说明帧真的对。
 */
class FrameComputerTest {

    private static final int ACC_STATIC = 0x0008;

    /** 把分支、循环、switch、try-catch-finally、long/double、泛型数组都塞进去 */
    private static final String SOURCE = String.join("\n",
            "package verify;",
            "public class Frames {",
            "    private long total;",
            "    private double ratio;",
            "    private String label = \"x\";",
            "    public long branch(int n) {",
            "        long acc = 0L;",
            "        if (n > 0) { acc = n; } else if (n == 0) { acc = -1L; } else { acc = total; }",
            "        for (int i = 0; i < n; i++) { acc += i; }",
            "        while (acc > 100L) { acc /= 2L; }",
            "        do { acc -= 1L; } while (acc > 50L);",
            "        total = acc;",
            "        return acc;",
            "    }",
            "    public String pick(int k) {",
            "        switch (k) {",
            "            case 1: return \"a\";",
            "            case 7: return \"b\";",
            "            case 99: return \"c\";",
            "            default: return \"d\";",
            "        }",
            "    }",
            "    public String sparse(int k) {",
            "        switch (k) {",
            "            case -1000: return \"neg\";",
            "            case 0: return \"zero\";",
            "            case 100000: return \"big\";",
            "            default: return \"other\";",
            "        }",
            "    }",
            "    public String guarded(int k) {",
            "        try {",
            "            if (k < 0) throw new IllegalStateException(\"neg\" + k);",
            "            return \"ok\";",
            "        } catch (NumberFormatException e) {",
            "            return \"nfe\";",
            "        } catch (IllegalStateException e) {",
            "            return \"ise:\" + e.getMessage();",
            "        } finally {",
            "            total += 1L;",
            "        }",
            "    }",
            "    public long[] widen(int n) {",
            "        long[] a = new long[n];",
            "        for (int i = 0; i < n; i++) { a[i] = i; }",
            "        return a;",
            "    }",
            "    public Object[][] nest(int n) {",
            "        Object[][] grid = new Object[n][n];",
            "        return grid;",
            "    }",
            "    public String chain(java.util.List<String> xs) {",
            "        java.util.Iterator<String> it = xs.iterator();",
            "        StringBuilder sb = new StringBuilder();",
            "        while (it.hasNext()) { sb.append(it.next()); }",
            "        return sb.toString();",
            "    }",
            "    public double mix(int a, double b, float c, long d) {",
            "        ratio = b * c + a / (double) d;",
            "        return ratio;",
            "    }",
            "    public static class Inner {",
            "        int value;",
            "        Inner(int v) { this.value = v; }",
            "        int twice() { return value * 2; }",
            "    }",
            "    public int useInner(int v) { return new Inner(v).twice(); }",
            "}");

    private static Map<String, byte[]> fixture() {
        return com.zifang.util.bc.bytecode.ModernJavaFeatureFixtures.compile("verify.Frames", SOURCE);
    }

    private static ClassFile parse() {
        return ByteCodeResolver.parseFromStream(new ByteArrayInputStream(fixture().get("verify.Frames")));
    }

    private static Code codeOf(ClassFile classFile, String name, String descriptor) {
        MethodInfo method = classFile.findMethod(name, descriptor);
        assertNotNull(method, "找不到方法 " + name + descriptor);
        for (AbstractAttribute a : method.getAttributes()) {
            if (a instanceof Code) {
                return (Code) a;
            }
        }
        throw new AssertionError("方法 " + name + " 没有 Code 属性");
    }

    /**
     * 把每个方法的帧全部重算并写回，然后交给 JVM 加载并实际调用
     */
    @Test
    void recomputedFramesAreAcceptedByTheJvm() throws Exception {
        ClassFile classFile = parse();
        ConstantPoolBuilder poolBuilder =
                new ConstantPoolBuilder().seed(classFile.getPoolInfo().getPoolList(),
                        new com.zifang.util.bc.bytecode.model.ConstantPoolHelper(
                                classFile.getPoolInfo().getPoolList()));
        int poolNameUtf8 = poolBuilder.utf8("StackMapTable");

        int recomputed = 0;
        for (MethodInfo method : classFile.getMethods()) {
            Code code = null;
            for (AbstractAttribute a : method.getAttributes()) {
                if (a instanceof Code) {
                    code = (Code) a;
                }
            }
            if (code == null) {
                continue;
            }
            // StackMapTable 是 Code 的属性，不是方法表的属性
            StackMapTableAttribute table = null;
            for (AbstractAttribute a : code.getAttributes()) {
                if (a instanceof StackMapTableAttribute) {
                    table = (StackMapTableAttribute) a;
                }
            }
            if (table == null) {
                table = new StackMapTableAttribute(new com.zifang.util.bc.bytecode.model.readtype.U2(poolNameUtf8),
                        new com.zifang.util.bc.bytecode.model.readtype.U4(0));
                code.getAttributes().add(table);
            }
            boolean isStatic = (method.getAccessFlags().intValue() & ACC_STATIC) != 0;
            List<StackMapFrameEntry> frames;
            try {
                frames = FrameComputer.compute(code, method.getDescriptor(), isStatic,
                        "<init>".equals(method.getName()), classFile.getClassIndex().intValue(),
                        classFile.getPoolInfo().getPoolList(), poolBuilder);
            } catch (RuntimeException e) {
                throw new AssertionError("方法 " + method.getName() + method.getDescriptor()
                        + " 帧计算失败: " + e.getMessage(), e);
            }
            table.setFrames(frames);
            recomputed++;
        }
        assertTrue(recomputed >= 10, "应至少重算 10 个方法，实际 " + recomputed);

        // ClassWriter 自己 seed 一个新池，会丢帧计算追加的项，所以复用同一个构造器
        byte[] rewritten = new ClassWriter(classFile, poolBuilder).toByteArray();

        // 只换外层类，内部类沿用原字节码：帧计算改的是 Frames 自己
        Map<String, byte[]> classes = new HashMap<>(fixture());
        classes.put("verify.Frames", rewritten);
        Class<?> reloaded = new MapClassLoader(classes).loadClass("verify.Frames");
        Object instance = reloaded.getConstructor().newInstance();

        assertEquals(14L, reloaded.getMethod("branch", int.class).invoke(instance, 5));
        assertEquals(-2L, reloaded.getMethod("branch", int.class).invoke(instance, 0));
        assertEquals("b", reloaded.getMethod("pick", int.class).invoke(instance, 7));
        assertEquals("d", reloaded.getMethod("pick", int.class).invoke(instance, 42));
        assertEquals("neg", reloaded.getMethod("sparse", int.class).invoke(instance, -1000));
        assertEquals("big", reloaded.getMethod("sparse", int.class).invoke(instance, 100000));
        assertEquals("ok", reloaded.getMethod("guarded", int.class).invoke(instance, 3));
        assertEquals("ise:neg-2", reloaded.getMethod("guarded", int.class).invoke(instance, -2));

        long[] widened = (long[]) reloaded.getMethod("widen", int.class).invoke(instance, 4);
        assertEquals(4, widened.length);
        assertEquals(3L, widened[3]);

        Object[][] grid = (Object[][]) reloaded.getMethod("nest", int.class).invoke(instance, 3);
        assertEquals(3, grid.length);
        assertEquals(3, grid[0].length);

        assertEquals("abc", reloaded.getMethod("chain", java.util.List.class)
                .invoke(instance, java.util.Arrays.asList("a", "b", "c")));

        assertEquals(4.25, (Double) reloaded.getMethod("mix", int.class, double.class, float.class, long.class)
                .invoke(instance, 5, 2.0, 1.5f, 4L), 1e-9);

        assertEquals(16, reloaded.getMethod("useInner", int.class).invoke(instance, 8));

        Field total = reloaded.getDeclaredField("total");
        total.setAccessible(true);
        // branch 最后一次把 total 写成 -2，之后 guarded 调了两次、每次 finally 加 1
        assertEquals(0L, total.get(instance));
    }

    @Test
    void framesAreParsedFromRealClassFiles() {
        ClassFile classFile = parse();
        int withFrames = 0;
        for (MethodInfo method : classFile.getMethods()) {
            for (AbstractAttribute a : method.getAttributes()) {
                if (!(a instanceof Code)) {
                    continue;
                }
                for (AbstractAttribute inner : ((Code) a).getAttributes()) {
                    if (inner instanceof StackMapTableAttribute) {
                        withFrames++;
                        assertTrue(((StackMapTableAttribute) inner).getFrames().size() > 0,
                                method.getName() + " 的栈帧表不应为空");
                    }
                }
            }
        }
        assertEquals(6, withFrames, "javac 产物里带帧的方法应至少 8 个，实际 " + withFrames);
    }

    @Test
    void descriptorParsingCoversArraysAndGenerics() {
        assertEquals(3, FrameComputer.parseParameterDescriptors("(ILjava/lang/String;[I)V").size());
        assertEquals("I", FrameComputer.parseParameterDescriptors("(ILjava/lang/String;[I)V").get(0));
        assertEquals("Ljava/lang/String;", FrameComputer.parseParameterDescriptors("(ILjava/lang/String;)V").get(1));
        assertEquals("[I", FrameComputer.parseParameterDescriptors("(ILjava/lang/String;[I)V").get(2));
        assertEquals("V", FrameComputer.parseReturnDescriptor("(ILjava/lang/String;)V"));
        assertEquals("[[Ljava/lang/Object;",
                FrameComputer.parseReturnDescriptor("()[[Ljava/lang/Object;"));
        assertEquals(0, FrameComputer.parseParameterDescriptors("()V").size());
    }

    @Test
    void mergingKeepsOnlyTypesAvailableOnEveryPath() {
        Frame a = Frame.fromSlots(java.util.Arrays.asList(VerificationType.integer(), VerificationType.top()),
                new java.util.ArrayList<VerificationType>());
        Frame b = Frame.fromSlots(java.util.Arrays.asList(VerificationType.top(), VerificationType.object(7)),
                new java.util.ArrayList<VerificationType>());
        a.merge(b, 10);
        assertEquals(VerificationType.top(), a.toSlots().get(0));
        assertEquals(VerificationType.top(), a.toSlots().get(1));
    }
}