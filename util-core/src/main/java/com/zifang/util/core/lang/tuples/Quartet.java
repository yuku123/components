package com.zifang.util.core.lang.tuples;

import lombok.Data;

@Data
public class Quartet<A, B, C, D> extends Triplet<A, B, C> {
    protected D d;

    public Quartet(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }
}
