package com.zifang.util.zex.bust.chapter11.case1;

import java.io.FileOutputStream;
import java.io.IOException;

public class WriteFile {
    public static void main(String[] args) throws IOException {
        FileOutputStream fout = null;
        try {
            String filePath = "/Users/zifang/workplace/idea_workplace/JavaBust/src/main/resources/test_out.txt";
            //创建字节输入流
            fout = new FileOutputStream(filePath);
            fout.write("第一行".getBytes());
            fout.write("第二行".getBytes());
            fout.write("第三行".getBytes());
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fout.close();
        }
    }
}
