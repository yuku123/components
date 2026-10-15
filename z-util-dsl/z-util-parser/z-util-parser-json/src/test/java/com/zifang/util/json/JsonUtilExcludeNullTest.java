package com.zifang.util.json;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * toJsonExcludingNulls 的契约测试。
 * <p>
 * 语义对齐 Jackson 的 JsonInclude.Include.NON_NULL：只剔除 POJO 字段与 Map 条目的 null，
 * 集合/数组元素里的 null 仍然输出。这一条是刻意与"递归剔除"划清界限，用例钉死它。
 */
public class JsonUtilExcludeNullTest {

    public static class Dto {
        private String name;
        private Integer count;
        private List<String> tags;

        public String getName() { return name; }
        public void setName(String v) { name = v; }
        public Integer getCount() { return count; }
        public void setCount(Integer v) { count = v; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> v) { tags = v; }
    }

    @Test
    public void toJson默认仍输出null字段() {
        Dto d = new Dto();
        d.setName("a");
        assertEquals("{\"name\":\"a\",\"count\":null,\"tags\":null}", JsonUtil.toJson(d));
    }

    @Test
    public void 剔除POJO的null字段() {
        Dto d = new Dto();
        d.setName("a");
        assertEquals("{\"name\":\"a\"}", JsonUtil.toJsonExcludingNulls(d));
    }

    @Test
    public void 全null字段的POJO输出空对象() {
        assertEquals("{}", JsonUtil.toJsonExcludingNulls(new Dto()));
    }

    @Test
    public void 剔除Map中值为null的条目() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("n", 1);
        m.put("s", null);
        m.put("t", "v");
        assertEquals("{\"n\":1,\"t\":\"v\"}", JsonUtil.toJsonExcludingNulls(m));
    }

    @Test
    public void 全null值的Map输出空对象() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("a", null);
        m.put("b", null);
        assertEquals("{}", JsonUtil.toJsonExcludingNulls(m));
    }

    @Test
    public void 集合元素里的null仍然输出() {
        // 与 Jackson NON_NULL 一致：它只作用于 bean 属性与 map entry，不作用于数组元素。
        assertEquals("[\"a\",null,\"b\"]", JsonUtil.toJsonExcludingNulls(Arrays.asList("a", null, "b")));
    }

    @Test
    public void 嵌套结构中的null一并剔除() {
        Map<String, Object> inner = new LinkedHashMap<>();
        inner.put("keep", "v");
        inner.put("drop", null);

        Dto d = new Dto();
        d.setName("a");
        d.setTags(Arrays.asList("x", null));

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("dto", d);
        root.put("inner", inner);
        root.put("nil", null);

        assertEquals("{\"dto\":{\"name\":\"a\",\"tags\":[\"x\",null]},\"inner\":{\"keep\":\"v\"}}",
                JsonUtil.toJsonExcludingNulls(root));
    }

    @Test
    public void 开关用完即复位不影响后续toJson() {
        Dto d = new Dto();
        d.setName("a");
        JsonUtil.toJsonExcludingNulls(d);
        assertEquals("{\"name\":\"a\",\"count\":null,\"tags\":null}", JsonUtil.toJson(d));
    }

    @Test
    public void 开关对嵌套调用生效且顶层null仍返回字面量() {
        assertEquals("null", JsonUtil.toJsonExcludingNulls(null));
    }
}
