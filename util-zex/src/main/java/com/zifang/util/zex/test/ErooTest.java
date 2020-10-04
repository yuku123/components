package com.zifang.util.zex.test;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ErooTest {
    public static void main(String[] args) {

        List<User> deleteList = ObjectUtils.defaultIfNull(getList(), new ArrayList<>());
        System.out.println(deleteList);
    }

    private static ArrayList<User> getList() {
        return new ArrayList<>();
    }
}

class User{

}
