package com.zifang.demo.jdk.java.util.concurent.analy;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * show unsafe 实力类都能帮助我们干什么事情
 *
 * 1. 实例化一个类
 * 2. 修改私有字段的值
 * 3. 使用堆外内存
 * 4. CompareAndSwap操作
 * 5. park/unpark
 *      当一个线程正在等待某个操作时，JVM调用Unsafe的park()方法来阻塞此线程
 *      当阻塞中的线程需要再次运行时，JVM调用Unsafe的unpark()方法来唤醒此线程
 * */
public class UnsafeDemo {

    private volatile int count = 0;

    private static long offset;


    @Test
    //只会分配内存给这个类，而不会去调用构造方法，因此打印出来age为1
    public void createInstanceByUnsafe() throws InstantiationException {
        Unsafe unsafe = UnsafeUtil.getUnsageInstance();
        User user2 = (User) unsafe.allocateInstance(User.class);
        System.out.println(user2.age);
    }


    @Test
    //只会分配内存给这个类，而不会去调用构造方法，因此打印出来age为1
    public void modifyInstanceFieldByUnsafe() throws InstantiationException, NoSuchFieldException {
        Unsafe unsafe = UnsafeUtil.getUnsageInstance();
        User user = new User();
        Field age = user.getClass().getDeclaredField("age");
        unsafe.putInt(user, unsafe.objectFieldOffset(age), 20);

        // 打印20
        System.out.println(user.getAge());
    }


    @Test
    //只会分配内存给这个类，而不会去调用构造方法，因此打印出来age为1
    public void cas() throws InstantiationException, NoSuchFieldException {
        Unsafe unsafe = UnsafeUtil.getUnsageInstance();
        offset = unsafe.objectFieldOffset(UnsafeDemo.class.getDeclaredField("count"));
    }

    public void increment(Unsafe unsafe) {
        int before = count;
        // 失败了就重试直到成功为止
        while (!unsafe.compareAndSwapInt(this, offset, before, before + 1)) {
            before = count;
        }
    }

    public static class User {
        int age;

        public User() {
            this.age = 10;
        }

        public int getAge() {
            return age;
        }
    }
}
