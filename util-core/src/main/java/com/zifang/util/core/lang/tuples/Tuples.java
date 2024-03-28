package com.zifang.util.core.lang.tuples;

public class Tuples {

     public static <A> Unit<A> of(A a){
         return new Unit<>(a);
     }

    public static <A,B> Pair<A,B> of(A a,B b){
        return new Pair<>(a,b);
    }

    public static <A,B,C> Triplet<A,B,C> of(A a,B b,C c){
        return new Triplet<>(a,b,c);
    }
}
