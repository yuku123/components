package com.zifang.util.core.system;

import java.util.Scanner;
import java.util.function.Function;

/**
 * @author zifang
 */
public class ScannerUtil {

    /**
     * @param transformHandler 吃饭
     */
    public static void scanner(Function<String, String> transformHandler) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            System.out.println(transformHandler.apply(sc.next()));
        }
    }
}
