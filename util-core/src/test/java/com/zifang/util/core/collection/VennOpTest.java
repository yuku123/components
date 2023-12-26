package com.zifang.util.core.collection;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class VennOpTest {
    @Test
    public void test0() {
        List<String> l1 = Arrays.asList("0", "1", "2", "3", "3");
        List<String> l2 = Arrays.asList("2", "3", "4", "4");

        VennGraph<String> vennGraph = new VennGraph<>(l1, l2);

        assert vennGraph.unionCount() == 5;
        assert vennGraph.union().size() == 5;
        assert vennGraph.intersection().size() == 2;
        assert vennGraph.intersectionCount() == 2;
        assert vennGraph.intersectionLeft().size() == 2;
        assert vennGraph.intersectionRight().size() == 1;
    }
}
