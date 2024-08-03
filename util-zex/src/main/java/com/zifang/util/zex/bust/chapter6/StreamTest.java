package com.zifang.util.zex.bust.chapter6;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    @Test
    public void test001(){

        List<List<Integer>> list1 = Arrays.asList(
                Arrays.asList(3,1,5,9,101),
                Arrays.asList(13,15,11,99,99)
        );

        List<Integer> out = list1.stream()
                .flatMap(e->e.stream()) // 将列表数据内的列表打平为元素
                .filter(e->e < 100) // 过滤，将流内100的值剔除
                .peek(e->System.out.println("遍历数据："+e)) // peek可以遍历数据而不截断
                .distinct() // 将两个99 变为一个99
                .sorted() // 进行排序
                .limit(5) // 仅选取5个
                .map(e->e +1) // 遍历映射，可以对数据进行加工产生新的流
                .collect(Collectors.toList()); // 从流产生列表数据
        System.out.println(out);
    }

    @Test
    public void test002(){

        List<Integer> list = Arrays.asList(1,2,3,4,5);

        System.out.println("Min:"+ list.stream().min(Integer::compareTo));
        System.out.println("Max:"+ list.stream().max(Integer::compareTo));
        System.out.println("count:"+ list.stream().count());
        System.out.println("allMatch: <=5 :"+list.stream().allMatch(e->e <=5));
        System.out.println("allMatch: <=4 :"+list.stream().allMatch(e->e <=4));

        System.out.println("anyMatch: >=5 :"+list.stream().anyMatch(e->e >=5));
        System.out.println("anyMatch: >=6 :"+list.stream().anyMatch(e->e >=6));

        System.out.println("noneMatch: ==5 :"+list.stream().noneMatch(e->e ==5));
        System.out.println("noneMatch: ==6 :"+list.stream().noneMatch(e->e ==6));

        System.out.println("findFirst: ==6 :"+list.stream().findFirst());
        System.out.println("findAny: ==6 :"+list.stream().findAny());
    }
}
