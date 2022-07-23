package com.zifang.util.core.lang.tuples;

import lombok.Data;

/**
 * @author zifang
 */
@Data
public class Pair<A, B> extends Unit<A> {
    protected B b;

    public Pair(A a, B b) {
        super(a);
        this.b = b;
    }

    @Override
    public String toString() {
        return a + ":" + b;
    }

    @Override
    public boolean equals(Object o) {
        if (this.toString().equals(o.toString())) {
            return true;
        }
        return false;
    }
}
