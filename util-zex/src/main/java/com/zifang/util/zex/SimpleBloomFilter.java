package com.zifang.util.zex;

import java.util.BitSet;

/**
 * 布隆过滤器
 *
 * 在数据量比较大的情况下，既满足时间要求，又满足空间的要求。即我们需要一个时间和空间消耗都比较小的数据结构和算法。Bloom Filter就是一种解决方案。
 *
 * BloomFilter实现的另一个重点就是怎么利用hash函数把数据映射到bit数组中。Guava的实现是对元素通过MurmurHash3计算hash值，将得到的hash值取高8个字节以及低8个字节进行计算，以得当前元素在bit数组中对应的多个位置。MurmurHash3算法详见:Murmur哈希，于2008年被发明。这个算法hbase,redis,kafka都在使用。
 *
 *
 * https://www.cnblogs.com/z941030/p/9218356.html
 *
 * */
public class SimpleBloomFilter {
	private static final int DEFAULT_SIZE = 2 << 24;
	private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	private SimpleHash[] func = new SimpleHash[seeds.length];

	public static void main(String[] args) {
		String value = "stone2083@yahoo.cn";
		SimpleBloomFilter filter = new SimpleBloomFilter();
		System.out.println(filter.contains(value));
		filter.add(value);
		System.out.println(filter.contains(value));
	}

	public SimpleBloomFilter() {
		for (int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
	}

	public void add(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}
	}

	public boolean contains(String value) {
		if (value == null) {
			return false;
		}
		boolean ret = true;
		for (SimpleHash f : func) {
			ret = ret && bits.get(f.hash(value));
		}
		return ret;
	}

	public static class SimpleHash {
		private int cap;
		private int seed;

		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;
		}

		public int hash(String value) {
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}
	}

	/**
	 * 计算 Bloom Filter的bit位数m
	 *
	 * <p>See http://en.wikipedia.org/wiki/Bloom_filter#Probability_of_false_positives for the
	 * formula.
	 *
	 * @param n 预期数据量
	 * @param p 误判率 (must be 0 < p < 1)
	 */
	static long optimalNumOfBits(long n, double p) {
		if (p == 0) {
			p = Double.MIN_VALUE;
		}
		return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
	}

	/**
	 * 计算最佳k值，即在Bloom过滤器中插入的每个元素的哈希数
	 *
	 * <p>See http://en.wikipedia.org/wiki/File:Bloom_filter_fp_probability.svg for the formula.
	 *
	 * @param n 预期数据量
	 * @param m bloom filter中总的bit位数 (must be positive)
	 */
		static int optimalNumOfHashFunctions(long n, long m) {
		// (m / n) * log(2), but avoid truncation due to division!
		return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
	}
}
