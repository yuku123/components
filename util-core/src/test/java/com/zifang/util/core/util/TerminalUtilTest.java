package com.zifang.util.core.util;

import java.io.IOException;

public class TerminalUtilTest {

    //@Test
    public void test1() throws IOException {
        System.out.println(TerminalUtil.runAndGetReturn("python3 /home/zifang/workplace/idea_workplace/aa.py 1 2"));
    }

}
