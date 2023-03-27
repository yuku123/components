package com.zifang.util.core.lang.beans.tuples;

import lombok.Data;

/**
 * @author zifang
 */
@Data
public class Quartet<A, B, C, D> extends Triplet<A, B, C> {
    protected D d;

    public Quartet(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }
}
