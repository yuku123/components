package com.zifang.util.core.common.utils;

import java.util.Scanner;

public class A {
    //减压利器
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            System.out.println("sb.append(\""+scanner.nextLine()+"\\n\");");
        }
    }
}
