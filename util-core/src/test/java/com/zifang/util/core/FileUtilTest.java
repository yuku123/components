package com.zifang.util.core;

import com.zifang.util.core.io.FileUtil;
import org.junit.Test;

import java.io.IOException;

public class FileUtilTest {

    @Test
    public void test1() throws IOException {
        String a = FileUtil.readString(FileUtil.class.getResource("/music.json"), "utf-8");
        System.out.println(a);
    }
}
