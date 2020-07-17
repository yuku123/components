package com.zifang.util.core.lang.object.tuples;

import lombok.Data;

@Data
public class Ennead<A,B,C,D,E,F,G,H,I> extends Octet<A, B, C, D, E, F, G, H> {
    protected I i;
    public Ennead(A a, B b, C c, D d, E e, F f, G g, H h,I i) {
        super(a, b, c, d, e, f, g, h);
        this.i = i;
    }
}
