package com.zifang.util.expr.sql.function;

import com.zifang.util.expr.sql.SqlException;
import com.zifang.util.expr.sql.annotation.SqlFunction;
import com.zifang.util.json.JsonUtil;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 内置 JSON 函数（MySQL 语义子集）。
 * <ul>
 *   <li>{@code JSON_EXTRACT(doc, '$.a.b[0]')}：按 path 取值；标量返回 Java 值，
 *       对象/数组返回紧凑 JSON 文本，path 未命中返回 NULL。</li>
 *   <li>{@code JSON_CONTAINS(target, candidate[, path])}：包含判定，返回 1/0。
 *       数组候选要求元素逐一被包含，对象候选要求键值逐一被包含，
 *       数值按十进制值比较（1 与 1.0 等价）。</li>
 * </ul>
 * 基于自研 JsonUtil（z-util-core 传递依赖 z-util-parser-json），不引入 Jackson。
 */
public final class SqlJsonFunctions {

    private SqlJsonFunctions() {
    }

    @SqlFunction("JSON_EXTRACT")
    public static Object json_extract(Map<String, Object> row, Object doc, Object path) {
        if (doc == null || path == null) {
            return null;
        }
        Object node = selectPath(readTree(doc), path.toString());
        return toJavaValue(node);
    }

    @SqlFunction("JSON_CONTAINS")
    public static Object json_contains(Map<String, Object> row, Object target, Object candidate, Object... path) {
        if (target == null || candidate == null) {
            return null;
        }
        Object targetNode = readTree(target);
        if (path != null && path.length > 0 && path[0] != null) {
            targetNode = selectPath(targetNode, path[0].toString());
            if (targetNode == null) {
                return null; // MySQL：path 未命中返回 NULL
            }
        }
        Object candidateNode = readTree(candidate);
        return contains(targetNode, candidateNode) ? 1L : 0L;
    }

    // ===================== 内部实现 =====================

    private static Object readTree(Object v) {
        if (v instanceof JsonObject || v instanceof JsonArray) {
            return v;
        }
        try {
            if (v instanceof String || v instanceof Character) {
                return JsonUtil.parse(v.toString());
            }
            return JsonUtil.parse(JsonUtil.toJson(v));
        } catch (Exception e) {
            throw new SqlException("非法 JSON 文档: " + v, e);
        }
    }

    /**
     * 解析 MySQL 风格 path：{@code $} 开头，{@code .key} / {@code ."quoted key"} / {@code [idx]} 链式下钻。
     * 未命中返回 null（区别于 JSON null 值——那种情况原样返回）。
     */
    private static Object selectPath(Object root, String path) {
        String p = path.trim();
        if (!p.startsWith("$")) {
            throw new SqlException("非法 JSON path（须以 $ 开头）: " + path);
        }
        Object cur = root;
        int i = 1;
        while (i < p.length()) {
            char c = p.charAt(i);
            if (c == '.') {
                i++;
                String key;
                if (i < p.length() && (p.charAt(i) == '"' || p.charAt(i) == '\'')) {
                    char quote = p.charAt(i++);
                    int start = i;
                    while (i < p.length() && p.charAt(i) != quote) {
                        i++;
                    }
                    if (i >= p.length()) {
                        throw new SqlException("JSON path 引号未闭合: " + path);
                    }
                    key = p.substring(start, i);
                    i++;
                } else {
                    int start = i;
                    while (i < p.length() && p.charAt(i) != '.' && p.charAt(i) != '[') {
                        i++;
                    }
                    key = p.substring(start, i);
                }
                if (key.isEmpty()) {
                    throw new SqlException("JSON path 含空 key: " + path);
                }
                if (!(cur instanceof JsonObject)) {
                    return null;
                }
                JsonObject obj = (JsonObject) cur;
                if (!obj.containsKey(key)) {
                    return null;
                }
                cur = obj.get(key);
            } else if (c == '[') {
                int close = p.indexOf(']', i);
                if (close < 0) {
                    throw new SqlException("JSON path 方括号未闭合: " + path);
                }
                String idx = p.substring(i + 1, close).trim();
                i = close + 1;
                int n;
                try {
                    n = Integer.parseInt(idx);
                } catch (NumberFormatException e) {
                    throw new SqlException("JSON path 数组下标非法: " + path);
                }
                if (!(cur instanceof JsonArray) || n < 0 || n >= ((JsonArray) cur).size()) {
                    return null;
                }
                cur = ((JsonArray) cur).get(n);
            } else {
                throw new SqlException("JSON path 语法不支持（位置 " + i + "）: " + path);
            }
        }
        return cur;
    }

    private static Object toJavaValue(Object node) {
        if (node == null) {
            return null;
        }
        if (node instanceof JsonObject || node instanceof JsonArray) {
            // 必须先摊平：JsonObject/JsonArray 的 toString() 是缩进格式，直接 toJson 会出多行。
            return JsonUtil.toJson(JsonUtil.toPlainValue(node));
        }
        if (node instanceof Boolean || node instanceof Number || node instanceof String) {
            return node;
        }
        return String.valueOf(node);
    }

    private static boolean contains(Object target, Object candidate) {
        if (target instanceof JsonObject && candidate instanceof JsonObject) {
            for (Map.Entry<String, Object> field : ((JsonObject) candidate).getAllKeyValue()) {
                JsonObject t = (JsonObject) target;
                if (!t.containsKey(field.getKey()) || !contains(t.get(field.getKey()), field.getValue())) {
                    return false;
                }
            }
            return true;
        }
        if (target instanceof JsonArray) {
            for (Object t : toList((JsonArray) target)) {
                if (contains(t, candidate)) {
                    return true;
                }
            }
            return false;
        }
        return jsonEquals(target, candidate);
    }

    private static boolean jsonEquals(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            return toDecimal((Number) a).stripTrailingZeros()
                    .compareTo(toDecimal((Number) b).stripTrailingZeros()) == 0;
        }
        if (a instanceof JsonObject && b instanceof JsonObject) {
            return Objects.equals(canonical(a), canonical(b));
        }
        if (a instanceof JsonArray && b instanceof JsonArray) {
            return Objects.equals(canonical(a), canonical(b));
        }
        return Objects.equals(a, b);
    }

    /** 容器比较走摊平后的紧凑文本：JsonObject 没有覆写 equals，直接比是引用相等。 */
    private static String canonical(Object node) {
        return JsonUtil.toJson(JsonUtil.toPlainValue(node));
    }

    private static BigDecimal toDecimal(Number n) {
        if (n instanceof BigDecimal) {
            return (BigDecimal) n;
        }
        return new BigDecimal(n.toString());
    }

    private static List<Object> toList(JsonArray array) {
        List<Object> list = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            list.add(array.get(i));
        }
        return list;
    }
}
