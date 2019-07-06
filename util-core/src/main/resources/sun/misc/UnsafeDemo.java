package com.zifang.util.core.demo.temp.sun.misc;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class UnsafeDemo {
	private static final Unsafe unsafe = getUnsafe();
	public int a = 5;
	private static final long stateOffset;
	static {
		try {
			stateOffset = unsafe.objectFieldOffset(UnsafeDemo.class.getDeclaredField("a"));
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public static void main(String[] args) {
		UnsafeDemo t = new UnsafeDemo();
		/**
		 * compareAndSet���������Ҫ����unsafe.compareAndSwapInt���������
		 * ����������ĸ�����
		 * 	��һ������Ϊ��Ҫ�ı�Ķ���
		 * 	�ڶ���Ϊƫ����(��֮ǰ�������valueOffset��ֵ)��
		 * 	����������Ϊ�ڴ���ֵ��
		 * 	���ĸ�Ϊ���º��ֵ��
		 * �������������ü�Ϊ�����ø÷���ʱ��value��ֵ��expect���ֵ��ȣ���ô��value�޸�Ϊupdate���ֵ��
		 * ������һ��true��������ø÷���ʱ��value��ֵ��expect���ֵ����ȣ���ô�����κβ���������Χһ��false��
		 * 
		 * ���֮������getAndSet�����е���һ��forѭ��������֤�������compareAndSet�����������Ϊfalseʱ��
		 * ���ٴγ��Խ����޸�value��ֵ��ֱ���޸ĳɹ����������޸�ǰvalue��ֵ��
		 * 
		 * ���������ܱ�֤�ڶ��߳�ʱ�����̰߳�ȫ�ԣ�����û��ʹ��java���κ����Ļ��ƣ��������ı���Unsafe������е��õĸ÷�������ԭ���ԣ�
		 * ���ԭ���Եı�֤�����ǿ�java����֤�����ǿ�һ�����ײ�������ϵͳ��ص�����ʵ�֡�
		 */
		boolean f = unsafe.compareAndSwapInt(t, stateOffset, 2, 3);
		System.out.println(f);
		System.out.println(t.a);
	}

	/**
	 * ���ȿ��Կ���AtomicInteger��������������������˽�б���unsafe��valueOffset��
	 * ����unsafeʵ������Unsafe���о�̬����getUnsafe()�õ���������������������д��ʱ����ûᱨ��
	 * ��Ϊ��������ڵ���ʱ���ж�������������ǵĴ�����û�С������Ρ��ģ�����jdkԴ���е�����û���κ�����ģ�
	 * valueOffset�����ָ������Ӧ�ֶ��ڸ����ƫ��������������弴��ָvalue����ֶ���AtomicInteger����ڴ�������ڸ����׵�ַ��ƫ������
	 * Ȼ����Կ�һ����һ����̬��ʼ���飬���������ü������value����ֶε�ƫ����������ķ���ʹ�õķ���Ļ��Ƶõ�value��Field����
	 * �ٸ���objectFieldOffset����������value��������ڴ����ڸö����е�ƫ������
	 * volatile�ؼ��ֱ�֤���ڶ��߳���value��ֵ�ǿɼ��ģ��κ�һ���߳��޸���valueֵ���Ὣ������д���ڴ浱��
	 */
	//ͨ�������ȡUnsafe���󣬲���ֱ�ӻ�
	private static Unsafe getUnsafe(){
		try{
			Class<?> unsafeClass = Unsafe.class;
			for (Field f : unsafeClass.getDeclaredFields()) {
				if ("theUnsafe".equals(f.getName())) {
					f.setAccessible(true);
					return (Unsafe) f.get(null);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
