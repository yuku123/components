package com.zifang.util.core.collections;

import java.util.Arrays;
import java.util.List;

public class Lists {

    /**
     * @param t1 the elements you appended need to be transform to the list
     * @return
     * */
    public static <T> List<T> of(T... t1){
        return Arrays.asList(t1);
    }

    public static List<String> of(String content,String splitor){
        return Arrays.asList(content.split(splitor));
    }

}
