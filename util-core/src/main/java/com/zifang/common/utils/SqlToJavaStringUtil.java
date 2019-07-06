package com.zifang.common.utils;

import java.util.Scanner;

public class SqlToJavaStringUtil {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String content = "sb.append(\"##\\n\");";
            System.out.println(content.replace("##",scanner.nextLine()));
        }
    }
}
