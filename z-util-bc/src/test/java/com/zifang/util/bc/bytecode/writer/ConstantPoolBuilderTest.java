package com.zifang.util.bc.bytecode.writer;

import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.Utf8Info;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 常量池构造器测试
 * <p>
 * 核心是一条不变式：<b>返回的下标必须正好指向刚追加的那一项</b>。
 * 下标偏一不会编译报错，也不会在解析阶段暴露，只会在写出的 class 被加载时
 * 变成一句莫名其妙的 Invalid constant pool index，所以只能直接钉住。
 */
class ConstantPoolBuilderTest {

    @Test
    void everyInternedIndexPointsAtItsOwnEntry() {
        ConstantPoolBuilder builder = new ConstantPoolBuilder();
        int utf8 = builder.utf8("java/lang/String");
        int clazz = builder.clazz("java/lang/String");
        int method = builder.methodRef("java/lang/Object", "toString", "()Ljava/lang/String;");
        List<AbstractConstantPool> pool = builder.build();

        assertEquals("java/lang/String", ((Utf8Info) pool.get(utf8 - 1)).getValue());
        assertEquals(clazz, builder.clazz("java/lang/String"), "同一个类应当复用下标");
        assertEquals(method, builder.methodRef("java/lang/Object", "toString", "()Ljava/lang/String;"),
                "同一个方法引用应当复用下标");
        assertEquals(pool.size() + 1, builder.constantPoolCount());
    }

    @Test
    void wideConstantsReserveTwoSlots() {
        ConstantPoolBuilder builder = new ConstantPoolBuilder();
        int value = builder.longConstant(42L);
        assertEquals(1, value);
        assertNull(builder.build().get(value), "long 之后必须留一个空槽");
        // 空槽占位后，下一项的下标要跳过它
        assertEquals(3, builder.utf8("x"));
        assertEquals(4, builder.constantPoolCount());
    }

    @Test
    void seedingKeepsOriginalIndicesAndInternsExistingEntries() {
        List<AbstractConstantPool> original = new java.util.ArrayList<>();
        original.add(new Utf8Info("java/lang/String"));
        ConstantPoolHelper helper = new ConstantPoolHelper(original);

        ConstantPoolBuilder builder = new ConstantPoolBuilder().seed(original, helper);
        assertEquals(1, builder.utf8("java/lang/String"), "原有 utf8 应复用原下标");
        assertEquals(2, builder.utf8("新增"));
    }
}
