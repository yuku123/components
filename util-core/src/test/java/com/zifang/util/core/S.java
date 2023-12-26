package com.zifang.util.core;

import java.time.LocalDateTime;

public class S {
    public static void main(String[] args) {
        LocalDateTime l = LocalDateTime.now();
        l = l.minusMonths(80);
        int i = 0;
        for (; ; ) {
            i++;
            l = l.plusMonths(1);
            System.out.println("## " + l.getYear() + "-" + (l.getMonth().getValue() > 9 ? l.getMonth().getValue() : "0" + l.getMonth().getValue()));
            if (i > 80) {
                break;
            }
        }
    }
}
