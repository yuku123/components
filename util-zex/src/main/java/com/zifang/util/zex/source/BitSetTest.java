package com.zifang.util.zex.source;

import java.util.BitSet;

public class BitSetTest {
    public static void main(String[] args) {


        BitSet bitSet = new BitSet(129);
        bitSet.set(0);
        bitSet.set(64);

        BitSet  ass = bitSet.get(0,65);

        bitSet.nextSetBit(1);

    }
}
