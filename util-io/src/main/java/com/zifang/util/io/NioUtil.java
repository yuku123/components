package com.zifang.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioUtil {


    public static void show(Buffer buffer){
        System.out.println("capacity:"+buffer.capacity());
        System.out.println("limit:"+buffer.limit());
        System.out.println("position:"+buffer.position());
        //System.out.println("mark:"+buffer.mark());
    }
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1,2,3,4});
        //ByteBuffer.
        //NioUtil.show(byteBuffer.putInt(1));
        System.out.println(byteBuffer.get(0));
    }

    public static void s(){
        try {
            FileChannel fileChannel = new FileOutputStream(new File("aa")).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
