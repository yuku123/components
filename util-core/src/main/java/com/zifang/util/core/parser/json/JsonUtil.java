package com.zifang.util.core.parser.json;

import com.zifang.util.core.parser.json.define.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zifang
 */
public class JsonUtil {

    public static <T> String toJson(T t) {

        if(t.getClass().isAssignableFrom(List.class)){
            return solveList(t);
        } else if(t.getClass().isAssignableFrom(Map.class)){
            return solveMap(t);
        } else {
            return solveObject(t);
        }
    }

    private static <T> String solveMap(T t) {
        return null;
    }

    private static <T> String solveList(T t) {
        return null;
    }

    private static <T> String solveObject(T t) {
        return null;
    }

    public static <T> T fromJson(String jsonStr, TypeReference<?> clazz) {
        T t = null;
//        try {
//             t = clazz.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        return t;
    }

    public static void main(String[] args) {
        List<String> c = new ArrayList<>();
        fromJson("",new TypeReference<List<String>>(){});
        c.getClass();
    }
}
