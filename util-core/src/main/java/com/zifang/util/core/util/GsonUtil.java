package com.zifang.util.core.util;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

	public static ParameterizedType type(final Class<?> raw, final Type... args) {
		return new ParameterizedType() {

			@Override
			public Type getRawType() {
				return raw;
			}

			@Override
			public Type getOwnerType() {
				return null;
			}

			@Override
			public Type[] getActualTypeArguments() {
				return args;
			}
		};
	}
}
