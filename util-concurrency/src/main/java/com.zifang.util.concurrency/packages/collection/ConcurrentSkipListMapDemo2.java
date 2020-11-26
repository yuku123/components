package com.zifang.util.concurrency.packages.collection;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapDemo2 {

	public static void main(String[] args) {
		ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
		map.put("ab", "ab");
		map.put("1ab", "1ab");
		map.put("acb", "acb");
		map.put("abe", "abe");
		map.put("abd", "abd");
		
		NavigableSet<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println("key:"+key+"_value:"+map.get(key));
		}
		
	}
}
