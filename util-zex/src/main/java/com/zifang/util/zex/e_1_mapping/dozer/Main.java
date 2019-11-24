package com.zifang.util.zex.e_1_mapping.dozer;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class Main {
    public static void main(String[] args) {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        User sourceObject = new User();
        sourceObject.setPassword("pass");
        sourceObject.setUsername("user");

        User user = mapper.map(sourceObject, User.class);
        System.out.println(user);
    }
}
