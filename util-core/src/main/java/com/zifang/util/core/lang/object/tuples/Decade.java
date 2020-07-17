package com.zifang.util.core.lang.object.tuples;

import lombok.Data;

@Data
public class Decade<A,B,C,D,E,F,G,H,I,J> extends Ennead<A,B,C,D,E,F,G,H,I>{
    protected J j;

    public Decade(A a, B b, C c, D d, E e, F f, G g, H h, I i,J j) {
        super(a, b, c, d, e, f, g, h, i);
        this.j=j;
    }
}
