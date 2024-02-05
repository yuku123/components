package com.zifang.util.pandas.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 矩阵类
 */
public class Matrix {

    private List<List<Double>> data = new ArrayList<>();

    /**
     * 矩阵乘法
     */
    public void multiply(Matrix another) {

    }

    /**
     * 矩阵的一行
     */
    public void set(Double... arrays) {
        data.add(Arrays.asList(arrays));
    }

    /**
     * 美化输出
     */
    public void format() {

    }

    private Integer analysisPadding() {
        Integer max = 0;
        for (List<Double> row : data) {
            for (Double col : row) {
                Integer cu = String.valueOf(col).length();
                if (cu > max) {
                    max = cu;
                }
            }
        }
        return max;
    }

    public void shape() {
    }

    public void dtype() {
    }

    public void ndim() {
    }

    /**
     * 切片方法
     */
    public List<List<Double>> slice() {
        return null;
    }

}
