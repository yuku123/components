package com.zifang.util.core.lang.collection;


import com.zifang.util.core.lang.validator.Checker;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/***
 * 韦恩图操作
 *
 * @author zifang
 */
@Slf4j
public class Collections {

    public static <E> Collection<E> union(Collection<E>... collection) {
        Collection<E> base = new ArrayList<>();
        for (Collection<E> collectionElement : collection) {
            base.addAll(new ArrayList<>(collectionElement));
        }
        base = base.stream().distinct().collect(Collectors.toList());
        return base;
    }

    public static <E> Collection<E> retain(Collection<E> collection1, Collection<E> collection2) {
        Collection<E> left = new ArrayList<>(collection1);
        Collection<E> right = new ArrayList<>(collection2);
        left.retainAll(right);
        return left;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 参数
     * @return boolean
     */
    public static boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 参数
     * @return boolean
     */
    public static boolean isNotEmptyCollection(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 判断map集合是否不为空
     *
     * @param map 参数
     * @return boolean
     */
    public static boolean isNotEmptyMap(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 判断map集合是否为空
     *
     * @param map 参数
     * @return boolean
     */
    public static boolean isEmptyMap(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 去除重复元素
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        if (list == null || list.size() == 0) {
            log.error("list is empty or is null");
            return new ArrayList<>();
        }
        return list.stream().distinct().collect(Collectors.toList());

    }

    /**
     * 求俩个集合的交集
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        if (Checker.valid(list1, list2)) {
            Set<T> set = new HashSet<>(list1);
            set.retainAll(list2);
            return new ArrayList<>(set);
        }
        return new ArrayList<>();
    }

    /**
     * 求俩个集合的交集
     */
    public static <T> Set<T> intersection(Collection<T> set1, Collection<T> set2) {
        if (Checker.valid(set1, set2)) {
            List<T> list = new ArrayList<>(set1);
            list.retainAll(set2);
            return new HashSet<>(list);
        }
        return new HashSet<>();
    }

    /**
     * Map集合的交集只提供键的交集
     */
    public static <K, V> Map<K, V> intersection(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> map = new HashMap<>(map1.size());
        if (Checker.valid(map1, map2)) {
            Set<K> setkey1 = new HashSet<>(map1.keySet());
            Set<K> setkey2 = new HashSet<>(map2.keySet());
            setkey1.retainAll(setkey2);
            for (K k : setkey1) {
                map.put(k, map1.get(k));
            }
        }
        return map;
    }

    /**
     * 求俩个集合的并集
     */
    public static <T> List<T> unicon(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    /**
     * 求俩个集合的交集
     *
     * @param set1 set
     * @param set2 set
     * @param <T>  泛型
     * @return 交集
     */
    public static <T> Set<T> unicon(Set<T> set1, Set<T> set2) {
        set1.addAll(set2);
        return set1;
    }

    /**
     * 求俩个集合的交集
     *
     * @param queue1 队列
     * @param queue2 队列
     * @param <T>    泛型
     * @return 交集
     */
    public static <T> Queue<T> unicon(Queue<T> queue1, Queue<T> queue2) {
        queue1.addAll(queue2);
        return queue1;
    }

    /**
     * 求俩个map的交集
     *
     * @param map1 map
     * @param map2 map
     * @param <K>  泛型
     * @param <V>  泛型
     * @return 交集
     */
    public static <K, V> Map<K, V> unicon(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> map = new HashMap<>(map1.size() + map2.size());
        map.putAll(map1);
        map.putAll(map2);
        return map;
    }


    /**
     * 求俩个集合的差集
     */
    public static <T> List<T> subtract(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>(list1.size() + list2.size());
        if (Checker.valid(list1)) {
            list.addAll(list1);
            list.removeAll(list2);
        }
        return list;
    }

    /**
     * 求俩个集合的差集
     *
     * @param set1 set
     * @param set2 set
     * @param <T>  T
     * @return 差集
     */
    public static <T> Set<T> subtract(Set<T> set1, Set<T> set2) {
        Set<T> set = new HashSet<>(set1.size() + set2.size());
        if (Checker.valid(set1)) {
            set.addAll(set1);
            set.removeAll(set2);
        }
        return set;
    }

    /**
     * 求俩个集合的差集
     *
     * @param queue1 队列
     * @param queue2 队列
     * @param <T>    泛型
     * @return 差集
     */
    public static <T> Queue<T> subtract(Queue<T> queue1, Queue<T> queue2) {
        Queue<T> queue = new LinkedList<>();
        if (Checker.valid(queue1)) {
            queue.addAll(queue1);
            queue.removeAll(queue2);
        }
        return queue;
    }

    /**
     * 求俩个集合的差集
     *
     * @param map1 map
     * @param map2 map
     * @param <K>  泛型
     * @param <V>  泛型
     * @return 差集
     */
    public static <K, V> Map<K, V> subtract(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> map = new HashMap<>(map1.size() + map2.size());
        if (Checker.valid(map1, map2)) {
            Set<K> setkey1 = new HashSet<>(map1.keySet());
            Set<K> setkey2 = new HashSet<>(map2.keySet());
            for (K k : setkey2) {
                setkey1.remove(k);
            }
            for (K k : setkey1) {
                map.put(k, map1.get(k));
            }
        }
        return map;

    }

    /**
     * 将List以separator链接并以字符串的形式返回
     *
     * @param collection collection
     * @param separator  连接符
     * @param <T>        泛型
     * @return 差集
     */
    public static <T> String join(Collection<T> collection, String separator) {
        StringBuilder sb = new StringBuilder();
        for (T t : collection) {
            sb.append(t.toString()).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }


    /**
     * 将Map以separator链接并以字符串的形式返回
     *
     * @return 字符串
     */
    public static <K, V> String join(Map<K, V> map, String separator, String separator1) {
        if (map == null || map.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(separator1)
                    .append(entry.getValue()).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - separator.length());
    }

    /**
     * 将map的key以separator链接并以字符串的形式返回
     *
     * @param map       map
     * @param separator 连接符
     * @param <K>       泛型
     * @param <V>       泛型
     * @return 字符串
     */
    public static <K, V> String keyJoin(Map<K, V> map, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - separator.length());
    }

    /**
     * 将map的value以separator链接并以字符串的形式返回
     *
     * @param map       map
     * @param separator 连接符
     * @param <K>       泛型
     * @param <V>       泛型
     * @return 字符串
     */
    public static <K, V> String valueJoin(Map<K, V> map, String separator) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getValue()).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - separator.length());
    }

}