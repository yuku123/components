package com.zifang.util.json;

import com.zifang.util.json.annotation.JsonSerialize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 逐字段 {@code nonNull} —— 对齐 Jackson 的 {@code @JsonInclude(NON_NULL)} 挂在单个字段上。
 *
 * <p>与类级 {@code @JsonType(nonNull=true)} 的区别就在这里：类级是"整个类所有字段
 * 都不输出 null"，逐字段是"同类里只有这一个字段这样"。z-kb 的 IngestItemSummary 就是
 * 后者 —— 成功项不该带 error 键，而同类的 documentId 该保留 null 就保留。
 */
class JsonUtilFieldNonNullTest {

    /** 复刻 z-kb IngestItemSummary 的形状：7 个固定键 + 可缺省的 error。 */
    public static class ItemSummary {
        private String requestId;
        private String documentId;
        private String status;

        @JsonSerialize(nonNull = true)
        private String error;

        public String getRequestId() { return requestId; }
        public void setRequestId(String v) { this.requestId = v; }
        public String getDocumentId() { return documentId; }
        public void setDocumentId(String v) { this.documentId = v; }
        public String getStatus() { return status; }
        public void setStatus(String v) { this.status = v; }
        public String getError() { return error; }
        public void setError(String v) { this.error = v; }
    }

    @Test
    void nullNonNullFieldIsOmittedButSiblingsKeepTheirNulls() {
        ItemSummary ok = new ItemSummary();
        ok.setRequestId("r-1");
        ok.setStatus("SUCCESS");

        String json = JsonUtil.toJson(ok);
        assertFalse(json.contains("\"error\""), "成功项不得凭空多出 \"error\":null: " + json);
        // 同类的其他 null 字段必须照常出现 —— 这正是不能用类级 nonNull 的理由
        assertTrue(json.contains("\"documentId\":null"), "同类的 null 字段不该被一起吞掉: " + json);
        assertTrue(json.contains("\"requestId\":\"r-1\""));
        assertTrue(json.contains("\"status\":\"SUCCESS\""));
    }

    @Test
    void nonNullFieldStillSerializesWhenPresent() {
        ItemSummary bad = new ItemSummary();
        bad.setRequestId("r-2");
        bad.setStatus("FAILED");
        bad.setError("拉取超时");

        String json = JsonUtil.toJson(bad);
        assertTrue(json.contains("\"error\":\"拉取超时\""), json);
    }

    @Test
    void fieldNonNullCoexistsWithGlobalSkipNulls() {
        ItemSummary ok = new ItemSummary();
        ok.setStatus("SUCCESS");

        // 全局 toJsonExcludingNulls 下，逐字段标记不该改变别的字段的去留
        String json = JsonUtil.toJsonExcludingNulls(ok);
        assertFalse(json.contains("\"error\""), json);
        assertFalse(json.contains("\"requestId\""), json);
        assertTrue(json.contains("\"status\":\"SUCCESS\""));
    }

    @Test
    void absentAnnotationMeansNullsAreKept() {
        Plain plain = new Plain();
        plain.setA("x");
        String json = JsonUtil.toJson(plain);
        assertTrue(json.contains("\"b\":null"), "没标 nonNull 的字段该照常输出 null: " + json);
    }

    public static class Plain {
        private String a;
        private String b;

        public String getA() { return a; }
        public void setA(String v) { this.a = v; }
        public String getB() { return b; }
        public void setB(String v) { this.b = v; }
    }

    @Test
    void listOfItemsEachOmitsItsOwnErrorKey() {
        ItemSummary ok = new ItemSummary();
        ok.setRequestId("r-1");
        ItemSummary bad = new ItemSummary();
        bad.setRequestId("r-2");
        bad.setError("boom");

        String json = JsonUtil.toJson(java.util.Arrays.asList(ok, bad));
        assertEquals(1, countOccurrences(json, "\"error\""), "只有失败项该带 error 键: " + json);
    }

    private static int countOccurrences(String haystack, String needle) {
        int n = 0;
        int i = haystack.indexOf(needle);
        while (i >= 0) {
            n++;
            i = haystack.indexOf(needle, i + needle.length());
        }
        return n;
    }
}
