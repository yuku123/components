package com.zifang.util.core.lang.object.tuples;

import lombok.Data;

@Data
public class Triplet<A,B,C> extends Pair<A,B>{

    protected C c;

    public Triplet(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }
}
