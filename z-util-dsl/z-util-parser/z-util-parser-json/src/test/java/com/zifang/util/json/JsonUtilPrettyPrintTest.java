package com.zifang.util.json;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * toJsonPretty 只准改空白，不准改内容。
 *
 * <p>起因是 z-bot 迁 Jackson 时踩到的真损坏：旧实现把 JSON 当纯文本重排缩进，
 * 字符串值里的 ':' 后面被塞了空格，"cli:54353:1" 落盘变成 "cli: 54353: 1"。
 */
class JsonUtilPrettyPrintTest {

    @Test
    void 字符串里的结构符号不被当语法() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("claim_token", "cli:54353:1");
        m.put("url", "https://h/p?q=1&r=2");
        m.put("sep", "a,b");
        m.put("brace", "{not a container}");

        String pretty = JsonUtil.toJsonPretty(m);
        for (Object v : m.values()) {
            // 无需转义的字符，序列化前后逐字面相同；旧实现会把 ':' 后塞空格、',' 后塞换行
            assertTrue(pretty.contains(String.valueOf(v)), "值被改写了: " + v + "\n" + pretty);
        }
    }

    @Test
    void 漂亮化后仍能原样读回() {
        Map<String, Object> inner = new LinkedHashMap<>();
        inner.put("k", "a:b,c");
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("token", "cli:54353:1");
        m.put("nested", inner);
        m.put("list", Arrays.asList("x:1", "y,2"));
        m.put("quoted", "he said \"hi\", then left: really");

        Map<?, ?> back = (Map<?, ?>) JsonUtil.toPlainValue(JsonUtil.parse(JsonUtil.toJsonPretty(m)));
        assertEquals(m, back);
    }

    @Test
    void 转义引号不会提前结束字符串() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("v", "a\\\"b:c,d");
        String pretty = JsonUtil.toJsonPretty(m);
        // 转义引号没有提前结束字符串，里面的 ':' 仍在字面量内 —— 不得被加分隔空格
        assertFalse(pretty.contains("b: c"), pretty);
        assertTrue(pretty.contains("b:c,d"), pretty);
        assertEquals(m, JsonUtil.toPlainValue(JsonUtil.parse(pretty)));
    }

    @Test
    void 确实是缩进过的() {
        Map<String, Object> inner = new LinkedHashMap<>();
        inner.put("k", 1);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("nested", inner);
        String pretty = JsonUtil.toJsonPretty(m);
        assertTrue(pretty.contains("\n"), pretty);
        assertTrue(pretty.contains("  "), pretty);
    }

    @Test
    void 顶层标量与集合不炸() {
        assertEquals("null", JsonUtil.toJsonPretty(null));
        assertEquals("1", JsonUtil.toJsonPretty(1));
        List<String> list = Arrays.asList("a:b", "c,d");
        assertEquals(list, JsonUtil.toPlainValue(JsonUtil.parse(JsonUtil.toJsonPretty(list))));
    }
}
