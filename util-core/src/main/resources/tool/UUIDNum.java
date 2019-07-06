package com.zifang.util.core.demo.temp.tool;

import java.util.Date;

/**
 * Ψһ�������ɺܼ򵥣���������ʱ��Ϊ�����������ɡ���JDK�����Ѿ���Java.spi.UUID���������Ψһ������������ϣ�����ɵ�Ψһ��Ϊ�ض��ĸ�ʽ����ô����Ҫ�Լ�������Ψһ���ˡ�����Ψһ��ʱ�����������Ǳ����ڿ��ǵģ�
	���뱣֤Ψһ�����һ����ʱ��Ϊ�������б仯��
	��Ч����ȻԽ��ЧԽ�á�
	��ʱ����ϣ�������ɵ�Ψһ���а����ض������ݣ���ѵ�ǰʱ�䣬��20110609132641����Ϊǰ׺�ȡ�����Ƭ�ι��ο���
 *
 *
 */
public class UUIDNum {
	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();
	private static int seq = 0;
	private static final int ROTATION = 99999;

	public static synchronized long next() {
		if (seq > ROTATION)
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++);
		return Long.parseLong(str);
	}
}
