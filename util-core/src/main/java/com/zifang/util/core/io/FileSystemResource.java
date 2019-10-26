package com.zifang.util.core.io;

import com.zifang.util.core.util.FileUtil;

import java.io.*;

public class FileSystemResource {
    public static void main(String[] args) throws IOException {
        String s ="/Users/zifang/workplace/idea_workplace/components/util-workflow/src/main/java/workflow.json";

        String json = FileUtil.getFileContent(s);
        System.out.println(json);

    }
}
