package com.zifang.util.core.beans.converter;

public class Converter<A,B> {

    private Class<A> clazz;
    private Class<B> target;

    public Converter(Class<A> clazz,Class<B> target){
        this.clazz = clazz;
        this.target = target;
    }

    public B doConvert(Object a,Object b){
        Byte.parseByte("");
        Short.parseShort("");
        return null;
    }

    public static void main(String[] args) {
        for(int i = 0;i<100;i++){
            System.out.println(((char)i)+":"+i);
        }
    }
}
