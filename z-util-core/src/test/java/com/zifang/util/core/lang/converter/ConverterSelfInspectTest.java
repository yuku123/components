package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.converter.fixture.Status;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 枚举按 {@link EnumValue} 字段取值，以及注册表自省输出。
 */
class ConverterSelfInspectTest {

    @Test
    void 枚举按业务值字段匹配() {
        assertEquals(Status.ENABLED, Converters.to("1", Status.class));
        assertEquals(Status.DISABLED, Converters.to("0", Status.class));
    }

    @Test
    void 声明了业务值字段就不回退常量名() {
        // "ENABLED" 是常量名但不是业务值，直接判失败，避免"匹配成功却取错值"
        ConvertException e = assertThrows(ConvertException.class, () -> Converters.to("ENABLED", Status.class));
        assertTrue(e.getMessage().contains("无 code 为 ENABLED 的常量"), e.getMessage());
        assertTrue(e.getMessage().contains("可选值=[1, 0]"), e.getMessage());
    }

    @Test
    void 业务值字段支持大小写策略与别名() {
        ConvertPolicy policy = ConvertPolicy.defaults().toBuilder()
                .enumMatchPolicy(ConvertPolicy.EnumMatchPolicy.CASE_INSENSITIVE)
                .enumAlias("on", "1")
                .build();
        assertEquals(Status.ENABLED, Converters.to("on", Status.class, policy));
    }

    @Test
    void 无业务值字段的枚举仍按常量名() {
        assertEquals(DayOfWeek.MONDAY, Converters.to("MONDAY", DayOfWeek.class));
    }

    @Test
    void 注册表快照打出scope树() {
        ConverterScope parent = Converters.defineScope("inspect", "core", 10);
        Converters.defineScope("inspect-child", "inspect", 20);
        Converters.register(parent, String.class, LocalDate.class,
                (value, context) -> LocalDate.parse(value));

        String snapshot = Converters.describe();
        assertTrue(snapshot.contains("core"), snapshot);
        assertTrue(snapshot.contains("inspect  (priority=10"), snapshot);
        assertTrue(snapshot.contains("inspect-child  (priority=20"), snapshot);

        Converters.unregisterScope("inspect");
    }

    @Test
    void 命中链路说明排序依据() {
        String chain = Converters.explain(String.class, LocalDate.class);
        assertTrue(chain.contains("解析链路 String -> LocalDate"), chain);
        assertTrue(chain.contains("distance="), chain);
        assertTrue(chain.contains("scope=core"), chain);
    }

    @Test
    void 无候选的类型对链路为空() {
        assertTrue(Converters.explain(Object.class, LocalDate.class).contains("命中 0 个候选"));
    }
}