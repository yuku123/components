package com.zifang.util.core.collections;


import com.zifang.util.core.base.condition.NullPredicate;

import java.util.*;

import java.util.function.Function;
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

    /**
     * set 转换器，根据set内的每个值，分裂生成Map对象
     *
     * @param set 将要被处理的set
     * @param acceptAsKey 对set生成key
     * @param acceptAsValue 对set元素生成value值
     *
     * @return Map<K,V> 生成符合条件的map对象
     * */
    //可以对此生成唯一的自增长id
    public static <U,K,V> Map<K,V> populateMap(Set<U> set, Function<U,K> acceptAsKey, Function<U,V> acceptAsValue){
        Map<K,V> map = new LinkedHashMap<>();
        for(U u : set){
            K k = acceptAsKey.apply(u);
            V v = acceptAsValue.apply(u);
            map.put(k,v);
        }
        return map;
    }
}
