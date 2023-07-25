package com.zifang.util.zex.bust.chapter8;

import java.lang.reflect.Array;

public class GenericArray<T> {
    private T[] t;

    @SuppressWarnings({"unchecked", "hiding"})
    public void init(Class<T> clazz, int length) {
        t = (T[]) Array.newInstance(clazz, length);
    }
}

