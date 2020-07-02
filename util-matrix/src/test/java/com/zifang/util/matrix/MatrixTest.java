package com.zifang.util.matrix;

import org.junit.Test;

public class MatrixTest {

    @Test
    public void test(){
        Matrix matrix = new Matrix();
        matrix.set(1.2,3.233333333,2.1);
        matrix.set(1.4,3.3,1.0);
        matrix.set(1.4,3.4,1.0);
        matrix.format();
    }
}
