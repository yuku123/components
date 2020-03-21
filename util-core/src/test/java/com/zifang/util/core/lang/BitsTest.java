package com.zifang.util.core.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitsTest {


    @Test
    void multipleLess() {
        assertEquals(Bits.multipleLess(3),2);
        assertEquals(Bits.multipleLess(2),2);
    }

    @Test
    void multipleMore() {
        assertEquals(Bits.multipleMore(3),4);
        assertEquals(Bits.multipleMore(4),4);
        assertEquals(Bits.multipleMore(5),8);
    }

    @Test
    void avg() {
        assertEquals(Bits.avg(5,9),7);
        assertEquals(Bits.avg(5,10),7);
    }

    @Test
    void abs() {
        assertEquals(Bits.abs(2),2);
        assertEquals(Bits.abs(-2),2);
    }

    @Test
    void isOdd() {
        assertTrue(Bits.isOdd(2));
        assertFalse(Bits.isOdd(3));
        assertTrue(Bits.isOdd(-2));
    }

    @Test
    void isEven() {
        assertFalse(Bits.isEven(2));
        assertTrue(Bits.isEven(-3));
        assertTrue(Bits.isEven(3));
    }

    @Test
    void isPowFrom2() {
        assertTrue(Bits.isPowFrom2(4));
        assertFalse(Bits.isPowFrom2(5));
    }

    @Test
    void mod() {
        assertEquals(Bits.mod(12,5),2);
        assertEquals(Bits.mod(11,2),1);
    }
}