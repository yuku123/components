package com.zifang.util.core;

import com.zifang.util.core.io.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class FileUtilTest {

    @Test
    public void test1() throws IOException {
        String a = FileUtil.readString(FileUtil.class.getResource("/music.json"), "utf-8");
        System.out.println(a);
    }

    @Test
    public void test2() throws IOException {

        String base = "Z:\\学习\\小说广播\\小说";

        doFolder(base);


    }

    private void doFolder(String base) {
        File[] files = new File(base).listFiles();
        for (File file : files) {
            if(file.isDirectory()){
                doFolder(file.getAbsolutePath());
            } else {
                if(file.getName().contains("(1)")){
                    file.delete();
                    System.out.println("delete:" +file.getAbsolutePath());
                }
            }
        }
    }
}
