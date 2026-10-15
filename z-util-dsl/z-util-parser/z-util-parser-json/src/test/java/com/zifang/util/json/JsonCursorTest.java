package com.zifang.util.json;

import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JsonCursor 的契约：语义对齐 Jackson 的 JsonNode.path(...) 家族，
 * 迁移 Jackson 树导航代码时不能改变断言语义。每条判据都注明对应的 Jackson 行为。
 */
public class JsonCursorTest {

    private static JsonCursor cur(String json) {
        return JsonCursor.of(JsonUtil.parse(json));
    }

    // ===================== 导航容错 =====================

    @Test
    public void 缺失键下钻得到missing而非异常() {
        // Jackson: node.path("a").path("b") → MissingNode，不抛
        JsonCursor c = cur("{\"a\":{}}").path("a").path("b");
        assertTrue(c.isMissing());
        assertEquals("", c.asText());
        assertEquals(0, c.asInt(0));
        assertEquals(7, c.asInt(7));
        assertFalse(c.isNull());
    }

    @Test
    public void 非对象上取字段得到missing() {
        // Jackson: 整数节点.path("x") → MissingNode
        assertTrue(cur("42").path("x").isMissing());
        assertTrue(cur("[1,2]").path("x").isMissing());
        assertTrue(cur("\"s\"").path("x").isMissing());
        assertTrue(JsonCursor.missing().path("deeper").isMissing());
    }

    @Test
    public void 数组at越界或非数组得到missing() {
        assertTrue(cur("[1,2]").at(5).isMissing());
        assertTrue(cur("[1,2]").at(-1).isMissing());
        assertTrue(cur("{}").at(0).isMissing());
        assertEquals(2, cur("[1,2]").at(1).asInt(0));
    }

    @Test
    public void null值与缺失可区分() {
        // Jackson: {"k":null}.path("k") → NullNode (isNull=true, isMissing=false)
        JsonCursor nullVal = cur("{\"k\":null}").path("k");
        assertTrue(nullVal.isNull());
        assertFalse(nullVal.isMissing());

        JsonCursor absent = cur("{}").path("k");
        assertTrue(absent.isMissing());
        assertFalse(absent.isNull());
    }

    // ===================== 形状判断 =====================

    @Test
    public void 形状判断与Jackson一致() {
        assertTrue(cur("{\"a\":1}").isObject());
        assertTrue(cur("[1]").isArray());
        assertTrue(cur("42").isScalar());
        assertTrue(cur("true").isScalar());
        assertTrue(cur("\"s\"").isScalar());
        assertFalse(cur("[1]").isObject());
        assertFalse(cur("{\"a\":1}").isArray());
        // missing 上除 isMissing 外全 false
        JsonCursor m = JsonCursor.missing();
        assertFalse(m.isObject());
        assertFalse(m.isArray());
        assertFalse(m.isScalar());
        assertFalse(m.isNull());
    }

    @Test
    public void has与size() {
        assertTrue(cur("{\"a\":1}").has("a"));
        assertTrue(cur("{\"a\":null}").has("a"));
        assertFalse(cur("{\"a\":1}").has("b"));
        assertFalse(cur("[1]").has("a"));
        assertEquals(2, cur("{\"a\":1,\"b\":2}").size());
        assertEquals(3, cur("[1,2,3]").size());
        assertEquals(0, cur("42").size());
        assertEquals(0, JsonCursor.missing().size());
    }

    // ===================== 取值 =====================

    @Test
    public void asText与Jackson对齐() {
        assertEquals("s", cur("\"s\"").asText());
        assertEquals("42", cur("42").asText());
        assertEquals("true", cur("true").asText());
        // 容器/missing = ""（对齐 Jackson）
        assertEquals("", cur("{\"a\":1}").asText());
        assertEquals("", cur("[1]").asText());
        assertEquals("", JsonCursor.missing().asText());
        // 与 Jackson 的一处刻意差异：本库把顶层 "null" 解析成 Java null（JsonUtil.parse 的模型），
        // of(null) 归入 missing，asText 得 "" 而非 Jackson NullNode 的 "null"。
        // 键位上的 null（{"k":null}）不受影响，见 null值与缺失可区分。
        assertEquals("", cur("null").asText());
    }

    @Test
    public void asIntAsLongAsDouble带默认值() {
        assertEquals(42, cur("42").asInt(0));
        assertEquals(42, cur("\"42\"").asInt(0));
        assertEquals(0, cur("\"abc\"").asInt(0));
        assertEquals(0, cur("{}").asInt(0));

        long big = 9_007_199_254_740_993L; // 2^53+1，JS 不安全但 Java 没问题
        assertEquals(big, cur(String.valueOf(big)).asLong(0));

        assertEquals(1.5, cur("1.5").asDouble(0), 1e-9);
        assertEquals(2.5, cur("\"2.5\"").asDouble(0), 1e-9);
        assertEquals(-1.0, cur("\"x\"").asDouble(-1.0), 1e-9);
    }

    @Test
    public void asBoolean带默认值() {
        assertTrue(cur("true").asBoolean(false));
        assertFalse(cur("false").asBoolean(true));
        assertTrue(cur("\"true\"").asBoolean(false));
        assertFalse(cur("\"yes\"").asBoolean(false));
        assertFalse(JsonCursor.missing().asBoolean(false));
        assertTrue(JsonCursor.missing().asBoolean(true));
    }

    // ===================== 遍历 =====================

    @Test
    public void elements遍历数组() {
        JsonCursor arr = cur("[{\"name\":\"a\"},{\"name\":\"b\"}]");
        assertTrue(arr.isArray());
        assertEquals(2, arr.elements().size());
        assertEquals("a", arr.elements().get(0).path("name").asText());
        assertEquals("b", arr.elements().get(1).path("name").asText());
        // 非数组 elements 为空，不抛
        assertTrue(cur("{}").elements().isEmpty());
        assertTrue(JsonCursor.missing().elements().isEmpty());
    }

    @Test
    public void fieldNames保文档顺序() {
        JsonCursor obj = cur("{\"b\":1,\"a\":2}");
        assertEquals("[b, a]", obj.fieldNames().toString());
        assertTrue(cur("[1]").fieldNames().isEmpty());
    }

    // ===================== 还原与入口 =====================

    @Test
    public void value还原原始节点() {
        Object parsed = JsonUtil.parse("{\"a\":1}");
        assertEquals(parsed, JsonCursor.of(parsed).value());
        assertEquals("x", cur("\"x\"").value());
        assertEquals(null, JsonCursor.missing().value());
    }

    @Test
    public void of能把plainMap当树遍历() {
        // 入口不限于 parse 结果：plain 的 Map/List 树同样可游标（JsonObject 内部也是 Map 存键）
        java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
        java.util.Map<String, Object> inner = new java.util.LinkedHashMap<>();
        inner.put("k", "v");
        m.put("n", inner);
        m.put("arr", java.util.Arrays.asList(1, 2));
        JsonCursor c = JsonCursor.of(m);
        assertTrue(c.isObject());
        assertEquals("v", c.path("n").path("k").asText());
        assertTrue(c.path("arr").isArray());
        assertEquals(2, c.path("arr").at(1).asInt(0));
        assertEquals("[n, arr]", c.fieldNames().toString());
    }

    @Test
    public void 端到端模拟Jackson树导航测试的典型形状() {
        // 迁移自 Jackson readTree 断言的典型写法：下钻 + 判形状 + 取值
        JsonCursor root = cur("{\"jsonrpc\":\"2.0\",\"result\":{\"tools\":[{\"name\":\"t1\",\"inputSchema\":{\"type\":\"object\"}}]},\"id\":1}");
        assertEquals("2.0", root.path("jsonrpc").asText());
        assertTrue(root.has("result"));
        JsonCursor tools = root.path("result").path("tools");
        assertTrue(tools.isArray());
        assertEquals(1, tools.size());
        assertEquals("t1", tools.at(0).path("name").asText());
        assertEquals("object", tools.at(0).path("inputSchema").path("type").asText());
        // 缺失路径安全下钻
        assertFalse(root.path("error").path("code").isMissing() == false);
        assertTrue(root.path("error").path("code").isMissing());
    }
}
