package com.zifang.util.core.lang.converter;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.zifang.util.core.lang.converter.fixture.YearMonthConverter;

import java.time.Duration;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 注解 + 扫描驱动注册。
 *
 * 扫描器的去重记录是进程级的，测试间共享，因此固定方法顺序：
 * 唯一断言"首次扫描注册条数"的用例必须排在最前。
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConverterScannerTest {

    private static final String FIXTURE_PACKAGE =
            "com.zifang.util.core.lang.converter.fixture";

    @Test
    @Order(1)
    void 扫描注册类级与方法级转换器() {
        int first = ConverterScanner.scan(FIXTURE_PACKAGE);
        assertEquals(2, first);

        assertEquals(YearMonth.of(2024, 1), Converters.to("2024-01", YearMonth.class));
        assertEquals(Duration.ofMinutes(90), Converters.to("90m", Duration.class));
        assertEquals(Duration.ofHours(2), Converters.to("2h", Duration.class));
    }

    @Test
    @Order(2)
    void 重复扫描幂等() {
        ConverterScanner.scan(FIXTURE_PACKAGE);
        assertEquals(0, ConverterScanner.scan(FIXTURE_PACKAGE));

        // 幂等不是"注册项只剩一份"的另一种表现：精确匹配仍只有注解声明的那一条在最前
        List<ConverterEntry<?, ?>> chain = Converters.registry().chain(String.class, YearMonth.class);
        assertEquals(YearMonthConverter.class.getSimpleName(), chain.get(0).getConverterType()
                .substring(chain.get(0).getConverterType().lastIndexOf('.') + 1));
    }

    @Test
    @Order(3)
    void 未知简写走完整链路后失败() {
        ConverterScanner.scan(FIXTURE_PACKAGE);
        ConvertException e = assertThrows(ConvertException.class, () -> Converters.to("3d", Duration.class));
        assertTrue(e.getAttempts().size() >= 1);
    }

    @Test
    @Order(4)
    void 扫描出的转换器归属于声明的scope() {
        ConverterScanner.scan(FIXTURE_PACKAGE);
        ConverterScope fixture = Converters.scopes().stream()
                .filter(s -> "fixture".equals(s.getName()))
                .findFirst().orElse(null);
        assertTrue(fixture != null, "fixture scope 应已创建");
        assertTrue(Converters.describe().contains("fixture"));
    }
}