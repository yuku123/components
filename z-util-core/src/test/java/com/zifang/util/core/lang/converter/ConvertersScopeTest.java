package com.zifang.util.core.lang.converter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * scope 分组、候选链排序与链式兜底。
 */
class ConvertersScopeTest {

    private static final String SCOPE = "test-scope";

    @AfterEach
    void 清理测试scope() {
        ConverterScope scope = Converters.scopes().stream()
                .filter(s -> SCOPE.equals(s.getName()))
                .findFirst().orElse(null);
        if (scope != null) {
            Converters.unregisterScope(SCOPE);
            Converters.disableScope(SCOPE);
            Converters.enableScope(SCOPE);
        }
    }

    @Test
    void 同一类型对全链路保留直到成功() {
        ConverterScope scope = Converters.defineScope(SCOPE, "core", 100);
        Converters.register(scope, String.class, LocalDate.class,
                (value, context) -> {
                    throw new ConvertException(value, String.class, LocalDate.class, "只支持 2024 年");
                });
        Converters.register(scope, String.class, LocalDate.class,
                (value, context) -> LocalDate.parse(value, java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        // 第一个转换器失败后继续试链上的下一个
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("2024/01/02", LocalDate.class));
        // 全部失败时抛异常，链上 test-scope 注册的两条失败原因都在
        ConvertException e = assertThrows(ConvertException.class, () -> Converters.to("nope", LocalDate.class));
        long fromTestScope = e.getAttempts().stream()
                .filter(a -> SCOPE.equals(a.getScopeName()))
                .count();
        assertEquals(2, fromTestScope, () -> "实际尝试: " + e.getAttempts());
    }

    @Test
    void 权重高的先试() {
        ConverterScope scope = Converters.defineScope(SCOPE, "core", 100);
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(2000, 1, 1), 1);
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(1999, 1, 1), 9);

        assertEquals(LocalDate.of(1999, 1, 1), Converters.to("2024-01-02", LocalDate.class));
    }

    @Test
    void 同权重先注册者先试() {
        ConverterScope scope = Converters.defineScope(SCOPE, "core", 100);
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(2000, 1, 1));
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(1999, 1, 1));

        assertEquals(LocalDate.of(2000, 1, 1), Converters.to("2024-01-02", LocalDate.class));
    }

    @Test
    void 类型距离优先于权重() {
        ConverterScope scope = Converters.defineScope(SCOPE, "core", 100);
        // 精确匹配但权重低
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(2000, 1, 1));
        // 走兜底(String->Object)但权重高
        Converters.register(scope, String.class, Object.class,
                (value, context) -> LocalDate.of(1999, 1, 1), 999);

        assertEquals(LocalDate.of(2000, 1, 1), Converters.to("2024-01-02", LocalDate.class));
    }

    @Test
    void scope可整组停用() {
        ConverterScope scope = Converters.defineScope(SCOPE, "core", 100);
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(2000, 1, 1));

        assertEquals(LocalDate.of(2000, 1, 1), Converters.to("2024-01-02", LocalDate.class));
        Converters.disableScope(SCOPE);
        // 停用后回到 core 的默认解析
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("2024-01-02", LocalDate.class));
        Converters.enableScope(SCOPE);
        assertEquals(LocalDate.of(2000, 1, 1), Converters.to("2024-01-02", LocalDate.class));
    }

    @Test
    void 子scope继承父scope() {
        ConverterScope parent = Converters.defineScope(SCOPE, "core", 100);
        ConverterScope child = Converters.defineScope(SCOPE + "-child", SCOPE, 200);
        Converters.register(parent, String.class, LocalDate.class, (value, context) -> LocalDate.of(2000, 1, 1));

        // 子 scope 的条目在父 scope 之下，但父 scope 的条目对子 scope 依然可见
        assertEquals(LocalDate.of(2000, 1, 1), Converters.to("2024-01-02", LocalDate.class));
        assertEquals(200, child.getPriority());
        assertEquals(parent, child.getParent());

        // 父 scope 停用会连带子 scope 失效
        Converters.disableScope(SCOPE);
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("2024-01-02", LocalDate.class));
        assertFalse(child.isActive());
    }

    @Test
    void 注销后回到默认解析() {
        ConverterScope scope = Converters.defineScope(SCOPE, "core", 100);
        Converters.register(scope, String.class, LocalDate.class, (value, context) -> LocalDate.of(2000, 1, 1));
        assertEquals(LocalDate.of(2000, 1, 1), Converters.to("2024-01-02", LocalDate.class));

        assertTrue(Converters.unregister(SCOPE, String.class, LocalDate.class));
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("2024-01-02", LocalDate.class));
    }

    @Test
    void 注册表快照可读() {
        String snapshot = Converters.describe();
        assertTrue(snapshot.contains("core"), snapshot);
        assertTrue(snapshot.contains("String -> LocalDate"), snapshot);
    }

    @Test
    void 无候选时明确报无候选() {
        ConvertException e = assertThrows(ConvertException.class,
                () -> Converters.to(new Object(), LocalDate.class));
        assertTrue(e.getMessage().contains("无候选转换器"), e.getMessage());
    }

    @Test
    void 候选链顺序确定() {
        List<ConverterEntry<?, ?>> first = Converters.registry().chain(String.class, LocalDate.class);
        List<ConverterEntry<?, ?>> second = Converters.registry().chain(String.class, LocalDate.class);
        assertEquals(first.size(), second.size());
        for (int i = 0; i < first.size(); i++) {
            assertEquals(first.get(i).getDescription(), second.get(i).getDescription());
            assertEquals(first.get(i).getSequence(), second.get(i).getSequence());
        }
    }
}