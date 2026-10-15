package com.zifang.util.serialize.formats.json;

import com.zifang.util.json.JsonUtil;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import com.zifang.util.serialize.annotation.ZMessage;
import com.zifang.util.serialize.core.SchemaRegistry;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON 互转器：将 {@code @ZMessage} 对象转为人类可读 JSON，反之亦然。
 *
 * <p>JSON 输出包含字段 ID（{@code _id}），便于其他语言解析时定位。
 * 例如：</p>
 *
 * <pre>{@code
 * {
 *     "_id": 100,                          // 来自 @ZMessage.id
 *     "_type": "test.User",                // 来自 @ZMessage.name
 *     "1": 42,                             // @ZField(id=1) → key="1"
 *     "2": "alice"                         // @ZField(id=2) → key="2"
 * }
 * }</pre>
 *
 * <p>数值 key 保证跨语言顺序无关；字段名可读性通过 {@code _fields} 数组补充：</p>
 *
 * <pre>{@code
 * {
 *     "_id": 100,
 *     "_fields": ["id", "name"],
 *     "1": 42,
 *     "2": "alice"
 * }
 * }</pre>
 */
public final class JsonTranscoder {

    private JsonTranscoder() {}

    /** 将消息转为 JSON 字符串（缩进格式，便于阅读）。 */
    public static String toJson(Object message) throws IOException {
        return JsonUtil.toJsonPretty(encode(message));
    }

    /** 将消息转为紧凑 JSON 字符串。 */
    public static String toCompactJson(Object message) throws IOException {
        return JsonUtil.toJson(encode(message));
    }

    /** 从 JSON 字符串解码消息。 */
    public static <T> T fromJson(String json, Class<T> messageClass) throws IOException {
        return decodeFrom(JsonUtil.toPlainValue(JsonUtil.parseObject(json)), messageClass);
    }

    private static Object encode(Object message) {
        SchemaRegistry.SchemaDescriptor schema = SchemaRegistry.get(message.getClass());
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("_id", schema.getId());
        if (!schema.getName().isEmpty()) {
            root.put("_type", schema.getName());
        }
        // 字段名映射（顺序保持）
        List<Object> fields = new ArrayList<>();
        root.put("_fields", fields);
        for (SchemaRegistry.FieldDescriptor fd : schema.getFields()) {
            if (!fd.getRemoved().isEmpty()) continue;
            Object v;
            try {
                v = fd.getField().get(message);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot read " + fd.getName(), e);
            }
            if (v == null) continue;
            fields.add(fd.getName());
            root.put(String.valueOf(fd.getId()), encodeValue(v));
        }
        return root;
    }

    private static Object encodeValue(Object v) {
        if (v == null) return null;
        if (v instanceof Boolean || v instanceof Number || v instanceof String) return v;
        if (v instanceof Collection) {
            List<Object> arr = new ArrayList<>();
            for (Object o : (Collection<?>) v) arr.add(encodeValue(o));
            return arr;
        }
        if (v instanceof Map) {
            Map<String, Object> obj = new LinkedHashMap<>();
            for (Map.Entry<?, ?> e : ((Map<?, ?>) v).entrySet()) {
                obj.put(String.valueOf(e.getKey()), encodeValue(e.getValue()));
            }
            return obj;
        }
        if (v.getClass().isAnnotationPresent(ZMessage.class)) {
            return encode(v);
        }
        if (v.getClass().isEnum()) {
            return ((Enum<?>) v).name();
        }
        // fallback
        return v.toString();
    }

    private static <T> T decodeFrom(Object node, Class<T> messageClass) throws IOException {
        SchemaRegistry.SchemaDescriptor schema = SchemaRegistry.get(messageClass);
        Map<?, ?> obj = asMap(node);
        if (obj == null) {
            throw new IOException("Cannot decode JSON to " + messageClass.getName()
                    + ": root is not an object");
        }
        try {
            T instance = messageClass.getDeclaredConstructor().newInstance();
            for (SchemaRegistry.FieldDescriptor fd : schema.getFields()) {
                if (!fd.getRemoved().isEmpty()) continue;
                if (!obj.containsKey(String.valueOf(fd.getId()))) continue;
                Object v = decodeValue(obj.get(String.valueOf(fd.getId())), fd.getJavaType(), fd.getElementType());
                if (v == null) continue;
                fd.getField().set(instance, v);
            }
            return instance;
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            throw new IOException("Cannot decode JSON to " + messageClass.getName(), e);
        }
    }

    private static Object decodeValue(Object n, Class<?> type, Class<?> elemType) {
        if (n == null) return null;
        if (type == boolean.class || type == Boolean.class) return asBoolean(n);
        if (type == int.class || type == Integer.class) return (int) asLong(n);
        if (type == long.class || type == Long.class) return asLong(n);
        if (type == float.class || type == Float.class) return (float) asDouble(n);
        if (type == double.class || type == Double.class) return asDouble(n);
        if (type == BigDecimal.class || type == BigInteger.class) return asBigNumber(n, type);
        if (type == String.class) return asText(n);
        if (type.isEnum()) {
            // 支持 enum by name
            @SuppressWarnings({"unchecked", "rawtypes"})
            Object e = Enum.valueOf((Class<Enum>) type.asSubclass(Enum.class), asText(n));
            return e;
        }
        if (Collection.class.isAssignableFrom(type)) {
            Collection<Object> coll = new ArrayList<>();
            List<?> items = asList(n);
            if (items != null) {
                for (Object item : items) {
                    coll.add(decodeValue(item, elemType, null));
                }
            }
            return coll;
        }
        if (Map.class.isAssignableFrom(type)) {
            Map<Object, Object> map = new LinkedHashMap<>();
            Map<?, ?> src = asMap(n);
            if (src != null) {
                for (Map.Entry<?, ?> e : src.entrySet()) {
                    map.put(String.valueOf(e.getKey()), decodeValue(e.getValue(), elemType, null));
                }
            }
            return map;
        }
        if (type.isAnnotationPresent(ZMessage.class)) {
            try {
                return decodeFrom(n, type);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return asText(n);
    }

    // ==================== 类型归一 ====================
    // 解析器把整数按字面量大小返回 Integer/Long/BigDecimal，直接 set 到 int 字段会抛
    // IllegalArgumentException（"argument type mismatch"）。Jackson 侧的 intValue()/longValue()
    // 天然做了归一，这里必须补上，否则窄字段赋值全部失败。

    private static Map<?, ?> asMap(Object n) {
        if (n instanceof Map) return (Map<?, ?>) n;
        Object plain = n instanceof JsonObject ? JsonUtil.toPlainValue(n) : null;
        return plain instanceof Map ? (Map<?, ?>) plain : null;
    }

    private static List<?> asList(Object n) {
        if (n instanceof List) return (List<?>) n;
        Object plain = n instanceof JsonArray ? JsonUtil.toPlainValue(n) : null;
        return plain instanceof List ? (List<?>) plain : null;
    }

    private static long asLong(Object n) {
        if (n instanceof Number) return ((Number) n).longValue();
        if (n instanceof Boolean) return ((Boolean) n) ? 1L : 0L;
        return Long.parseLong(asText(n));
    }

    private static double asDouble(Object n) {
        if (n instanceof Number) return ((Number) n).doubleValue();
        if (n instanceof Boolean) return ((Boolean) n) ? 1D : 0D;
        return Double.parseDouble(asText(n));
    }

    /**
     * BigDecimal / BigInteger 字段的还原。
     * <p>旧实现没有这一支，会掉到 {@code asText} 兜底产出 String，再 set 进 BigDecimal 字段时
     * 抛 IllegalArgumentException —— 即带 BigDecimal 字段的 {@code @ZMessage} 根本解不回来。</p>
     */
    private static Object asBigNumber(Object n, Class<?> type) {
        String text = asText(n);
        return type == BigInteger.class ? new BigInteger(text) : new BigDecimal(text);
    }

    private static boolean asBoolean(Object n) {        if (n instanceof Boolean) return (Boolean) n;
        if (n instanceof Number) return ((Number) n).doubleValue() != 0D;
        return Boolean.parseBoolean(asText(n));
    }

    private static String asText(Object n) {
        if (n == null) return null;
        if (n instanceof String) return (String) n;
        return String.valueOf(n);
    }
}
