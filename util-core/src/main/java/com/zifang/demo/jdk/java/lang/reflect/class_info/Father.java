package com.zifang.demo.jdk.java.lang.reflect.class_info;

public class Father {
    public String father_public_field;
    protected String father_protected_field;
    String father_default_field;
    private String father_private_field;
    public Father(){
        System.out.println("Father class's construction");
    }
    public Father(String args){
        System.out.println("Father class's construction of ("+args+")");
    }
}
