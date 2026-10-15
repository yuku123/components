package com.zifang.util.serialize.formats.json;

import com.zifang.util.serialize.annotation.FieldType;
import com.zifang.util.serialize.annotation.ZField;
import com.zifang.util.serialize.annotation.ZMessage;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
 * JsonTranscoder 在去 Jackson 之后的行为契约。
 * <p>迁移前用差分探针与旧实现逐项比对过：往返字段 9/9 一致，缩进排版与 Map 键序有差异但都不在契约内。
 * 这里把真正需要守住的语义钉住。</p>
 */
public class JsonTranscoderTypeTest {

    public enum Color { RED, GREEN }

    @ZMessage(id = 400, name = "test.Outer")
    public static class Outer {
        @ZField(id = 1, type = FieldType.VARINT) public long id;
        @ZField(id = 2, type = FieldType.LENGTH_DELIMITED) public int count;
        @ZField(id = 3, type = FieldType.LENGTH_DELIMITED) public boolean active;
        @ZField(id = 4, type = FieldType.LENGTH_DELIMITED) public Double ratio;
        @ZField(id = 5, type = FieldType.LENGTH_DELIMITED) public BigDecimal price;
        @ZField(id = 6, type = FieldType.LENGTH_DELIMITED) public BigInteger big;
        @ZField(id = 7, type = FieldType.LENGTH_DELIMITED) public Color color;
        @ZField(id = 8, type = FieldType.VARINT) public List<String> tags;
        @ZField(id = 9, type = FieldType.LENGTH_DELIMITED) public Map<String, Integer> attrs;
        @ZField(id = 10, type = FieldType.LENGTH_DELIMITED) public Inner inner;
        @ZField(id = 11, type = FieldType.LENGTH_DELIMITED) public String text;

        public Outer() {
            tags = new ArrayList<>();
            attrs = new LinkedHashMap<>();
        }
    }

    @ZMessage(id = 401, name = "test.Inner")
    public static class Inner {
        @ZField(id = 1, type = FieldType.LENGTH_DELIMITED) public String label;
        @ZField(id = 2, type = FieldType.VARINT) public long value;

        public Inner() {
        }
    }

    private static Outer sample() {
        Outer o = new Outer();
        o.id = 9007199254740993L;
        o.count = 7;
        o.active = true;
        o.ratio = 1.5D;
        o.price = new BigDecimal("12.3400");
        o.big = new BigInteger("123456789012345678901234567890");
        o.color = Color.GREEN;
        o.tags.addAll(Arrays.asList("a", "b"));
        o.attrs.put("size", 42);
        Inner in = new Inner();
        in.label = "in";
        in.value = 9L;
        o.inner = in;
        o.text = "带\"引号\"和\n换行";
        return o;
    }

    @Test
    public void toCompactJson真的紧凑() throws IOException {
        // 旧实现这里是个 bug：MAPPER 全局开了 INDENT_OUTPUT，toCompactJson 与 toJson 输出一模一样。
        String compact = JsonTranscoder.toCompactJson(sample());
        assertFalse("紧凑输出不该换行", compact.contains("\n"));
        assertTrue(compact.startsWith("{\"_id\":400,"));
    }

    @Test
    public void toJson是缩进格式() throws IOException {
        String pretty = JsonTranscoder.toJson(sample());
        assertTrue("缩进输出该有多行", pretty.contains("\n"));
        assertTrue(pretty.contains("\"_type\""));
    }

    @Test
    public void 往返保持各类型取值() throws IOException {
        Outer o = sample();
        Outer back = JsonTranscoder.fromJson(JsonTranscoder.toCompactJson(o), Outer.class);

        assertEquals(9007199254740993L, back.id);
        assertEquals(7, back.count);
        assertTrue(back.active);
        assertEquals(1.5D, back.ratio, 0.0001D);
        // BigDecimal 的 scale 走一趟 JSON 会被抹掉（解析器把字面量归一成最短表示）。
        // 旧 Jackson 实现同样如此（它输出 12.34），故此处按数值比而非 equals 比 scale。
        assertEquals(0, new BigDecimal("12.3400").compareTo(back.price));
        assertEquals(new BigInteger("123456789012345678901234567890"), back.big);
        assertEquals(Color.GREEN, back.color);
        assertEquals(Arrays.asList("a", "b"), back.tags);
        assertEquals(Integer.valueOf(42), back.attrs.get("size"));
        assertEquals("in", back.inner.label);
        assertEquals(9L, back.inner.value);
        assertEquals("带\"引号\"和\n换行", back.text);
    }

    @Test
    public void BigDecimal字段不再解码失败() throws IOException {
        // 旧实现没有 BigDecimal 分支，会掉到 asText 兜底产出 String，
        // 再 set 进 BigDecimal 字段时抛 IllegalArgumentException —— 带 BigDecimal 的消息根本解不回来。
        Outer o = new Outer();
        o.price = new BigDecimal("99.99");
        Outer back = JsonTranscoder.fromJson(JsonTranscoder.toCompactJson(o), Outer.class);
        assertEquals(new BigDecimal("99.99"), back.price);
    }

    @Test
    public void 窄整数字段不会因解析器返回宽类型而赋值失败() throws IOException {
        // 解析器按字面量大小返回 Integer/Long/BigDecimal，直接 set 进 int 字段会抛类型不匹配。
        Outer o = new Outer();
        o.id = 5L;
        o.count = 3;
        Outer back = JsonTranscoder.fromJson(JsonTranscoder.toCompactJson(o), Outer.class);
        assertEquals(3, back.count);
        assertEquals(5L, back.id);
    }

    @Test
    public void 集合里的null元素保留() throws IOException {
        Outer o = new Outer();
        o.tags.add("x");
        o.tags.add(null);
        Outer back = JsonTranscoder.fromJson(JsonTranscoder.toCompactJson(o), Outer.class);
        assertEquals(2, back.tags.size());
        assertNull(back.tags.get(1));
    }

    @Test
    public void 缺失字段保持默认值() throws IOException {
        Outer back = JsonTranscoder.fromJson("{\"_id\":400,\"1\":1}", Outer.class);
        assertEquals(1L, back.id);
        assertNull(back.text);
        assertNull(back.inner);
        assertTrue(back.tags.isEmpty());
    }

    @Test
    public void 字段名清单只列非null字段() throws IOException {
        Outer o = new Outer();
        o.id = 1L;
        String json = JsonTranscoder.toCompactJson(o);
        // text/inner 为 null，不该进 _fields
        assertFalse(json.contains("\"text\""));
        assertFalse(json.contains("\"inner\""));
        assertTrue(json.contains("\"id\""));
    }
}
