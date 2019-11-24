package com.zifang.util.zex.demo.jdk.lang.reflect.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

public class CglibTest {

    @Test
    public void testCglib() {
        DaoProxy daoProxy = new DaoProxy();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallback(daoProxy);

        Dao dao = (Dao)enhancer.create();


        dao.update();
        dao.select();
    }
}
