package com.zifang.util.zex.bust.chapter4.enumCase;

public class HungryLevelEnumTest {
    public static void main(String[] args) {
        Enum.valueOf(HungryLevelEnum.class, "HUNGRY_LEVEL_1");
        System.out.println(HungryLevelEnum.HUNGRY_LEVEL_1);
        System.out.println(HungryLevelEnum.HUNGRY_LEVEL_2);
        System.out.println(HungryLevelEnum.HUNGRY_LEVEL_3);

        assert HungryLevelEnum.valueOf("HUNGRY_LEVEL_1") == HungryLevelEnum.HUNGRY_LEVEL_1;


    }
}
