package com.win.algorithm.bloomfilter;

import java.util.BitSet;

public class SimpleBloomFilter {
	// 默认大小，一般取记录最大数的32倍
	private final int defaultSize;
	private final int[] seeds = new int[] { 7, 11, 13, 31, 37, 61,67 };
	private BitSet bits;
	private SimpleHash[] func = new SimpleHash[seeds.length];

	public static void main(String[] args) {
		String value = "stone2083@yahoo.cn";
		SimpleBloomFilter filter = new SimpleBloomFilter(10);
		filter.add("stone2083@yahoo.cn");
		filter.add("stone2081@yahoo.cn");
		filter.add("ston2e2081@yahoo.cn");
		filter.add("stone32081@yahoo.cn");
		filter.add("stoan2e2081@yahoo.cn");
		filter.add("stoadne2081@yahoo.cn");
		filter.add("stone24081@yahoo.cn");
		filter.add("ston5e2081@yahoo.cn");
		filter.add("stodne2081@yahoo.cn");
		filter.add("stoqerne2081@yahoo.cn");
		filter.add("stoerqerne2081@yahoo.cn");
		filter.add("stoqerne2081@yaqerhoo.cn");
		filter.add("stone2081@qryahqroo.cn");
		filter.add("stone2081@afdyahoo.cn");
		filter.add("stone2081@adfyahoo.cn");
		filter.add("stone2081@afa.cn");
		filter.add("stone2081@yahodafo.cn");
		System.out.println(filter.contains(value));
		filter.add(value);
		System.out.println(filter.contains(value));
	}

	public SimpleBloomFilter(int itemSize) {
		defaultSize = itemSize << 4;
		bits = new BitSet(defaultSize);
		for (int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(defaultSize, seeds[i]);
		}
	}

	// 覆盖方法，把URL 添加进来
	public void add(CrawlUrl value) {
		if (value != null) {
			add(value.getOriUrl());
		}
	}

	// 覆盖方法，把URL 添加进来
	public void add(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}
	}

	// 覆盖方法，是否包含URL
	public boolean contains(CrawlUrl value) {
		return contains(value.getOriUrl());
	}

	// 覆盖方法，是否包含URL
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
}