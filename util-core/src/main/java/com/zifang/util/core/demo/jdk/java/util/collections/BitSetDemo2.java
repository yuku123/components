package com.zifang.util.core.demo.jdk.java.util.collections;

import java.util.BitSet;

/**
 * java.spi.BitSet 研究（存数海量数据时的一个途径）
 *
 * java.spi.BitSet可以按位存储。
 * 计算机中一个字节（byte）占8位（bit），我们java中数据至少按字节存储的，
 * 比如一个int占4个字节。 如果遇到大的数据量，这样必然会需要很大存储空间和内存。
 * 如何减少数据占用存储空间和内存可以用算法解决。
 * java.spi.BitSet就提供了这样的算法。
 * 比如有一堆数字，需要存储，source=[3,5,6,9] 用int就需要4*4个字节。
 * java.spi.BitSet可以存true/false。
 * 如果用java.spi.BitSet，则会少很多，
 * 其原理是：
 * 	1，先找出数据中最大值maxvalue=9
 * 	2，声明一个BitSet bs,它的size是maxvalue+1=10
 * 	3，遍历数据source，bs[source[i]]设置成true.
 * 最后的值是： (0为false;1为true)
 * 	bs[0,0,0,1,0,1,1,0,0,1] 3, 5,6, 9
 * 	这样一个本来要int型需要占4字节共32位的数字现在只用了1位！ 比例32:1
 * 	这样就省下了很大空间。
 *
 *
 *
 */
public class BitSetDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] shu = { 2, 42, 5, 6, 6, 18, 33, 15, 25, 31, 28, 37 };
		BitSet bm1 = new BitSet(BitSetDemo2.getMaxValue(shu));
		System.out.println("bm1.size()--" + bm1.size());

		BitSetDemo2.putValueIntoBitSet(shu, bm1);
		printBitSet(bm1);
	}

	// 初始全部为false，这个你可以不用，因为默认都是false
	public static void initBitSet(BitSet bs) {
		for (int i = 0; i < bs.size(); i++) {
			bs.set(i, false);
		}
	}

	// 打印
	public static void printBitSet(BitSet bs) {
		StringBuffer buf = new StringBuffer();
		buf.append("[\n");
		for (int i = 0; i < bs.size(); i++) {
			if (i < bs.size() - 1) {
				buf.append(BitSetDemo2.getBitTo10(bs.get(i)) + ",");
			} else {
				buf.append(BitSetDemo2.getBitTo10(bs.get(i)));
			}
			if ((i + 1) % 8 == 0 && i != 0) {
				buf.append("\n");
			}
		}
		buf.append("]");
		System.out.println(buf.toString());
	}

	// 找出数据集合最大值
	public static int getMaxValue(int[] zu) {
		int temp = 0;
		temp = zu[0];
		for (int i = 0; i < zu.length; i++) {
			if (temp < zu[i]) {
				temp = zu[i];
			}
		}
		System.out.println("maxvalue:" + temp);
		return temp;
	}

	// 放值
	public static void putValueIntoBitSet(int[] shu, BitSet bs) {
		for (int i = 0; i < shu.length; i++) {
			bs.set(shu[i], true);
		}
	}

	// true,false换成1,0为了好看
	public static String getBitTo10(boolean flag) {
		String a = "";
		if (flag == true) {
			return "1";
		} else {
			return "0";
		}
	}
}
