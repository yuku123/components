package com.zifang.util.zex.leetcode;

public class _048 {
    public void rotate(int[][] matrix) {

        // 矩阵大小
        int n = matrix.length;

        // 层数
        int cs = (n + 1)/2;
        int temp = 0;

        // 遍历层数
        for(int c = 0; c < cs; c++){
            // 单层内循环
            for(int j = c; j < n - c - 1; j++){
                // a: matrix[c][j]
                // b: matrix[n-c-1-j][c]
                // c: matrix[n-c-1][n-c-1-j]
                // d: matrix[n-c-1][c+j]
                temp = matrix[c][j];
                matrix[c][j] = matrix[n-1-j][c];
                matrix[n-1-j][c] = matrix[n-1-c][n-1-j];
                matrix[n-1-c][n-1-j] = matrix[j][n-1-c];
                matrix[j][n-1-c] = temp;
            }
        }
    }

    public static void main(String[] args) {
        new _048().rotate(new int[][]{new int[]{5,1,9,11},new int[]{2,4,8,10},new int[]{13,3,6,7},new int[]{15,14,12,16}});
    }
}
