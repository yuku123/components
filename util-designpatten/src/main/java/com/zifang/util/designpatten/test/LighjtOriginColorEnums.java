package com.zifang.util.designpatten.test;

public enum LighjtOriginColorEnums {
    RED("s"),
    YELLOW("s"),
    BLUE("s");

    LighjtOriginColorEnums(String a){
        System.out.println("aa");
    }
    public static void main(String[] args){
        for(LighjtOriginColorEnums ele : LighjtOriginColorEnums.values()){
            System.out.println(ele + " int value is: " + ele.ordinal());
        }
    }
}