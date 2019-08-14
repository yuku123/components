package com.zifang.util.core.base.primitives;

public class Strings {

    public static String fill(Integer count,Character c){
        char[] characters = new char[count];
        for(int i = 0 ; i<characters.length;i++){
            characters[i]=c;
        }
        return new String(characters);
    }
}
