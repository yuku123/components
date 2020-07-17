package com.zifang.util.core.lang.object.tuples;

import lombok.Data;

@Data
public class Pair<A,B> extends Unit<A> {
    protected B b;
    public Pair(A a,B b){
        super(a);
        this.b = b;
    }
}
