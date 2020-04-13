package com.zifang.util.zex.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class aa {
    public static void main(String[] args) throws IOException {

        BufferedWriter b = new BufferedWriter(new FileWriter(new File("a.csv")));
        byte[] a = new byte[100000000];

        for(int i = 2;i<a.length;i++){
            if(a[i]==1){
                continue;
            }
            for(int j = 2;j<100000;j++){
                if(i*j <=a.length-1){
                    a[i*j] = 1;
                }else{
                    break;
                }
            }
        }
        for(int i = 0;i<a.length;i++){
            if(a[i]==0){
                b.write(i+"\n");
                b.flush();
                //System.out.println(i);
            }
        }
    }
}
