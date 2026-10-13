package com.zifang.util.core.lang.converter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 标量族的转换矩阵与失败路径。
 */
class ConvertersScalarTest {

    // ---------- 字符串 ----------

    @Test
    void 数字与字符转字符串() {
        assertEquals("1", Converters.to(1, String.class));
        assertEquals("true", Converters.to(true, String.class));
        assertEquals("a", Converters.to('a', String.class));
        assertEquals("[1, 2, 3]", Converters.to(new int[]{1, 2, 3}, String.class));
        assertEquals("2024-01-02", Converters.to(LocalDate.of(2024, 1, 2), String.class));
    }

    @Test
    void 字符串转字符() {
        assertEquals('a', Converters.to("a", Character.class));
        assertEquals('a', Converters.to(" a ", Character.class));
        assertThrows(ConvertException.class, () -> Converters.to("ab", Character.class));
    }

    // ---------- 数值 ----------

    @Test
    void 字符串转八种数值类型() {
        assertEquals((byte) 12, Converters.to("12", Byte.class));
        assertEquals((short) 12, Converters.to("12", Short.class));
        assertEquals(12, Converters.to("12", Integer.class));
        assertEquals(12L, Converters.to("12", Long.class));
        assertEquals(12f, Converters.to("12", Float.class));
        assertEquals(12d, Converters.to("12", Double.class));
        assertEquals(BigInteger.valueOf(12), Converters.to("12", BigInteger.class));
        assertEquals(new BigDecimal("12.50"), Converters.to("12.50", BigDecimal.class));
    }

    @Test
    void 字符串数值按策略trim() {
        assertEquals(12, Converters.to(" 12 ", Integer.class));
        ConvertPolicy noTrim = ConvertPolicy.defaults().toBuilder().trim(false).build();
        assertThrows(ConvertException.class, () -> Converters.to(" 12 ", Integer.class, noTrim));
    }

    @Test
    void 数值窄化按溢出策略() {
        assertThrows(ConvertException.class, () -> Converters.to(300L, Byte.class));
        ConvertPolicy saturate = ConvertPolicy.defaults().toBuilder()
                .narrowingPolicy(ConvertPolicy.NarrowingPolicy.SATURATE).build();
        assertEquals((byte) 127, Converters.to(300L, Byte.class, saturate));
        assertEquals((byte) -128, Converters.to(-300L, Byte.class, saturate));
        ConvertPolicy wrap = ConvertPolicy.defaults().toBuilder()
                .narrowingPolicy(ConvertPolicy.NarrowingPolicy.WRAP).build();
        assertEquals((byte) 300, Converters.to(300L, Byte.class, wrap));
    }

    @Test
    void 小数转整数按舍入模式() {
        assertEquals(2, Converters.to(new BigDecimal("1.9"), Integer.class));
        ConvertPolicy floor = ConvertPolicy.defaults().toBuilder().roundingMode(RoundingMode.FLOOR).build();
        assertEquals(1, Converters.to(new BigDecimal("1.9"), Integer.class, floor));
    }

    @Test
    void 强制小数位与去尾零() {
        ConvertPolicy scaled = ConvertPolicy.defaults().toBuilder().scale(2).build();
        assertEquals(new BigDecimal("1.24"), Converters.to("1.235", BigDecimal.class, scaled));
        assertEquals(new BigDecimal("1.23"),
                Converters.to("1.235", BigDecimal.class,
                        scaled.toBuilder().roundingMode(RoundingMode.DOWN).build()));
    }

    @Test
    void 非法数字抛异常而不是返回null() {
        ConvertException e = assertThrows(ConvertException.class, () -> Converters.to("abc", Integer.class));
        assertEquals("abc", e.getValue());
        assertEquals(String.class, e.getSourceType());
        assertEquals(Integer.class, e.getTargetType());
        assertTrue(e.getAttempts().size() >= 1, "异常应带上候选链的失败原因");
    }

    // ---------- 布尔 ----------

    @Test
    void 布尔词表可配置() {
        assertEquals(true, Converters.to("true", Boolean.class));
        assertEquals(true, Converters.to("YES", Boolean.class));
        assertEquals(false, Converters.to("off", Boolean.class));
        assertThrows(ConvertException.class, () -> Converters.to("是", Boolean.class));

        ConvertPolicy chinese = ConvertPolicy.defaults().toBuilder()
                .trueValues("是").falseValues("否").build();
        assertEquals(true, Converters.to("是", Boolean.class, chinese));
    }

    // ---------- 枚举 ----------

    @Test
    void 枚举字面量匹配() {
        assertEquals(DayOfWeek.MONDAY, Converters.to("MONDAY", DayOfWeek.class));
        assertThrows(ConvertException.class, () -> Converters.to("MONDAY_X", DayOfWeek.class));
        assertThrows(ConvertException.class, () -> Converters.to("abc", DayOfWeek.class));
    }

    @Test
    void 枚举别名与大小写策略() {
        ConvertPolicy policy = ConvertPolicy.defaults().toBuilder()
                .enumAlias("mon", "MONDAY")
                .enumMatchPolicy(ConvertPolicy.EnumMatchPolicy.CASE_INSENSITIVE)
                .build();
        assertEquals(DayOfWeek.MONDAY, Converters.to("mon", DayOfWeek.class, policy));
        assertEquals(DayOfWeek.MONDAY, Converters.to("monday", DayOfWeek.class, policy));
        assertThrows(ConvertException.class,
                () -> Converters.to("monday", DayOfWeek.class, ConvertPolicy.defaults()));
    }

    // ---------- 时间 ----------

    @Test
    void 字符串转时间类型() {
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("2024-01-02", LocalDate.class));
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("2024/01/02", LocalDate.class));
        assertEquals(LocalDateTime.of(2024, 1, 2, 3, 4, 5),
                Converters.to("2024-01-02 03:04:05", LocalDateTime.class));
        assertEquals(LocalTime.of(3, 4, 5), Converters.to("03:04:05", LocalTime.class));
        assertEquals(Duration.ofMinutes(90), Converters.to("PT1H30M", Duration.class));
        assertEquals(Instant.ofEpochMilli(1700000000000L), Converters.to("1700000000000", Instant.class));
    }

    @Test
    void 时间格式失败时给出候选格式() {
        ConvertException e = assertThrows(ConvertException.class,
                () -> Converters.to("02/01/2024", LocalDate.class));
        assertTrue(e.getMessage().contains("候选格式"), "异常应说明试过哪些格式");
    }

    @Test
    void 自定义时间格式() {
        ConvertPolicy policy = ConvertPolicy.defaults().toBuilder()
                .format(LocalDate.class, "dd/MM/yyyy").build();
        assertEquals(LocalDate.of(2024, 1, 2), Converters.to("02/01/2024", LocalDate.class, policy));
        assertThrows(ConvertException.class, () -> Converters.to("2024-01-02", LocalDate.class, policy));
    }

    @Test
    void javaUtilDate双向桥() {
        LocalDate date = LocalDate.of(2024, 1, 2);
        Date asDate = Converters.to(date, Date.class);
        assertEquals(date, Converters.to(asDate, LocalDate.class));
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 3, 4, 5);
        assertEquals(dateTime, Converters.to(Converters.to(dateTime, Date.class), LocalDateTime.class));
        assertEquals(date.atStartOfDay(ZoneId.systemDefault()).toInstant(), Converters.to(asDate, Instant.class));
    }

    // ---------- 值类型 ----------

    @Test
    void 值类型解析() {
        UUID uuid = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
        assertEquals(uuid, Converters.to("6ba7b810-9dad-11d1-80b4-00c04fd430c8", UUID.class));
        assertThrows(ConvertException.class, () -> Converters.to("a1b2", UUID.class));
        assertEquals("https://a.example/x", Converters.to("https://a.example/x", java.net.URI.class).toString());
        assertThrows(ConvertException.class, () -> Converters.to("::::", java.net.URI.class));
    }

    // ---------- null 与空串 ----------

    @Test
    void null输入恒返回null() {
        assertNull(Converters.to(null, String.class));
        assertNull(Converters.to(null, Integer.class));
        assertNull(Converters.to(null, LocalDate.class));
    }

    @Test
    void 空串按策略处理() {
        assertNull(Converters.to("", Integer.class));
        assertNull(Converters.to("   ", Integer.class));

        ConvertPolicy fail = ConvertPolicy.defaults().toBuilder()
                .blankPolicy(ConvertPolicy.BlankPolicy.FAIL).build();
        assertThrows(ConvertException.class, () -> Converters.to("", Integer.class, fail));

        ConvertPolicy keep = ConvertPolicy.defaults().toBuilder()
                .blankPolicy(ConvertPolicy.BlankPolicy.KEEP).build();
        assertThrows(ConvertException.class, () -> Converters.to("", Integer.class, keep));
    }

    // ---------- 宽松轨 ----------

    @Test
    void 宽松轨失败返回默认值() {
        assertEquals(-1, Converters.toOrDefault("abc", Integer.class, -1));
        assertEquals(7, Converters.toOrDefault("7", Integer.class, -1));
        assertNull(Converters.toOrDefault(null, Integer.class, -1));
        assertEquals(DayOfWeek.FRIDAY, Converters.toOrDefault("abc", DayOfWeek.class, DayOfWeek.FRIDAY));
    }

    // ---------- 集合 ----------

    @Test
    void 列表元素逐个转换() {
        assertEquals(Arrays.asList(1, 2, 3),
                Converters.toList(Arrays.asList("1", "2", "3"), Integer.class, ConvertPolicy.defaults()));
        assertEquals(Collections.singletonList(null),
                Converters.toList(Collections.singletonList(null), Integer.class, ConvertPolicy.defaults()));
    }

    @Test
    void 列表元素失败带出下标() {
        ConvertException e = assertThrows(ConvertException.class,
                () -> Converters.toList(Arrays.asList("1", "x", "3"), Integer.class, ConvertPolicy.defaults()));
        assertEquals("[1]", e.getPath());
        assertEquals("x", e.getValue());
    }

    @Test
    void 数组与集合互转() {
        Integer[] array = Converters.toArray(Arrays.asList("1", "2"), Integer.class, ConvertPolicy.defaults());
        assertEquals(Arrays.asList(1, 2), Arrays.asList(array));
        assertEquals(2, Converters.toSet(Arrays.asList("1", "1", "2"), Integer.class,
                ConvertPolicy.defaults()).size());
    }

    @Test
    void 映射键值分别转换() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("1", "2024-01-02");
        Map<Integer, LocalDate> converted = Converters.toMap(raw, Integer.class, LocalDate.class,
                ConvertPolicy.defaults());
        assertEquals(LocalDate.of(2024, 1, 2), converted.get(1));
    }
}