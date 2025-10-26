package com.zifang.util.core.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExpandObject implements Serializable {

    private static final long serialVersionUID = -7925759494583339810L;

    /**
     * expand map
     */
    private Map<String, Object> expandMap;

    /**
     * @author: zifang
     * @description: clear value for key
     * @time: 2021/11/6 17:24
     * @params: [key] key
     * @return: java.lang.Object response
     */
    public Object clear(String key) {
        if (null != this.expandMap) {
            return this.expandMap.remove(key);
        }
        return null;
    }

    /**
     * @author: zifang
     * @description: clear all key and value
     * @time: 2021/11/6 17:24
     * @params: [key] key
     * @return: java.lang.Object response
     */
    public void clearAll() {
        if (null != this.expandMap) {
            this.expandMap.clear();
        }
    }

    /**
     * @author: zifang
     * @description: get expand value if not return defaultValue
     * @time: 2021/11/6 17:24
     * @params: [key, defaultValue] key, defaultValue
     * @return: java.lang.Object response
     */
    public Object getOrDefault(String key, String defaultValue) {
        if (null != this.expandMap) {
            return this.expandMap.getOrDefault(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * @author: zifang
     * @description: get expand value
     * @time: 2021/11/6 17:24
     * @params: [key] key
     * @return: java.lang.Object response
     */
    public Object get(String key) {
        if (null != this.expandMap) {
            return this.expandMap.get(key);
        }
        return null;
    }

    public String getString(String key) {
        return Optional.ofNullable(get(key)).map(Object::toString).orElse(null);
    }

    public Long getLong(String key) {
        return Optional.ofNullable(get(key)).map(value -> Long.parseLong(value.toString()))
                .orElse(null);
    }

    public Integer getInteger(String key) {
        return Optional.ofNullable(get(key)).map(value -> Integer.parseInt(value.toString()))
                .orElse(null);
    }

    public Short getShort(String key) {
        return Optional.ofNullable(get(key)).map(value -> Short.parseShort(value.toString()))
                .orElse(null);
    }

    public Byte getByte(String key) {
        return Optional.ofNullable(get(key)).map(value -> Byte.parseByte(value.toString()))
                .orElse(null);
    }

    public Double getDouble(String key) {
        return Optional.ofNullable(get(key)).map(value -> Double.parseDouble(value.toString()))
                .orElse(null);
    }

    public Float getFloat(String key) {
        return Optional.ofNullable(get(key)).map(value -> Float.parseFloat(value.toString()))
                .orElse(null);
    }

    public <T> T getObject(String key) {
        return (T) Optional.ofNullable(get(key)).orElse(null);
    }

    /**
     * @author: zifang
     * @description: put expand value
     * @time: 2021/11/6 17:24
     * @params: [key, value] key, value
     * @return: java.lang.Object response
     */
    public void set(String key, Object value) {
        if (null == this.expandMap) {
            this.expandMap = new HashMap<>(8);
        }
        this.expandMap.put(key, value);
    }

}
