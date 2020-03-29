package com.zifang.util.core.lang.tuples;

public class Pair<A,B> extends Unit<A> {
    protected B b;
    public Pair(A a,B b){
        super(a);
        this.b = b;
    }
}
