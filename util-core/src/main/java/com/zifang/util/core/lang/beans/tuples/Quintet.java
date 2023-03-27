package com.zifang.util.core.lang.beans.tuples;

import lombok.Data;

/**
 * @author zifang
 */
@Data
public class Quintet<A, B, C, D, E> extends Quartet<A, B, C, D> {
    protected E e;

    public Quintet(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        this.e = e;
    }
}
