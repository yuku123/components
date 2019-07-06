package com.zifang.util.core.demo.temp.sun.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * jmap ��һ���ڴ�ӳ�乤�ߣ����ṩ������һ�ֲ���Ҫ���� OutOfMemoryErrors �Ϳ��Ի�ȡ�� dump
 * �ļ��ķ�����������΢�޸�һ������ĳ���һ��Ч����
 * 
 * @author jijs
 *
 */
public class Jmap {

	private static Map map = new HashMap<>();

	/**
	 * ע�⣬�������ǲ�Ҫ���Ĵ������ڴ棬ֻ�ǱȽ���������ڽ��̹رչ�����ȴ����� JVM �˳������������������� jmap ����������̻�ȡ�����ڴ�
	 * dump�� ���������� jmap ������������ʵ�֣���ȡ��ͳ����Ϣ�ʹ���һ���� dump��
	 * 
	 * jmap -heap 1354������ 1354 �ǳ������еĽ��̺ţ����Ϳ��Ի�ȡһ���ܺõ��ڴ�ʹ��ͳ����Ϣ
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("Enter something, so I'll release the process");
					System.in.read();
					System.out.println("We have accumulated " + map.size() + " entries");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});

		for (int i = 0; i < 10000; i++) {
			map.put(Integer.toBinaryString(i), i);
		}
	}
}
