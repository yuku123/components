package com.zifang.util.core.resource;

public class ResourceUtil {
    public static String getFileFromResource(String path){
        return ResourceUtil.class.getResource("/").getPath()+path;
    }

    public static void main(String[] args) {
        System.out.println(getFileFromResource("aa"));
    }
}
