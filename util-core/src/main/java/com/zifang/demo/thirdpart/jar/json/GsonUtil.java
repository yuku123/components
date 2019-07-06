package com.zifang.demo.thirdpart.jar.json;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonUtil {
	private static Gson gson = new Gson();
	public static <T> String objectToJsonStr(T object) {
		return gson.toJson(object);
	}
	public static <T> String objectToJsonStr(T object, Type type) {
		return gson.toJson(object, type);
	}
	public static <T> List<T> jsonToList(String jsonStr, Type type) {
		return gson.fromJson(jsonStr, type);
	}
	public static <T> T jsonStrToObject(String jsonStr, Class<T> classOfT) {
		return gson.fromJson(jsonStr, classOfT);
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
	
	public static void main(String[] args) {
	}
}
