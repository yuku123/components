package com.zifang.util.core.collections;


import com.zifang.util.core.base.condition.NullPredicate;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import java.util.function.Predicate;

public class Maps {

    /**
     * 移除key为null的元素
     *
     * @param map
     *
     * */
    public static <K,V> void removeNullKeys(Map<K,V> map){
        removeKeys(map, new NullPredicate<>());
    }

    /**
     * 移除value 为null的元素
     *
     * @param map
     *
     * */
    public static <K,V> void removeNullValues(Map<K,V> map){
        removeValues(map, new NullPredicate<>());
    }


    /**
     * 移除符合predicate检测结果的key的元素
     *
     * @param map 将要被移除元素的map
     * @param predicate 检验条件
     *
     * */
    public static <K,V> void removeKeys(Map<K,V> map, Predicate<K> predicate){
        Iterator<Map.Entry<K,V>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<K,V> entry = it.next();
            if(predicate.test(entry.getKey())){
                it.remove();
            }
        }
    }


    /**
     * 移除符合predicate检测结果的value的元素
     *
     * @param map 将要被移除元素的map
     * @param predicate 检验条件
     *
     * */
    public static <K,V> void removeValues(Map<K,V> map, Predicate<V> predicate){
        Iterator<Map.Entry<K,V>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<K,V> entry = it.next();
            if(predicate.test(entry.getValue())){
                it.remove();
            }
        }
    }

    /**
     * 移除符合predicate检测结果的key,value的元素
     *
     * @param map 将要被移除元素的map
     * @param predicate 检验条件(同时对key与value生效)
     *
     * */
    public static <K,V> void remove(Map<K,V> map, Predicate<Map.Entry<K,V>> predicate){
        Iterator<Map.Entry<K,V>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<K,V> entry = it.next();
            if(predicate.test(entry)){
                it.remove();
            }
        }
    }

    /**
     * 对给定的Map进行条件过滤
     * @param map Map<K,V>
     * @param predicate Predicate<Map.Entry<K,V>>
     *
     * @return Map<K,V>
     *
     * */
    public static <K,V> Map<K,V> filter(Map<K,V> map,Predicate<Map.Entry<K,V>> predicate){
        Map<K,V> mapStore = new HashMap<>(map.size());
        remove(mapStore,predicate);
        return mapStore;
    }
}
