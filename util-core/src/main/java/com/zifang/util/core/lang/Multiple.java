package com.zifang.util.core.lang;

/**
 * 固化2的次方倍数
 * */
public enum Multiple {

    Multiple_1(1<<1),
    Multiple_2(1<<2),
    Multiple_3(1<<3),
    Multiple_4(1<<4),
    Multiple_5(1<<5),
    Multiple_6(1<<6),
    Multiple_7(1<<7),
    Multiple_8(1<<8);

    private int i;

    Multiple(int i){
        this.i = i;
    }

}
