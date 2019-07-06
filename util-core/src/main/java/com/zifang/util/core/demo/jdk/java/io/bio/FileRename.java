package com.zifang.util.core.demo.jdk.java.io.bio;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileRename {
    public static void main(String[] args) {
        String baseFold = "/Users/zifang/Downloads/Java架构师进阶书籍推荐/linux/";
        File base = new File(baseFold);
        List<File> subList = Arrays.asList(base.listFiles());
        for(File subFile : subList){
            String fileName = subFile.getName();
            String modified = fileName.replace("Linux综合教程","");
            String fullModify = baseFold + modified;
            subFile.renameTo(new File(fullModify));
            System.out.println(subFile.getName());
        }
    }
}
