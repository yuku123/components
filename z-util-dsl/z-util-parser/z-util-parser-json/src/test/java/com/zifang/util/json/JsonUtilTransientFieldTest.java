package com.zifang.util.json;

import com.zifang.util.json.annotation.JsonSerialize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * transient / static 字段不参与序列化 —— 与 Jackson 默认口径对齐。
 *
 * <p>这条是从 z-bot 迁出来的：CronJob.parsed 靠 transient 才没被写进 jobs.json，
 * 换成 JsonUtil 后 transient 形同虚设，运行时缓存会被灌进配置文件。</p>
 */
public class JsonUtilTransientFieldTest {

    public static class Pojo {
        public String keep = "a";
        public transient String skipped = "b";
        public static String shared = "c";

        @JsonSerialize
        public transient String forcedIn = "d";
    }

    @Test
    public void transientAndStaticAreSkipped() {
        String json = JsonUtil.toJson(new Pojo());
        assertTrue(json.contains("\"keep\":\"a\""), json);
        assertFalse(json.contains("skipped"), "transient 字段不得进 JSON: " + json);
        assertFalse(json.contains("shared"), "static 字段不得进 JSON: " + json);
    }

    @Test
    public void explicitAnnotationOverridesTransient() {
        String json = JsonUtil.toJson(new Pojo());
        assertTrue(json.contains("\"forcedIn\":\"d\""),
                "显式 @JsonSerialize 视为作者有意要它，应盖过 transient: " + json);
    }

    @Test
    public void transientInsideNestedPojoIsAlsoSkipped() {
        String json = JsonUtil.toJson(new Wrapper(new Pojo()));
        assertFalse(json.contains("skipped"), "嵌套里的 transient 同样不得进 JSON: " + json);
    }

    @Test
    public void transientValueIsSkippedEvenWhenNonNull() {
        Pojo p = new Pojo();
        p.skipped = "not-null";
        assertFalse(JsonUtil.toJson(p).contains("skipped"),
                "transient 判据是修饰符不是值 —— 值非空也不该出现: " + JsonUtil.toJson(p));
    }

    public static class Wrapper {
        public Pojo inner;
        public String label = "w";

        public Wrapper(Pojo inner) {
            this.inner = inner;
        }
    }

    @Test
    public void roundTripDoesNotCarryTransientValues() {
        // 单独一个类：transient 字段初值为 null，才能分清"从 JSON 读回来的"和"字段初始化器给的"。
        // 用 Pojo 验不出来 —— new 出来时 skipped 就已经被初始化成 "b" 了。
        WithNullTransient src = new WithNullTransient();
        src.cache = "运行时缓存";
        WithNullTransient back = JsonUtil.fromJson(JsonUtil.toJson(src), WithNullTransient.class);
        assertEquals("a", back.keep);
        assertNull(back.cache, "transient 的值不该跟着 JSON 回来");
    }

    public static class WithNullTransient {
        public String keep = "a";
        public transient String cache;
    }
}
