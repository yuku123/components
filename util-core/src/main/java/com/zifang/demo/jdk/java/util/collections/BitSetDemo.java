package com.zifang.demo.jdk.java.util.collections;

import org.junit.Test;

import java.util.BitSet;

/**
 * BitSet 类创建一个特殊类型的数组保存位值。该BitSet中数组的大小可以根据需要增加。这使得它类似于比特的向量。
 *
 * 这是一个传统类，但它已被完全重新设计在Java 2，1.4版本。
 *
 * BitSet 定义了两个构造函数。第一个版本创建一个默认的对象：
 *
 * BitSet() Java2 版本允许指定它的初始大小，即比特，它可以容纳的数量。所有位初始化为零。
 *
 * BitSet(int size)
 *
 *
 *
 */
public class BitSetDemo {

	@Test
	public void test1() {
		BitSet bits1 = new BitSet(16);
		BitSet bits2 = new BitSet(16);

		// set some bits
		for (int i = 0; i < 16; i++) {
			if ((i % 2) == 0)
				bits1.set(i);
			if ((i % 5) != 0)
				bits2.set(i);
		}
		System.out.println("Initial pattern in bits1: ");
		System.out.println(bits1);
		System.out.println("Initial pattern in bits2: ");
		System.out.println(bits2);

		// AND bits
		bits2.and(bits1);
		System.out.println("bits2 AND bits1: ");
		System.out.println(bits2);

		// OR bits
		bits2.or(bits1);
		System.out.println("bits2 OR bits1: ");
		System.out.println(bits2);

		// XOR bits
		bits2.xor(bits1);
		System.out.println("bits2 XOR bits1: ");
		System.out.println(bits2);
	}

	/**
	 * BitSet 默认值是64位，如果你要用的位超过了默认size,它会再申请64位，而不是报错。
	 * long类型默认64位
	 */
	@Test
	public void test2(){
		BitSet bm=new BitSet();  
        System.out.println(bm.isEmpty()+"--"+bm.size());  
        bm.set(0);  
        System.out.println(bm.isEmpty()+"--"+bm.size());  
        bm.set(1);  
        System.out.println(bm.isEmpty()+"--"+bm.size());  
        System.out.println(bm.get(65));  
        System.out.println(bm.isEmpty()+"--"+bm.size());  
        bm.set(65);  
        System.out.println(bm.isEmpty()+"--"+bm.size());  
	}
}