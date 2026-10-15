package com.zifang.util.json;

import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * parse(String) 与 toPlainValue(Object) 的契约。
 * <p>这两个方法是迁 Jackson 过程中补出来的：前者补通用解析入口（原先只有
 * parseObject/parseArray/parseToMap），后者补「容器摊平成 plain 结构」——
 * JsonObject 的 toString() 是缩进格式，需要紧凑单行的场景必须先过这一层。</p>
 */
public class JsonUtilParseTest {

    @Test
    public void parse按文档结构返回不同容器() {
        assertTrue(JsonUtil.parse("{\"a\":1}") instanceof JsonObject);
        assertTrue(JsonUtil.parse("[1,2]") instanceof JsonArray);
        assertEquals("x", JsonUtil.parse("\"x\""));
        assertEquals(Integer.valueOf(1), JsonUtil.parse("1"));
        assertEquals(Boolean.TRUE, JsonUtil.parse("true"));
    }

    @Test
    public void parse空输入返回null() {
        assertNull(JsonUtil.parse(null));
        assertNull(JsonUtil.parse(""));
        assertNull(JsonUtil.parse("   "));
    }

    @Test
    public void toPlainValue把容器摊平成有序Map与List() {
        Object plain = JsonUtil.toPlainValue(JsonUtil.parse("{\"b\":1,\"a\":[{\"x\":1}]}"));
        assertTrue(plain instanceof Map);
        Map<?, ?> m = (Map<?, ?>) plain;
        assertEquals(Integer.valueOf(1), m.get("b"));
        // 值是 plain 的 List，内部也是 plain 的 Map
        List<?> a = (List<?>) m.get("a");
        assertEquals(Integer.valueOf(1), ((Map<?, ?>) a.get(0)).get("x"));
        // 摊平后装进 LinkedHashMap，键序即文档顺序
        assertEquals(Arrays.asList("b", "a"), new ArrayList<>(m.keySet()));
    }

    @Test
    public void toPlainValue摊平后输出紧凑单行() {
        // 这是迁移中踩到的坑：JsonObject.toString() 是缩进格式，直接 toJson 会出多行空白。
        Object node = JsonUtil.parse("{\"tags\":[\"a\",\"b\"],\"n\":{\"k\":1}}");
        String json = JsonUtil.toJson(JsonUtil.toPlainValue(node));
        assertFalse("不该有换行", json.contains("\n"));
        assertEquals("{\"tags\":[\"a\",\"b\"],\"n\":{\"k\":1}}", json);
    }

    @Test
    public void toPlainValue对标量与null原样返回() {
        assertEquals("x", JsonUtil.toPlainValue("x"));
        assertEquals(Integer.valueOf(5), JsonUtil.toPlainValue(Integer.valueOf(5)));
        assertNull(JsonUtil.toPlainValue(null));
    }

    @Test
    public void toPlainValue对已是plain的结构是恒等() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("k", "v");
        Object out = JsonUtil.toPlainValue(m);
        assertTrue(out == m);

        List<Object> l = new ArrayList<>();
        l.add(1);
        assertTrue(JsonUtil.toPlainValue(l) == l);
    }
}
