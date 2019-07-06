package com.zifang.util.core.demo.temp.sun.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Java�ѷ������ߣ�jhat����������������������������dump����Ϣ���������С��������ǹ�����һ�� OutOfMemoryError ��Ȼ������
 * java ����ָ�� -XX:+HeapDumpOnOutOfMemoryError ����������ʱ�ͻ����һ�� dump �ļ������Ƿ�����
 * ���԰Ѷ��ڴ�����Сһ�㣬���ײ��� -Xmx32M
 * 
 * ����java_pid5644.hprof �ļ���Ȼ��ִ����������
 * 	jhat java_pid5644.hprof 
 * 
 * ͨ�������ַ���ʣ�
 * 	http://localhost:7000
 * 
 * ��Heap Histogram(��ֱ��ͼ)
 * �������ǿ��������ؿ���ӵ�� 393567 ���� HashMap ���ǵ��³��������Ԫ�ס���Ȼ�и�����Լ���ڴ�ֲ�ʹ������Ͷѷ����Ĺ��ߣ�����jhat�����õģ��Ƿ�����һ���õĿ��ˡ�
 * @author jijs
 *
 */
public class Jhat {

	private static Map map = new HashMap();

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				System.out.println("We have accumulated " + map.size() + "entries");
			}

		});
		for (int i = 0;; i++) {
			map.put(Integer.toBinaryString(i), i);
		}

	}
}
