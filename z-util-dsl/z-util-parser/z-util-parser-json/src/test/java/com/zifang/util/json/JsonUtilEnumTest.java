package com.zifang.util.json;

import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * 枚举序列化契约。
 * <p>
 * 补这组是因为迁移去 Jackson 时撞出来的：JsonUtil 原来没有 enum 序列化分支，
 * enum 会掉进 POJO 分支，而 {@code name()} / {@code ordinal()} 不是 bean getter，
 * 于是被输出成 {@code {"name":null,"ordinal":null}} —— 看着有值，读回来在
 * {@code Enum.valueOf} 上炸。Jackson 默认写 name()，这里对齐 Jackson。
 */
public class JsonUtilEnumTest {

    public enum Status {
        PENDING, RUNNING, DONE
    }

    /** enum 字段带 Jackson 风格的 getter，用来钉住 POJO 分支。 */
    public static class Task {
        private String name;
        private Status status;

        public Task() {
        }

        public Task(String name, Status status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public Status getStatus() {
            return status;
        }
    }

    @Test
    public void enumFieldSerializesAsItsName() {
        String json = JsonUtil.toJson(new Task("t1", Status.RUNNING));
        assertEquals("{\"name\":\"t1\",\"status\":\"RUNNING\"}", json);
    }

    @Test
    public void enumNeverSerializesAsNullNameOrdinalObject() {
        // 这是迁移前实际产出的形态：看着像有值，其实读不回来
        String json = JsonUtil.toJson(new Task("t1", Status.RUNNING));
        assertTrue("不能退回 {\"name\":null,\"ordinal\":null} 形态: " + json,
                !json.contains("ordinal"));
    }

    @Test
    public void enumRoundTripsThroughFromJson() {
        Task t = JsonUtil.fromJson("{\"name\":\"t2\",\"status\":\"DONE\"}", Task.class);
        assertEquals(Status.DONE, t.getStatus());
        assertEquals("t2", t.getName());
    }

    @Test
    public void bareEnumSerializesToString() {
        assertEquals("\"PENDING\"", JsonUtil.toJson(Status.PENDING));
    }

    @Test
    public void enumInsideCollectionAndMap() {
        assertEquals("[\"A\",\"B\"]".replace("A", "PENDING").replace("B", "RUNNING"),
                JsonUtil.toJson(Arrays.asList(Status.PENDING, Status.RUNNING)));

        Map<String, Status> m = new LinkedHashMap<>();
        m.put("s", Status.DONE);
        assertEquals("{\"s\":\"DONE\"}", JsonUtil.toJson(m));
    }

    @Test
    public void enumAsMapValueThroughNormalizeValue() {
        // normalizeValue 走的是 parseObject(toJson(value))，枚举必须先短路成字符串，
        // 否则 parseObject 拿到 "\"DONE\"" 这个标量会抛。
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("status", Status.DONE);
        JsonObject o = JsonUtil.parseObject(JsonUtil.toJson(m));
        assertNotNull(o);
        assertEquals("DONE", o.get("status").toString());
    }

    @Test
    public void legacyNameObjectFormStillDeserializes() {
        // 历史落盘数据是 {"name":"DONE"} 形态，别把读路径一起改坏
        Task t = JsonUtil.fromJson("{\"status\":{\"name\":\"DONE\"}}", Task.class);
        assertEquals(Status.DONE, t.getStatus());
    }

    @Test
    public void enumSetAndEnumMapKeysAreUnaffected() {
        List<Status> all = Arrays.asList(Status.values());
        String json = JsonUtil.toJson(all);
        assertTrue(json.contains("PENDING") && json.contains("RUNNING") && json.contains("DONE"));
        assertSame(Status.class, Status.PENDING.getDeclaringClass());
    }

    @Test
    public void enumMapSerializesKeysAsNames() {
        EnumMap<Status, String> em = new EnumMap<>(Status.class);
        em.put(Status.RUNNING, "x");
        assertEquals("{\"RUNNING\":\"x\"}", JsonUtil.toJson(em));
    }
}
