package com.zifang.util.core.lang.tuples;

import org.junit.jupiter.api.Test;

class UnitTest {

    @Test
    void toMap() {
        Pair pair = new Pair<>("a","b");
        System.out.println(pair.toMap());
    }
}