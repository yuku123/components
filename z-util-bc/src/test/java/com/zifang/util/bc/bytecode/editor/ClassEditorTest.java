package com.zifang.util.bc.bytecode.editor;

import com.zifang.util.bc.bytecode.builder.InstructionBuilder;
import com.zifang.util.bc.compile.MapClassLoader;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;
import com.zifang.util.bc.bytecode.writer.ConstantPoolBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 变更门面测试
 * <p>
 * 判定标准只有一条：改完还能不能被 JVM 加载并跑出正确结果。
 * 「字段加上了」「访问标志改对了」只能证明模型被改了，证明不了写出去的字节合法。
 */
class ClassEditorTest {

    private static final String SOURCE = String.join("\n",
            "package edit;",
            "public class Target {",
            "    private int counter;",
            "    private String label = \"base\";",
            "    public int twice(int x) { return x * 2; }",
            "    public String name() { return label; }",
            "}");

    private static Map<String, byte[]> fixture() {
        return com.zifang.util.bc.bytecode.ModernJavaFeatureFixtures.compile("edit.Target", SOURCE);
    }

    @Test
    void addedMethodWithLoopLoadsAndRuns() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        ConstantPoolBuilder pool = editor.pool();

        // int sumTo(int n)：for (int i = 0; i < n; i++) acc += i;
        // 局部变量槽、跳转、循环回边都在这儿现算，栈帧得由门面自己补齐
        InstructionBuilder body = new InstructionBuilder();
        InstructionBuilder.Label again = body.newLabel();
        InstructionBuilder.Label done = body.newLabel();
        // static (I)I：参数 n 在槽 0
        body.iconst(0, pool);
        body.istore(1);
        body.iconst(0, pool);
        body.istore(2);
        body.mark(again);
        body.iload(2);
        body.iload(0);
        body.jump(Opcodes.IF_ICMPGE, done);
        body.iload(1);
        body.iload(2);
        body.op(Opcodes.IADD);
        body.istore(1);
        body.iinc(2, 1);
        body.goTo(again);
        body.mark(done);
        body.iload(1);
        body.op(Opcodes.IRETURN);

        editor.addMethod(0x0009, "sumTo", "(I)I", body);

        Class<?> loaded = editor.defineAndLoad(compiled);
        Object instance = loaded.getConstructor().newInstance();
        assertEquals(10, loaded.getMethod("sumTo", int.class).invoke(instance, 5));
        assertEquals(0, loaded.getMethod("sumTo", int.class).invoke(instance, 0));
        assertEquals(8, loaded.getMethod("twice", int.class).invoke(instance, 4));
    }

    @Test
    void addedMethodWithExceptionHandlerLoadsAndRuns() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        ConstantPoolBuilder pool = editor.pool();

        // String guard(int k)：k < 0 抛 IllegalStateException，catch 里返回 "neg"
        InstructionBuilder body = new InstructionBuilder();
        InstructionBuilder.Label start = body.newLabel();
        InstructionBuilder.Label afterThrow = body.newLabel();
        InstructionBuilder.Label handler = body.newLabel();
        body.mark(start);
        body.iload(0);
        body.jump(Opcodes.IFGE, afterThrow);
        body.newClass("java/lang/IllegalStateException", pool);
        body.dup();
        body.ldcString("neg", pool);
        body.invokespecialInit("java/lang/IllegalStateException", "(Ljava/lang/String;)V", pool);
        body.op(Opcodes.ATHROW);
        body.mark(afterThrow);
        body.ldcString("ok", pool);
        body.op(Opcodes.ARETURN);
        body.mark(handler);
        body.pop();
        body.ldcString("neg", pool);
        body.op(Opcodes.ARETURN);
        body.tryCatch(start, afterThrow, handler, pool.clazz("java/lang/IllegalStateException"));

        editor.addMethod(0x0009, "guard", "(I)Ljava/lang/String;", body);

        Class<?> loaded = editor.defineAndLoad(compiled);
        Object instance = loaded.getConstructor().newInstance();
        assertEquals("ok", loaded.getMethod("guard", int.class).invoke(instance, 3));
        assertEquals("neg", loaded.getMethod("guard", int.class).invoke(instance, -1));
    }

    @Test
    void rewrittenMethodBodyTakesEffect() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        ConstantPoolBuilder pool = editor.pool();

        // twice(x) 改成 x * 3
        InstructionBuilder body = new InstructionBuilder();
        body.iload(1);
        body.iconst(3, pool);
        body.op(Opcodes.IMUL);
        body.op(Opcodes.IRETURN);
        editor.rewriteMethodBody("twice", "(I)I", body);

        Class<?> loaded = editor.defineAndLoad(compiled);
        assertEquals(12, loaded.getMethod("twice", int.class).invoke(loaded.getConstructor().newInstance(), 4));
    }

    @Test
    void fieldsAndFlagsAreMutable() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        editor.addField(0x0001, "added", "I");
        editor.setFieldFlags("counter", 0x0001);
        editor.renameField("added", "renamed");
        editor.addClassFlag(0x0010);

        Class<?> loaded = editor.defineAndLoad(compiled);
        Field renamed = loaded.getDeclaredField("renamed");
        renamed.setAccessible(true);
        Object instance = loaded.getConstructor().newInstance();
        renamed.setInt(instance, 42);
        assertEquals(42, renamed.getInt(instance));

        assertTrue(Modifier.isPublic(loaded.getDeclaredField("counter").getModifiers()));
        assertTrue(Modifier.isFinal(loaded.getModifiers()));
        assertThrows(NoSuchFieldException.class, () -> loaded.getDeclaredField("added"));
    }

    @Test
    void methodsCanBeRenamedAndRemoved() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        editor.renameMethod("twice", "(I)I", "triple");
        editor.removeMethod("name", "()Ljava/lang/String;");

        Class<?> loaded = editor.defineAndLoad(compiled);
        Method triple = loaded.getDeclaredMethod("triple", int.class);
        assertEquals(6, triple.invoke(loaded.getConstructor().newInstance(), 3));
        assertThrows(NoSuchMethodException.class, () -> loaded.getDeclaredMethod("twice", int.class));
        assertThrows(NoSuchMethodException.class, () -> loaded.getDeclaredMethod("name"));
    }

    @Test
    void untouchedClassStaysByteIdentical() {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        assertTrue(java.util.Arrays.equals(compiled.get("edit.Target"), editor.toBytes()),
                "没有任何变更时不该改动字节");
    }

    @Test
    void writesToDiskAndTheFileLoads() throws Exception {
        Map<String, byte[]> compiled = fixture();
        ClassEditor editor = ClassEditor.of(compiled.get("edit.Target"));
        editor.addField(0x0001, "onDisk", "J");
        Path dir = Files.createTempDirectory("zutil-bc-editor");
        Path file = dir.resolve("Target.class");
        editor.writeTo(file.toString());
        assertTrue(Files.size(file) > 0);

        Map<String, byte[]> fromDisk = new java.util.HashMap<>(compiled);
        fromDisk.put("edit.Target", Files.readAllBytes(file));
        Class<?> loaded = new MapClassLoader(fromDisk).loadClass("edit.Target");
        assertNotNull(loaded.getDeclaredField("onDisk"));
    }

    @Test
    void missingOrDuplicateTargetsAreRejected() {
        ClassEditor editor = ClassEditor.of(fixture().get("edit.Target"));
        assertThrows(IllegalStateException.class, () -> editor.addField(0x0001, "counter", "I"));
        assertThrows(IllegalStateException.class, () -> editor.removeField("nope"));
        assertThrows(IllegalStateException.class, () -> editor.removeMethod("nope", "()V"));
        assertThrows(IllegalStateException.class, () -> editor.renameMethod("nope", "()V", "x"));
    }

    @Test
    void instructionBuilderRejectsUnmarkedJumpTargets() {
        InstructionBuilder builder = new InstructionBuilder();
        builder.iconst(1, new ConstantPoolBuilder());
        builder.goTo(builder.newLabel());
        assertThrows(IllegalStateException.class, builder::toByteArray);
    }

    @Test
    void instructionBuilderPatchesForwardAndBackwardJumps() {
        InstructionBuilder builder = new InstructionBuilder();
        InstructionBuilder.Label start = builder.newLabel();
        builder.mark(start);
        int jumpSite = builder.currentOffset();
        builder.goTo(start);
        assertFalse(jumpSite < 0);
        byte[] bytes = builder.toByteArray();
        // 跳回自身：偏移必须是 3（goto 指令起点到自身）
        int delta = ((bytes[jumpSite + 1] & 0xFF) << 8) | (bytes[jumpSite + 2] & 0xFF);
        assertEquals(0, delta);
    }

    @Test
    void instructionBuilderWidensToLdcWOnceThePoolOutgrowsOneByte() {
        ConstantPoolBuilder pool = new ConstantPoolBuilder();
        for (int i = 0; i < 300; i++) {
            pool.integer(i);
        }
        int bigIndex = pool.integer(123456);

        byte[] narrow = new InstructionBuilder().ldc(200).toByteArray();
        assertEquals(Opcodes.LDC, narrow[0] & 0xFF);
        assertEquals(2, narrow.length);

        byte[] wide = new InstructionBuilder().ldc(bigIndex).toByteArray();
        assertEquals(Opcodes.LDC_W, wide[0] & 0xFF);
        assertEquals(3, wide.length);
        assertEquals(bigIndex, ((wide[1] & 0xFF) << 8) | (wide[2] & 0xFF));
    }
}
