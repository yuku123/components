package com.zifang.demo.jdk.test;

import java.sql.Timestamp;

public class a {
    public static void main(String[] args) {
        String s = "Jun 25, 2019 2:00:58 PM";
        System.out.println(Timestamp.valueOf(s).toString());
    }
}
