package com.zifang.util.core.collection;

import java.util.Map;

public class MapEntry<K,V> implements Map.Entry<K,V> {

    private K key;
    private V value;

    public MapEntry(K key,V value){
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(final V value) {
        this.value = value;
        return this.value;
    }

    public K setKey(final K key) {
        this.key = key;
        return this.key;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }

        Map.Entry entry = (Map.Entry) obj;

        return ((key == null) ?
                (entry.getKey() == null) :
                key.equals(entry.getKey())) && ((value == null) ?
                (entry.getValue() == null) :
                value.equals(entry.getValue()));
    }

    @Override
    public int hashCode() {
        return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
    }

    public static void main(String[] args) {
        System.out.println(""+null+null);
    }

}
