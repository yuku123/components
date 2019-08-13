package com.zifang.util.core.collections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Sets {

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
    public static <U,K,V> Map<K,V> populateMap(Set<U> set,Function<U,K> acceptAsKey,Function<U,V> acceptAsValue){
        Map<K,V> map = new LinkedHashMap<>();
        for(U u : set){
            K k = acceptAsKey.apply(u);
            V v = acceptAsValue.apply(u);
            map.put(k,v);
        }
        return map;
    }

}
