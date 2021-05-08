package com.zifang.util.core.lang.tuples;

import lombok.Data;

@Data
public class Septet<A, B, C, D, E, F, G> extends Sextet<A, B, C, D, E, F> {
    protected G g;

    public Septet(A a, B b, C c, D d, E e, F f, G g) {
        super(a, b, c, d, e, f);
        this.g = g;
    }
}
