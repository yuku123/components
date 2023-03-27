package com.zifang.util.core.lang.beans.tuples;

import lombok.Data;

@Data
public class Sextet<A, B, C, D, E, F> extends Quintet<A, B, C, D, E> {

    protected F f;

    public Sextet(A a, B b, C c, D d, E e, F f) {
        super(a, b, c, d, e);
        this.f = f;
    }
}
