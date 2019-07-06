package com.zifang.common.utils;

public class ResourceUtil {
    public static String getFileFromResource(String path){
        return ResourceUtil.class.getResource("/").getPath()+path;
    }

    public static void main(String[] args) {
        System.out.println(getFileFromResource("aa"));
    }
}
