package com.zifang.demo.jdk.java.util.collections;

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
 */
public class BitSetDemo3 {


	/**
	 * java.spi.BitSet.andNot(BitSet set) 方法清除所有，其相应的位在指定的BitSet设置位。
	 */
	public static void testAndNot(){
		// create 2 bitsets
	      BitSet bitset1 = new BitSet(8);
	      BitSet bitset2 = new BitSet(8);

	      // assign values to bitset1
	      bitset1.set(0);
	      bitset1.set(1);
	      bitset1.set(2);
	      bitset1.set(3);
	      bitset1.set(4);
	      bitset1.set(5);

	      // assign values to bitset2
	      bitset2.set(2);
	      bitset2.set(4);
	      bitset2.set(6);
	      bitset2.set(8);
	      bitset2.set(10);

		  // print the sets
	      System.out.println("Bitset1:" + bitset1);
	      System.out.println("Bitset2:" + bitset2);

	      // perform andNot operation for the two  bitsets
	      bitset1.andNot(bitset2);

	      // print the new bitset1
	      System.out.println("" + bitset1);
	}

	/**
	 * java.spi.BitSet.cardinality() 方法返回在此BitSet设置为true数。
	 */
	public static void testCardinality(){
		// create 2 bitsets
	      BitSet bitset1 = new BitSet(8);
	      BitSet bitset2 = new BitSet(8);

	      // assign values to bitset1
	      bitset1.set(0);
	      bitset1.set(1);
	      bitset1.set(2);
	      bitset1.set(3);
	      bitset1.set(4);
	      bitset1.set(5);

	      // assign values to bitset2
	      bitset2.set(2);
	      bitset2.set(4);
	      bitset2.set(6);
	      bitset2.set(8);
	      bitset2.set(10);

		  // print the sets
	      System.out.println("Bitset1:" + bitset1);
	      System.out.println("Bitset2:" + bitset2);

	      // print cardinality for bitset1
	      System.out.println("" + bitset1.cardinality());

	      // print cardinality for bitset2
	      System.out.println("" + bitset2.cardinality());
	}

	/**
	 * 是否有交集
	 */
	public static void testIntersects(){
		// create 2 bitsets
	      BitSet bitset1 = new BitSet(8);
	      BitSet bitset2 = new BitSet(8);

	      // assign values to bitset1
	      bitset1.set(0);
	      bitset1.set(1);
	      bitset1.set(2);
	      bitset1.set(3);
	      bitset1.set(4);
	      bitset1.set(5);

	      // assign values to bitset2
	      bitset2.set(2);
	      bitset2.set(4);
	      bitset2.set(6);
	      bitset2.set(8);
	      bitset2.set(10);

	      // print the sets
	      System.out.println("Bitset1:" + bitset1);
	      System.out.println("Bitset2:" + bitset2);

	      // check if bitset1 intersects with bitset2
	      System.out.println("" + bitset1.intersects(bitset2));
	}

	/**
	 * 方法返回此BitSet的“逻辑大小”：在BitSet中最高设置位加一的索引。如果BitSet中没有包含的位，返回0。
	 * 返回最大数所在的位置
	 */
	public static void testLength(){
		// create 2 bitsets
	      BitSet bitset1 = new BitSet(8);
	      BitSet bitset2 = new BitSet(8);

	      // assign values to bitset1
	      bitset1.set(0);
	      bitset1.set(1);
	      bitset1.set(2);
	      bitset1.set(3);
	      bitset1.set(4);
	      bitset1.set(5);

	      // assign values to bitset2
	      bitset2.set(2);
	      bitset2.set(4);
	      bitset2.set(6);
	      bitset2.set(8);
	      bitset2.set(10);

	      // print the sets
	      System.out.println("Bitset1:" + bitset1);
	      System.out.println("Bitset2:" + bitset2);

	      // print the length of each bitset
	      System.out.println("" + bitset1.length());
	      System.out.println("" + bitset2.length());
	}

	/**
	 * java.spi.BitSet.nextClearBit(int fromIndex) 方法返回第一个位出现或之后指定的起始索引被设置为false的索引。
	 */
	public static void testNextClearBit(){
		// create 2 bitsets
	      BitSet bitset1 = new BitSet(8);
	      BitSet bitset2 = new BitSet(8);

	      // assign values to bitset1
	      bitset1.set(0);
	      bitset1.set(1);
	      bitset1.set(2);
	      bitset1.set(3);
	      bitset1.set(4);
	      bitset1.set(5);

	      // assign values to bitset2
	      bitset2.set(2);
	      bitset2.set(4);
	      bitset2.set(6);
	      bitset2.set(8);
	      bitset2.set(10);

	      // print the sets
	      System.out.println("Bitset1:" + bitset1);
	      System.out.println("Bitset2:" + bitset2);

	      // print the first clear bit of bitset1
	      System.out.println("" + bitset1.nextClearBit(0));

	      // print the first clear bit of bitset2 after index 5
	      System.out.println("" + bitset2.nextClearBit(5));
	}
	
	public static void main(String[] args) {
//		testAndNot();
//		testCardinality();
//		testIntersects();
//		testLength();
		testNextClearBit();
	}
}