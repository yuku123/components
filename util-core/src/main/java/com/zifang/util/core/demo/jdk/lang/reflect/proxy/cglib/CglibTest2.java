package com.zifang.util.core.demo.jdk.lang.reflect.proxy.cglib;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.Test;

public class CglibTest2 {

    @Test
    public void testCglib() {
        DaoProxy daoProxy = new DaoProxy();
        DaoAnotherProxy daoAnotherProxy = new DaoAnotherProxy();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallbacks(new Callback[]{daoProxy, daoAnotherProxy, NoOp.INSTANCE});
        enhancer.setCallbackFilter(new DaoFilter());
        //如果构造器内也进行调用，则拦截，如果不拦截的话要显示地设置值
        //enhancer.setInterceptDuringConstruction(false);


        Dao dao = (Dao)enhancer.create();
        dao.update();
        dao.select();
    }

    public static void main(String[] args) {
        CglibTest2 cglibTest2 = new CglibTest2();
        cglibTest2.testCglib();
    }

}