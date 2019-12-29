package com.zifang.util.core.lang;

public class Strings {

    public static String repeat(String element,Integer times){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i<times;i++){
            sb.append(element);
        }
        return sb.toString();
    }
}
