package com.zifang.util.json;

import com.google.gson.Gson;

public class GsonUtil {

	private static Gson gson = new Gson();

	public static <T> String objectToJsonStr(T object) {
		return gson.toJson(object);
	}

	public static <T> T jsonStrToObject(String jsonStr, Class<T> classOfT) {
		return gson.fromJson(jsonStr, classOfT);
	}

	public static <T> T changeToSubClass(Object o,Class<T> t){
		return jsonStrToObject(objectToJsonStr(o),t);
	}
}
