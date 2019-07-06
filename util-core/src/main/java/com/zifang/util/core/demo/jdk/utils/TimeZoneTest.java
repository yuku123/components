package com.zifang.util.core.demo.jdk.utils;

import java.util.TimeZone;
import java.util.UUID;

public class TimeZoneTest {
    public static void main(String[] args) {
        System.gc();
        System.out.println(UUID.randomUUID());
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println(timeZone.getDisplayName());
        /**
         * String[] strs=timeZone.getAvailableIDs(); for(String str:strs){
         * System.out.println(str); }
         */
    }
}
