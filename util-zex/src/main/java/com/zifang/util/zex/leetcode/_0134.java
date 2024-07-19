package com.zifang.util.zex.leetcode;

public class _0134 {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        if (gas == null || cost == null) return -1;

        int n = gas.length;

        int min = Integer.MAX_VALUE;
        int resIdx = 0;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += gas[i] - cost[i];
            if (sum < min) {
                min = sum;
                resIdx = (i + 1) % n;
            }
        }

        return sum < 0 ? -1 : resIdx;
    }

    public static void main(String[] args) {
        new _0134().canCompleteCircuit(new int[]{1,2,3,4,5}, new int[]{3,4,5,1,2});
    }
}
