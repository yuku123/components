package com.zifang.util.poi.word;

public class Tuple<K,V> {
    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
