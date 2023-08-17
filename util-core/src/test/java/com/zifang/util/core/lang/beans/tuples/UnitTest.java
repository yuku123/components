package com.zifang.util.core.lang.beans.tuples;


import com.zifang.util.core.lang.tuples.Pair;
import org.junit.Test;

public class UnitTest {

    @Test
    public void toMap() {
        Pair pair = new Pair<>("a", "b");
        System.out.println(pair.toMap());
    }
}