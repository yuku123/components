package com.zifang.util.crawler.konachar;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/zifang/Downloads/konacha/konachar-all.txt");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        Map<Integer,BufferedWriter> bufferedWriters = new LinkedHashMap<>();
        String line = null;
        int i = 0;
        while ((line = bufferedReader.readLine())!=null){
            i = i+1;
            int index = i / 100;
            BufferedWriter bufferedWriter = bufferedWriters.get(index);
            if(bufferedWriter == null){
                String filePath = "/Users/zifang/Downloads/konacha/konachar-markdown#index.md";
                filePath = filePath.replace("#index",index+"");
                bufferedWriter = new BufferedWriter(new FileWriter(filePath));
                bufferedWriters.put(index,bufferedWriter);
            }
            String d = String.format("![](%s)",line);
            bufferedWriter.write(d+"\n");
            bufferedWriter.flush();
        }
    }
}
