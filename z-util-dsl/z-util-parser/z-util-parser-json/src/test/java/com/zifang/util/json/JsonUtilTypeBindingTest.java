package com.zifang.util.json;

import com.zifang.util.json.annotation.JsonDeserialize;
import com.zifang.util.json.annotation.JsonSerialize;
import com.zifang.util.json.annotation.JsonType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@code @JsonType}（类级命名策略 / nonNull / exclude）、
 * {@code @JsonDeserialize(alias/ignore)}、构造器绑定（无无参构造器的不可变对象）。
 */
class JsonUtilTypeBindingTest {

    // ---- 测试夹具 ----

    @JsonType(naming = JsonType.Naming.SNAKE_CASE)
    static class SnakeDto {
        private String promptTokens;
        private int maxRetries;
        private String plain;

        public String getPromptTokens() { return promptTokens; }
        public void setPromptTokens(String v) { this.promptTokens = v; }
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int v) { this.maxRetries = v; }
        public String getPlain() { return plain; }
        public void setPlain(String v) { this.plain = v; }
    }

    @JsonType(naming = JsonType.Naming.KEBAB_CASE)
    static class KebabDto {
        private String modelName;
        public String getModelName() { return modelName; }
        public void setModelName(String v) { this.modelName = v; }
    }

    @JsonType(naming = JsonType.Naming.SNAKE_CASE)
    static class SnakeImmutable {
        private final String toolCallId;
        private final List<String> parts;
        private String optional;

        SnakeImmutable(String toolCallId, List<String> parts, String optional) {
            this.toolCallId = toolCallId;
            this.parts = parts;
            this.optional = optional;
        }
        String toolCallId() { return toolCallId; }
        List<String> parts() { return parts; }
        String optional() { return optional; }
    }

    static class TwoCtorNoNoArg {
        TwoCtorNoNoArg(String a) { }
        TwoCtorNoNoArg(String a, String b) { }
    }

    static class AliasDto {
        @JsonDeserialize(alias = {"old_name", "legacy"})
        private String name;

        public String getName() { return name; }
        public void setName(String v) { this.name = v; }
    }

    static class IgnoreRead {
        @JsonDeserialize(ignore = true)
        private String secret;
        private String visible;

        public String getSecret() { return secret; }
        public String getVisible() { return visible; }
        public void setVisible(String v) { this.visible = v; }
    }

    @JsonType(nonNull = true)
    static class NonNullDto {
        private String a;
        private String b;
        public String getA() { return a; }
        public void setA(String v) { this.a = v; }
        public String getB() { return b; }
        public void setB(String v) { this.b = v; }
    }

    @JsonType(exclude = {"internal"})
    static class ExcludedDto {
        private String pub;
        private String internal;
        public String getPub() { return pub; }
        public void setPub(String v) { this.pub = v; }
        public String getInternal() { return internal; }
        public void setInternal(String v) { this.internal = v; }
    }

    @JsonType(naming = JsonType.Naming.SNAKE_CASE, nonNull = true)
    static class NamingNonNullDto {
        private String firstName;
        private String middleName;
        public String getFirstName() { return firstName; }
        public void setFirstName(String v) { this.firstName = v; }
        public String getMiddleName() { return middleName; }
        public void setMiddleName(String v) { this.middleName = v; }
    }

    // ---- 命名策略 ----

    @Test
    void snakeCaseNamingAppliesOnBothDirections() {
        SnakeDto dto = new SnakeDto();
        dto.setPromptTokens("p");
        dto.setMaxRetries(3);
        dto.setPlain("x");
        String json = JsonUtil.toJson(dto);
        assertTrue(json.contains("\"prompt_tokens\":\"p\""), json);
        assertTrue(json.contains("\"max_retries\":3"), json);
        assertTrue(json.contains("\"plain\":\"x\""), json);

        SnakeDto back = JsonUtil.fromJson("{\"prompt_tokens\":\"p\",\"max_retries\":3,\"plain\":\"x\"}", SnakeDto.class);
        assertEquals("p", back.getPromptTokens());
        assertEquals(3, back.getMaxRetries());
        assertEquals("x", back.getPlain());
    }

    @Test
    void kebabCaseNamingForYamlStyleKeys() {
        KebabDto dto = new KebabDto();
        dto.setModelName("gpt");
        String json = JsonUtil.toJson(dto);
        assertTrue(json.contains("\"model-name\":\"gpt\""), json);
        assertEquals("gpt", JsonUtil.fromJson("{\"model-name\":\"gpt\"}", KebabDto.class).getModelName());
    }

    @JsonType(naming = JsonType.Naming.SNAKE_CASE)
    static class AcronymDto {
        private String promptAPI;
        private String APIKey;
        private String ID;

        public String getPromptAPI() { return promptAPI; }
        public void setPromptAPI(String v) { this.promptAPI = v; }
        public String getAPIKey() { return APIKey; }
        public String getID() { return ID; }
    }

    @Test
    void acronymRunsGetSingleSeparator() {
        AcronymDto dto = new AcronymDto();
        dto.setPromptAPI("p");
        String json = JsonUtil.toJson(dto);
        // 缩写词整段不拆：APIKey → api_key 而不是 a_p_i_key
        assertTrue(json.contains("\"prompt_api\":\"p\""), json);
        assertTrue(json.contains("\"api_key\""), json);
        assertTrue(json.contains("\"id\""), json);
    }

    @Test
    void fieldExplicitNameBeatsClassNaming() {
        @JsonType(naming = JsonType.Naming.SNAKE_CASE)
        class Dto {
            @JsonSerialize(name = "fixed_name")
            private String userName;
            public String getUserName() { return userName; }
            public void setUserName(String v) { this.userName = v; }
        }
        Dto d = new Dto();
        d.setUserName("u");
        assertTrue(JsonUtil.toJson(d).contains("\"fixed_name\":\"u\""),
                "字段显式 name 应盖过类级策略");
    }

    // ---- 别名 ----

    @Test
    void aliasFeedsFieldWhenPrimaryNameAbsent() {
        assertEquals("v", JsonUtil.fromJson("{\"name\":\"v\"}", AliasDto.class).getName());
        assertEquals("v", JsonUtil.fromJson("{\"old_name\":\"v\"}", AliasDto.class).getName());
        assertEquals("v", JsonUtil.fromJson("{\"legacy\":\"v\"}", AliasDto.class).getName());
    }

    @Test
    void aliasIsReadDirectionOnly() {
        AliasDto d = new AliasDto();
        d.setName("v");
        String json = JsonUtil.toJson(d);
        assertTrue(json.contains("\"name\":\"v\""), json);
        assertFalse(json.contains("old_name"), json);
    }

    // ---- 反序列化 ignore ----

    @Test
    void deserializeIgnoreBlocksBackfillOnly() {
        IgnoreRead r = JsonUtil.fromJson("{\"secret\":\"s\",\"visible\":\"v\"}", IgnoreRead.class);
        assertNull(r.getSecret(), "ignore 字段不应被回填");
        assertEquals("v", r.getVisible());
    }

    // ---- nonNull ----

    @Test
    void classNonNullSkipsNullFields() {
        NonNullDto d = new NonNullDto();
        d.setA("x");
        String json = JsonUtil.toJson(d);
        assertTrue(json.contains("\"a\":\"x\""), json);
        assertFalse(json.contains("\"b\""), json);
    }

    @Test
    void globalExcludingNullsStillAppliesToPlainClasses() {
        Plain p = new Plain();
        p.only = "1";
        // 无 @JsonType(nonNull) 的类不受影响，仍由 toJsonExcludingNulls 控制
        assertFalse(JsonUtil.toJson(p).isEmpty());
        assertTrue(JsonUtil.toJsonExcludingNulls(p).contains("only"));
        assertTrue(JsonUtil.toJson(p).contains("\"x\":null"));
    }

    static class Plain {
        private String only;
        private String x;
        public String getOnly() { return only; }
        public void setOnly(String v) { this.only = v; }
        public String getX() { return x; }
        public void setX(String v) { this.x = v; }
    }

    // ---- exclude ----

    @Test
    void classExcludeWorksBothDirections() {
        ExcludedDto d = new ExcludedDto();
        d.setPub("p");
        d.setInternal("i");
        String json = JsonUtil.toJson(d);
        assertTrue(json.contains("\"pub\":\"p\""), json);
        assertFalse(json.contains("internal"), json);

        ExcludedDto back = JsonUtil.fromJson("{\"pub\":\"p\",\"internal\":\"i\"}", ExcludedDto.class);
        assertEquals("p", back.getPub());
        assertNull(back.getInternal(), "exclude 字段不应被回填");
    }

    // ---- 构造器绑定 ----

    @Test
    void immutableClassBindsViaConstructor() {
        SnakeImmutable dto = JsonUtil.fromJson(
                "{\"tool_call_id\":\"tc1\",\"parts\":[\"a\",\"b\"],\"optional\":\"o\"}",
                SnakeImmutable.class);
        assertEquals("tc1", dto.toolCallId());
        assertEquals(Arrays.asList("a", "b"), dto.parts());
        assertEquals("o", dto.optional());
    }

    @Test
    void constructorBindingGivesDefaultsForAbsentParams() {
        SnakeImmutable dto = JsonUtil.fromJson("{\"tool_call_id\":\"tc1\"}", SnakeImmutable.class);
        assertEquals("tc1", dto.toolCallId());
        assertNull(dto.parts(), "缺参引用类型应为 null");
        assertNull(dto.optional());
    }

    @Test
    void multipleParameterConstructorsFailLoudly() {
        assertThrows(RuntimeException.class,
                () -> JsonUtil.fromJson("{\"a\":\"x\"}", TwoCtorNoNoArg.class));
    }

    // ---- 未知键容忍 ----

    @Test
    void unknownKeysAreToleratedByDefault() {
        SnakeDto dto = JsonUtil.fromJson(
                "{\"prompt_tokens\":\"p\",\"unknown_key\":123,\"another\":{\"x\":1}}", SnakeDto.class);
        assertEquals("p", dto.getPromptTokens());
    }
}
