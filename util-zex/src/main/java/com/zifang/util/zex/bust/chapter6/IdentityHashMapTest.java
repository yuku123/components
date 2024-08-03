package com.zifang.util.zex.bust.chapter6;

import java.util.IdentityHashMap;

public class IdentityHashMapTest {
    static class A{

    }

    public static void main(String[] args) {
        A a = new A();
        IdentityHashMap<A,Integer> integerIdentityHashMap = new IdentityHashMap<>(4);
        integerIdentityHashMap.put(a,1);
        integerIdentityHashMap.put(new A(),1);
        integerIdentityHashMap.put(new A(),2);
        integerIdentityHashMap.put(new A(),3);


    }
}
