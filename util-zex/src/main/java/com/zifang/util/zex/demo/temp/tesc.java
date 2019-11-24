package com.zifang.util.zex.demo.temp;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class tesc {
    public static void main(String[] args) {
       FileSystemResourceLoader fileSystemResourceLoader =  new FileSystemResourceLoader();
       Resource resource = fileSystemResourceLoader.getResource("file:/Users/zifang/learn_espanol_script.sh");
        if(resource.exists()){
           try {
               System.out.println(resource.contentLength());
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
}
