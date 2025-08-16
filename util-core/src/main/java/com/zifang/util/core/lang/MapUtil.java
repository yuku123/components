package com.zifang.util.core.lang;

import com.zifang.util.core.lang.primitive.ByteUtil;
import com.zifang.util.core.lang.primitive.IntegerUtil;
import com.zifang.util.core.lang.primitive.LongUtil;
import com.zifang.util.core.lang.primitive.ShortUtil;

import java.util.*;

/**
 * @author: zifang
 * @time: 2021-12-02 20:08:00
 * @description: map util
 * @version: JDK 1.8
 */
public class MapUtil {

    public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

    public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
        if (props != null) {
            for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    // Allow for defaults fallback or potentially overridden accessor...
                    value = props.getProperty(key);
                }
                map.put((K) key, (V) value);
            }
        }
    }

    /**
     * @author: zifang
     * @description: new HashMap with expected size
     * @time: 2021-12-24 11:21:07
     * @params: [expectedSize] expected size
     * @return: java.util.HashMap<K, V> out 出参
     */
    public static <K, V> HashMap<K, V> newHashMap(int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return newHashMap(16);
    }


    /**
     * @author: zifang
     * @description: new LinkedHashMap with expected size
     * @time: 2021-12-24 11:21:07
     * @params: [expectedSize] expected size
     * @return: java.util.LinkedHashMap<K, V> out 出参
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int expectedSize) {
        return new LinkedHashMap<>(capacity(expectedSize));
    }

    protected static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            checkNonNegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            // This is the calculation used in JDK8 to resize when a putAll
            // happens; it seems to be the most conservative calculation we
            // can make.  0.75 is the default load factor.
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        return Integer.MAX_VALUE;
    }

    private static int checkNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        }
        return value;
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    public static <K, V> V parseValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return map.get(key);
    }

    public static <K, V> String parseStringValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return StringUtil2.parseString(map.get(key));
    }

    public static <K, V> Byte parseByteValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return ByteUtil.parseByte(map.get(key));
    }

    public static <K, V> Short parseShortValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return ShortUtil.parseShort(map.get(key));
    }

    public static <K, V> Integer parseIntegerValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return IntegerUtil.parseInteger(map.get(key));
    }


    public static <K, V> Long parseLongValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return LongUtil.parseLong(map.get(key));
    }

    public static <K, V> V parseValueOrDefault(Map<K, V> map, K key, V defaultValue) {
        if (isEmpty(map)) {
            return defaultValue;
        }
        return map.get(key);
    }


    /**
     * @author: zifang
     * @description: create map from properties
     * @time: 2021-12-24 11:19:30
     * @params: [properties] properties
     * @return: java.util.Map<java.lang.String, java.lang.String> properties map
     */
    public static Map<String, String> fromProperties(Properties properties) {
        if (null == properties) {
            return Collections.emptyMap();
        }
        Map<String, String> map = newHashMap(properties.size());
        Enumeration<?> enumeration = properties.propertyNames();

        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            map.put(key, properties.getProperty(key));
        }

        return map;
    }

    public static <T> Map<String, T> replaceKey(Map<String, T> map, String search, String replace) {
        Map<String, T> newMap = MapUtil.newHashMap(map.size());
        map.forEach((key, value) -> {
            String newKey;
            if (key.contains(search)) {
                newKey = StringUtil2.replace(key, search, replace);
            } else {
                newKey = key;
            }
            newMap.put(newKey, value);
        });
        return newMap;
    }

    /**
     * @author: zifang
     * @description: Trim value
     * @description: 剔除值为null的键值对
     * @time: 2022-11-28 22:52:07
     * @params: [sourceMap] source map
     * @return: java.util.Map<java.lang.String, T> result map
     */
    public static <T> Map<String, T> trimValue(Map<String, T> sourceMap) {
        if (isEmpty(sourceMap)) {
            return sourceMap;
        }
        List<String> removeKeyList = new ArrayList<>();
        sourceMap.forEach((key, value) -> {
            if (null == value) {
                removeKeyList.add(key);
            }
        });
        for (String key : removeKeyList) {
            sourceMap.remove(key);
        }
        return sourceMap;
    }

}
