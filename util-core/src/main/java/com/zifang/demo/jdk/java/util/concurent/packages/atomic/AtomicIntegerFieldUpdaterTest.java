package com.zifang.demo.jdk.java.util.concurent.packages.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterTest {

    private static Class<Person> cls;
    /**
     * AtomicIntegerFieldUpdater说明
     * 基于反射的实用工具，可以对指定类的指定 volatile int 字段进行原子更新。此类用于原子数据结构，
     * 该结构中同一节点的几个字段都独立受原子更新控制。
     * 注意，此类中 compareAndSet 方法的保证弱于其他原子类中该方法的保证。
     * 因为此类不能确保所有使用的字段都适合于原子访问目的，所以对于相同更新器上的 compareAndSet 和 set 的其他调用，
     * 它仅可以保证原子性和可变语义。
     * @param args
     */
    public static void main(String[] args) {
        // 新建AtomicLongFieldUpdater对象，传递参数是“class对象”和“long类型在类中对应的名称”
        AtomicIntegerFieldUpdater<Person> mAtoLong = AtomicIntegerFieldUpdater.newUpdater(Person.class, "id");
        Person person = new Person(12345);
        mAtoLong.compareAndSet(person, 12345, 1000);
        System.out.println("id="+person.getId());
    }
}

class Person {
    volatile int id;
    public Person(int id) {
        this.id = id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}