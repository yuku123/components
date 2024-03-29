package com.zifang.util.proxy.proxy;

import com.zifang.util.proxy.aspects.Aspect;

import java.io.Serializable;

/**
 * 代理工厂<br>
 * 根据用户引入代理库的不同，产生不同的代理对象
 */
public abstract class ProxyFactory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建代理
     *
     * @param <T>    代理对象类型
     * @param target 被代理对象
     * @param aspect 切面实现
     * @return 代理对象
     */
    public abstract <T> T proxy(T target, Aspect aspect);

    /**
     * 根据用户引入Cglib与否自动创建代理对象
     *
     * @param <T>         切面对象类型
     * @param target      目标对象
     * @param aspectClass 切面对象类
     * @return 代理对象
     */
    public static <T> T createProxy(T target, Class<? extends Aspect> aspectClass) {
        try {
            return createProxy(target, aspectClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户引入Cglib与否自动创建代理对象
     *
     * @param <T>    切面对象类型
     * @param target 被代理对象
     * @param aspect 切面实现
     * @return 代理对象
     */
    public static <T> T createProxy(T target, Aspect aspect) {
        return create().proxy(target, aspect);
    }

    /**
     * 根据用户引入Cglib与否创建代理工厂
     *
     * @return 代理工厂
     */
    public static ProxyFactory create() {
        try {
            return new CglibProxyFactory();
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        return new JdkProxyFactory();
    }
}
