package com.zifang.util.core.pattern.register;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 烟雾: Register 的核心契约 = put/get/contains + 拒 null key.
 * key=null 抛 IllegalArgumentException 是关键安全网: null key 在 put 时静默成功,
 * 下游 get/contains 会 NPE, 难定位. 钉死这条.
 */
class RegisterTest {

    @Test
    @DisplayName("register/get/contains 三连必须贯通")
    void putGetContains() {
        Register<String, Integer> r = new Register<String, Integer>();
        r.register("k", 42);
        assertTrue(r.contains("k"));
        assertEquals(42, r.get("k"));
    }

    @Test
    @DisplayName("register(null key) 必须立刻抛 IllegalArgumentException, 不能静默成功")
    void nullKeyRejectedImmediately() {
        Register<String, Integer> r = new Register<String, Integer>();
        assertThrows(IllegalArgumentException.class, () -> r.register(null, 1),
                "null key 静默成功会导致下游 NPE, 必须立刻抛");
    }

    @Test
    @DisplayName("未注册的 key get 必须返 null, 不能抛")
    void missingGetReturnsNull() {
        Register<String, Integer> r = new Register<String, Integer>();
        assertFalse(r.contains("missing"));
        assertEquals(null, r.get("missing"));
    }

    @Test
    @DisplayName("putIfAbsent 首次写入返 null, 重复写返旧值且不覆盖")
    void putIfAbsentDoesNotOverwrite() {
        Register<String, Integer> r = new Register<String, Integer>();
        assertNull(r.putIfAbsent("k", 1));
        assertEquals(1, r.get("k"));
        assertEquals(1, r.putIfAbsent("k", 2));
        assertEquals(1, r.get("k"), "putIfAbsent 不能覆盖既有值");
    }

    @Test
    @DisplayName("putIfAbsent(null key) 同样必须立刻抛, 不能比其他入口宽松")
    void putIfAbsentRejectsNullKey() {
        Register<String, Integer> r = new Register<String, Integer>();
        assertThrows(IllegalArgumentException.class, () -> r.putIfAbsent(null, 1));
    }
}