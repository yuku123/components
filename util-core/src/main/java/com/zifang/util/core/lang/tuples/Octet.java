package com.zifang.util.core.lang.tuples;

import lombok.Data;

/**
 * @author zifang
 */
@Data
public class Octet<A, B, C, D, E, F, G, H> extends Septet<A, B, C, D, E, F, G> {

    protected H h;

    public Octet(A a, B b, C c, D d, E e, F f, G g, H h) {
        super(a, b, c, d, e, f, g);
        this.h = h;
    }
}
