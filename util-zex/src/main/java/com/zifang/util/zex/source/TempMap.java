package com.zifang.util.zex.source;

import java.util.AbstractMap;
import java.util.Set;

public class TempMap<K,V> extends AbstractMap<K,V> {

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }
}
