package com.zifang.util.zex.bust.chapter11.case2;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TransformInputStreamOutputStream {
    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream("XXX.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //out写的时候，每次写1024个字节，如果in有2048个字节数，则读2048/1024=2次
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0){
            out.write(buffer, 0, len);
        }
    }
}
