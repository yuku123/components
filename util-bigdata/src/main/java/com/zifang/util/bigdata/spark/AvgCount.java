package com.zifang.util.bigdata.spark;

public class AvgCount {

    public AvgCount(int total, int num) {
        this.total = total;
        this.num = num;
    }
    public int total;
    public int num;

    public double avg() {
        return total / (double) num;
    }
}
