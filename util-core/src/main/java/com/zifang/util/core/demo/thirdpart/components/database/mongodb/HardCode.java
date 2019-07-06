package com.zifang.util.core.demo.thirdpart.components.database.mongodb;

import java.util.HashMap;

public class HardCode {
	
	public static HashMap<String,String> hashMap = new HashMap<>();
	
	static {
		hashMap.put("aj1", "air jordan 1");
		hashMap.put("aj2", "air jordan 2");
	}
	
	public static String getAJ(String aj) {
		if(hashMap.get(aj)!=null) {
			return hashMap.get(aj);
		} else {
			return aj;
		}
	}
	
	public static void main(String[] args) {
	}
}
