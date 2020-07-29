package com.zifang.util.core.collections;

import java.util.Collection;
import java.util.Map;

/**
 * 对集合操作的封装
 * */
public class CollectionUtil {

    /**
     *  判断集合是否为空
     *
     * @param collection
     * @return boolean
     */
    public static boolean isEmptyCollection(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection
     * @return boolean
     */
    public static boolean isNotEmptyCollection(Collection<?> collection){
        return collection != null && !collection.isEmpty();
    }

    /**
     * 判断map集合是否不为空
     *
     * @param map
     * @return boolean
     */
    public static boolean isNotEmptyMap(Map<?,?> map){
        return map != null && !map.isEmpty();
    }

    /**
     * 判断map集合是否为空
     *
     * @param map
     * @return boolean
     */
    public static boolean isEmptyMap(Map<?,?> map){
        return map == null || map.isEmpty();
    }

}
