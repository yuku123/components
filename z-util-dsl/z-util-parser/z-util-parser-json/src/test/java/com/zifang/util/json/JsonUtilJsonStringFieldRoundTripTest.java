package com.zifang.util.json;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 「字段值本身是 JSON 字符串」的实体必须能往返。
 *
 * <p><b>为什么要有这条独立判据。</b>
 * 有一条已上线的特性把 {@code AgentExecution} 额外镜像一份成工作区的
 * {@code runs/{runId}/run.json}，读的时候优先从它取。它的写法是
 * {@code JsonUtil.toJsonPretty(entity)} → 落盘 → {@code JsonUtil.fromJson(...)}。
 *
 * <p>这条路曾经<b>从来没有成功读过</b>，而系统一片绿：读取侧把异常 catch 掉只
 * {@code log.warn} 再 {@code return null}（语义是"文件缺失，回落 DB"），
 * 于是"写成功 / 读回落 DB / 零告警"——一个从未生效的机制留在代码里，
 * 让人以为它生效过。两处根因都在 {@link JsonUtil}：
 *
 * <ol>
 *   <li><b>{@code toJsonPretty} 把 JSON 字符串字段重新缩进</b>：旧实现的漂亮化是
 *       纯文本重排，不认 JSON 语法，于是字段值 {@code "[]"} 被写成
 *       {@code "[\n    \n  ]"}，落盘即非法 JSON ⇒ 读回来直接解析失败。</li>
 *   <li><b>{@code LocalDateTime} 落成 {@code {"date":{...},"time":{...}}}</b>：
 *       序列化分派链缺 {@code java.time} 分支，这些类型掉进反射私有字段的兜底路径。</li>
 * </ol>
 *
 * <p>本类是<b>独立判据</b>：不依赖任何既有用例顺带过，而是把上面两条各自钉死。
 * 既有测试没有覆盖到"字段值是 JSON 字符串"这个形状 —— 那正是坏掉的那条路径。
 *
 * <p>断言一律用字面量，不用 {@code value.toString()} 推导期望值 —— 那样断言会与
 * 实现同源、永远为真，失去牙齿。
 */
class JsonUtilJsonStringFieldRoundTripTest {

    /**
     * 承载「JSON 字符串字段」+ java.time 字段的实体。
     *
     * <p>形状照真实用法来：字段是 {@code String} 但内容是 JSON 文本（这是
     * 「把结构化数据塞进字符串列」这一常见做法的真实样子），另有
     * {@code LocalDateTime} / {@code LocalDate} 字段。
     */
    public static class RunEntity {
        private Long id;
        private String status;
        /** 字段值本身是 JSON 字符串（数组形态）。 */
        private String stepsJson;
        /** 字段值本身是 JSON 字符串（对象形态，且值里带逗号与冒号）。 */
        private String resultJson;
        private LocalDateTime startedAt;
        private LocalDate day;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getStepsJson() { return stepsJson; }
        public void setStepsJson(String stepsJson) { this.stepsJson = stepsJson; }
        public String getResultJson() { return resultJson; }
        public void setResultJson(String resultJson) { this.resultJson = resultJson; }
        public LocalDateTime getStartedAt() { return startedAt; }
        public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
        public LocalDate getDay() { return day; }
        public void setDay(LocalDate day) { this.day = day; }
    }

    /** 空数组形态的 JSON 字符串字段 —— 坏掉时正是它被缩成 {@code "[\n    \n  ]"}。 */
    private static final String STEPS_JSON = "[]";
    /** 对象形态，且内层字符串里带逗号与冒号 —— 漂亮化最容易在这里动手脚。 */
    private static final String RESULT_JSON = "{\"k\":\"v\",\"n\":[1,2],\"s\":\"a,b:c\"}";

    private static RunEntity entity() {
        RunEntity e = new RunEntity();
        e.setId(10L);
        e.setStatus("RUNNING");
        e.setStepsJson(STEPS_JSON);
        e.setResultJson(RESULT_JSON);
        e.setStartedAt(LocalDateTime.of(2026, 10, 22, 13, 5, 9));
        e.setDay(LocalDate.of(2026, 10, 22));
        return e;
    }

    // ==================== toJsonPretty 不再重新缩进 JSON 字符串字段 ====================

    /**
     * 判据一：{@code toJsonPretty} 不得改动「字段值本身是 JSON 字符串」的内容。
     *
     * <p>断言的是<b>字面量原样出现</b>，而不是"读回来相等" ——
     * 后者对某些损坏形态不敏感（值被改了但仍能解析）。
     */
    @Test
    void 漂亮化后JSON字符串字段逐字不变() {
        String pretty = JsonUtil.toJsonPretty(entity());

        // 空数组形态：坏掉时是 "[\n    \n  ]"，这里必须是 "[]"
        assertTrue(pretty.contains("\"stepsJson\": \"[]\""),
                "空数组形态的 JSON 字符串字段被重新缩进了:\n" + pretty);

        // 对象形态：内层引号被转义成 \" 是对的（那才是合法 JSON 字符串字面量），
        // 但结构符号不得被当语法重排 —— 逐字比对转义后的形态
        assertTrue(pretty.contains("\"resultJson\": \"{\\\"k\\\":\\\"v\\\",\\\"n\\\":[1,2],\\\"s\\\":\\\"a,b:c\\\"}\""),
                "对象形态的 JSON 字符串字段被重新缩进了:\n" + pretty);

        // 反面钉死：不得出现被重排过的痕迹
        assertTrue(!pretty.contains("[\n"), "JSON 字符串字段里出现了换行，说明漂亮化动了内容:\n" + pretty);
    }

    /** 判据二：漂亮化后的产物本身必须是<b>合法 JSON</b>（能被解析回来）。 */
    @Test
    void 漂亮化产物是合法JSON() {
        String pretty = JsonUtil.toJsonPretty(entity());
        // 解析失败会抛异常；这里只断言"解析得动且是个对象"
        Object parsed = JsonUtil.parse(pretty);
        assertNotNull(parsed, "漂亮化产物解析为 null:\n" + pretty);
    }

    // ==================== 含 JSON 字符串字段的实体能往返 ====================

    /**
     * 判据三：<b>含 JSON 字符串字段的实体</b>能往返 —— 这是本卡最核心的一条。
     *
     * <p>走的是线上那条一模一样的路径：{@code toJsonPretty} → 文本 → {@code fromJson}。
     */
    @Test
    void 含JSON字符串字段的实体能往返() {
        RunEntity origin = entity();
        RunEntity back = JsonUtil.fromJson(JsonUtil.toJsonPretty(origin), RunEntity.class);

        assertNotNull(back, "往返后为 null");
        assertEquals(Long.valueOf(10L), back.getId());
        assertEquals("RUNNING", back.getStatus());
        assertEquals(STEPS_JSON, back.getStepsJson(), "JSON 字符串字段（数组形态）没能原样回来");
        assertEquals(RESULT_JSON, back.getResultJson(), "JSON 字符串字段（对象形态）没能原样回来");
    }

    /** 判据四：紧凑形态（{@code toJson}，非漂亮化）同样要能往返 —— 两条写盘路径都得成立。 */
    @Test
    void 含JSON字符串字段的实体紧凑形态也能往返() {
        RunEntity origin = entity();
        RunEntity back = JsonUtil.fromJson(JsonUtil.toJson(origin), RunEntity.class);

        assertNotNull(back, "往返后为 null");
        assertEquals(STEPS_JSON, back.getStepsJson());
        assertEquals(RESULT_JSON, back.getResultJson());
    }

    // ==================== java.time 字段能往返 ====================

    /**
     * 判据五：{@code LocalDateTime} / {@code LocalDate} 能往返，且落盘形态是 ISO 文本。
     *
     * <p>坏掉时它们落成 {@code {"date":{...},"time":{...}}}，反序列化不回去
     * ⇒ {@code RuntimeException: deserializePojo failed}。
     */
    @Test
    void javaTime字段能往返且落盘是ISO文本() {
        RunEntity origin = entity();
        String pretty = JsonUtil.toJsonPretty(origin);

        // 落盘形态：ISO-8601 文本，不是 {date,time} 结构
        assertTrue(pretty.contains("\"startedAt\": \"2026-10-22T13:05:09\""),
                "LocalDateTime 落盘不是 ISO 文本:\n" + pretty);
        assertTrue(pretty.contains("\"day\": \"2026-10-22\""),
                "LocalDate 落盘不是 ISO 文本:\n" + pretty);

        RunEntity back = JsonUtil.fromJson(pretty, RunEntity.class);
        assertNotNull(back);
        assertEquals(LocalDateTime.of(2026, 10, 22, 13, 5, 9), back.getStartedAt());
        assertEquals(LocalDate.of(2026, 10, 22), back.getDay());
    }
}
