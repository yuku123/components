package com.zifang.util.json;

import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JSON 树的容错只读游标，语义对齐 Jackson 的 {@code JsonNode.path(...)} 家族，
 * 用于把依赖 Jackson 树导航（{@code readTree} + {@code path()/asText()/isArray()}）的代码
 * 迁到自研解析器上。
 *
 * <p>核心性质是<b>永不抛、永不返回 null</b>：对非对象取 {@code path(key)}、对非数组取
 * {@code at(i)}、越界、键不存在，一律返回 {@link #missing()} 游标，链式下钻到底也不炸；
 * 真正的校验交给调用方用 {@link #isMissing()} 判断。这与 Jackson「缺失返回 MissingNode」
 * 的行为一致——迁 Jackson 的测试断言（{@code path("a").path("b").asText("")}）不需要改语义。</p>
 *
 * <p>与 Jackson 的两处刻意差异：</p>
 * <ul>
 *   <li>本库的 JSON null 解析成 Java {@code null}，靠 {@link JsonObject#containsKey} 与缺失区分：
 *       {@code {"k":null}} 上 {@code path("k")} 得到的是<b>有值游标</b>（{@code isNull()}=true、
 *       {@code isMissing()}=false），而 {@code {}} 上 {@code path("k")} 才是 missing。Jackson 同样如此。</li>
 *   <li>容器节点上 {@code asText()} 返回空串（对齐 Jackson），要容器文本用
 *       {@link JsonUtil#toJson(Object)}。</li>
 * </ul>
 *
 * <p>用法：</p>
 * <pre>{@code
 * JsonCursor cur = JsonCursor.of(JsonUtil.parse(body));
 * String status = cur.path("status").asText();
 * int total = cur.path("result").path("total").asInt(0);
 * if (cur.path("tools").isArray()) {
 *     for (JsonCursor t : cur.path("tools").elements()) {
 *         names.add(t.path("name").asText());
 *     }
 * }
 * }</pre>
 */
public final class JsonCursor {

    private static final JsonCursor MISSING = new JsonCursor(null, true);

    /** 包住的节点：JsonObject/JsonArray/标量；missing 游标为 null。 */
    private final Object node;
    private final boolean missing;

    private JsonCursor(Object node, boolean missing) {
        this.node = node;
        this.missing = missing;
    }

    /** 包住一个解析结果（{@link JsonUtil#parse(String)} 的返回值）或任意子值。null 按 missing 处理。 */
    public static JsonCursor of(Object node) {
        return node == null ? MISSING : new JsonCursor(node, false);
    }

    /** 缺失游标：所有取值方法走默认值，所有 is 判断为 false（除 {@link #isMissing()}）。 */
    public static JsonCursor missing() {
        return MISSING;
    }

    // ===================== 导航 =====================

    /** 对象字段下钻：非对象或键不存在返回 missing；键存在但值为 null 返回 isNull 的有值游标。 */
    public JsonCursor path(String key) {
        if (missing) return MISSING;
        if (node instanceof JsonObject) {
            JsonObject obj = (JsonObject) node;
            if (!obj.containsKey(key)) return MISSING;
            return new JsonCursor(obj.get(key), false);
        }
        if (node instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) node;
            if (!m.containsKey(key)) return MISSING;
            return new JsonCursor(m.get(key), false);
        }
        return MISSING;
    }

    /** 数组元素下钻：非数组或越界返回 missing。 */
    public JsonCursor at(int index) {
        if (missing) return MISSING;
        if (node instanceof JsonArray) {
            JsonArray arr = (JsonArray) node;
            if (index < 0 || index >= arr.size()) return MISSING;
            return new JsonCursor(arr.get(index), false);
        }
        if (node instanceof List<?>) {
            List<?> l = (List<?>) node;
            if (index < 0 || index >= l.size()) return MISSING;
            return new JsonCursor(l.get(index), false);
        }
        return MISSING;
    }

    // ===================== 形状判断 =====================

    public boolean isMissing() {
        return missing;
    }

    /** 键存在且值为 JSON null（注意：missing 游标不是 null）。 */
    public boolean isNull() {
        return !missing && node == null;
    }

    public boolean isObject() {
        return !missing && (node instanceof JsonObject || node instanceof Map);
    }

    public boolean isArray() {
        return !missing && (node instanceof JsonArray || node instanceof List);
    }

    /** 标量（字符串/数字/布尔）。 */
    public boolean isScalar() {
        return !missing && node != null && !isObject() && !isArray();
    }

    /** 对象是否含键；非对象一律 false。 */
    public boolean has(String key) {
        if (missing) return false;
        if (node instanceof JsonObject) return ((JsonObject) node).containsKey(key);
        if (node instanceof Map<?, ?>) return ((Map<?, ?>) node).containsKey(key);
        return false;
    }

    /** 数组长度或对象键数；missing/标量为 0。 */
    public int size() {
        if (missing) return 0;
        if (node instanceof JsonArray) return ((JsonArray) node).size();
        if (node instanceof JsonObject) return ((JsonObject) node).size();
        if (node instanceof List<?>) return ((List<?>) node).size();
        if (node instanceof Map<?, ?>) return ((Map<?, ?>) node).size();
        return 0;
    }

    // ===================== 取值（容错） =====================

    /**
     * 标量转字符串：字符串原样、数字/布尔按 toString、JSON null 按 Jackson 语义为 {@code "null"}；
     * missing 与容器返回空串。
     */
    public String asText() {
        if (missing || node instanceof JsonObject || node instanceof JsonArray) return "";
        return String.valueOf(node);
    }

    /** 标量转 int：Number 直接取值，可解析的数字字符串也接受，其余返回默认值。 */
    public int asInt(int def) {
        Number n = asNumber();
        return n == null ? def : n.intValue();
    }

    public long asLong(long def) {
        Number n = asNumber();
        return n == null ? def : n.longValue();
    }

    public double asDouble(double def) {
        Number n = asNumber();
        return n == null ? def : n.doubleValue();
    }

    public boolean asBoolean(boolean def) {
        if (missing) return def;
        if (node instanceof Boolean) return (Boolean) node;
        if (node instanceof String) {
            String s = (String) node;
            if ("true".equalsIgnoreCase(s)) return true;
            if ("false".equalsIgnoreCase(s)) return false;
        }
        return def;
    }

    private Number asNumber() {
        if (missing) return null;
        if (node instanceof Number) return (Number) node;
        if (node instanceof String) {
            try {
                return new java.math.BigDecimal(((String) node).trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    // ===================== 遍历 =====================

    /** 数组元素的游标列表；非数组返回空列表。 */
    public List<JsonCursor> elements() {
        if (node instanceof JsonArray) {
            JsonArray arr = (JsonArray) node;
            List<JsonCursor> out = new ArrayList<>(arr.size());
            for (int i = 0; i < arr.size(); i++) {
                out.add(new JsonCursor(arr.get(i), false));
            }
            return out;
        }
        if (node instanceof List<?>) {
            List<?> l = (List<?>) node;
            List<JsonCursor> out = new ArrayList<>(l.size());
            for (Object o : l) {
                out.add(new JsonCursor(o, false));
            }
            return out;
        }
        return Collections.emptyList();
    }

    /** 对象的键集（保文档顺序）；非对象返回空集。 */
    public Set<String> fieldNames() {
        if (node instanceof JsonObject) {
            Set<String> out = new LinkedHashSet<>();
            for (Map.Entry<String, Object> e : ((JsonObject) node).getAllKeyValue()) {
                out.add(e.getKey());
            }
            return out;
        }
        if (node instanceof Map<?, ?>) {
            Set<String> out = new LinkedHashSet<>();
            for (Object k : ((Map<?, ?>) node).keySet()) {
                out.add(String.valueOf(k));
            }
            return out;
        }
        return Collections.emptySet();
    }

    // ===================== 还原 =====================

    /** 取原始节点：JsonObject/JsonArray/标量；missing 返回 null。 */
    public Object value() {
        return missing ? null : node;
    }

    @Override
    public String toString() {
        return missing ? "<missing>" : String.valueOf(node);
    }
}
