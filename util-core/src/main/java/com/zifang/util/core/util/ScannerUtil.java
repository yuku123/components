package com.zifang.util.core.util;

import java.util.Scanner;
import java.util.function.Function;

public class ScannerUtil {
    public static void scanner(Function<String,String> transformHandler){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            System.out.println(transformHandler.apply(sc.next()));
        }
    }
}
