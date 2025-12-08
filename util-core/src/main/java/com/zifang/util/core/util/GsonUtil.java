package com.zifang.util.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author zifang
 */
public class GsonUtil {

//    private static Gson gson = new Gson();
    // 后端返回的日期格式（需与后端一致）
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 初始化Gson，注册LocalDateTime适配器
    private static final Gson gson = new GsonBuilder()
            // 序列化：LocalDateTime → 字符串
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                return new com.google.gson.JsonPrimitive(src.format(DATE_TIME_FORMATTER));
            })
            // 反序列化：字符串 → LocalDateTime
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
                if (json.getAsString().isEmpty()) {
                    return null;
                }
                return LocalDateTime.parse(json.getAsString(), DATE_TIME_FORMATTER);
            })
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // 兼容Date类型
            .create();


    public static <T> String objectToJsonStr(T object) {
        return gson.toJson(object);
    }

    public static <T> T jsonStrToObject(String jsonStr, Class<T> classOfT) {
        return gson.fromJson(jsonStr, classOfT);
    }

    public static <T> T jsonStrToObject(String jsonStr, Type typeReference) {
        return gson.fromJson(jsonStr, typeReference);
    }


    public static <T> T changeToSubClass(Object o, Class<T> t) {
        return jsonStrToObject(objectToJsonStr(o), t);
    }

    public static Map<String, Object> toMap(Object o) {
        return (Map<String, Object>) jsonStrToObject(GsonUtil.objectToJsonStr(o), Map.class);
    }
}
