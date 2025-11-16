package com.zifang.util.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author zifang
 */
public class GsonUtil {

    private static Gson gson = new Gson();

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
