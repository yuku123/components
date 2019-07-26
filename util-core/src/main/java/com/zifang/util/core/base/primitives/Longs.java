package com.zifang.util.core.base.primitives;

public class Longs {

    /**
     * byte数组 转 long
     */

    public static long of(byte[] bytes) {
        long longa = 0;
        for (int i = 0; i < bytes.length; i++)
            longa += (long) ((bytes[i] & 0xff) << i * 8); // 移位和清零

        return longa;
    }

}
