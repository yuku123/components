package com.zifang.util.core.demo.temp.tool.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ʱ�乤����
 * 
 * @version 1.0
 */
public class DateUtils {

	/**
	 * MySQL���ڸ�ʽ
	 */
	private static final String dateMySQLPattern = "yyyy-MM-dd";

	/**
	 * MySQL����ʱ���ʽ
	 */
	private static final String datetimeMySQLPattern = "yyyy-MM-dd HH:mm:ss";

	/**
	 * ���ڶ������ݸ�ʽת��Ϊ����ʱ���ַ���
	 * 
	 * @param date
	 *            ���ڶ���
	 * @param formatStr
	 *            ��ʽ�ַ���
	 * @return String
	 */
	public static String DateToStringByFormat(Date date, String formatStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			return sdf.format(date);
		} catch (Throwable t) {
			return null;
		}
	}

	/**
	 * ����ʱ���ַ������ݸ�ʽת��Ϊ���ڶ���
	 * 
	 * @param dateStr
	 *            ����ʱ���ַ���
	 * @param formatStr
	 *            ��ʽ�ַ���
	 * @return Date
	 */
	public static Date StringToDateByFormat(String dateStr, String formatStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			Date date = sdf.parse(dateStr);
			return date;
		} catch (Throwable t) {
			return null;
		}
	}

	/**
	 * ��ȡ��ǰ���ڵ�MySQL��ʽ�ַ�������ʽ"yyyy-MM-dd"
	 * 
	 * @return String
	 */
	public static String getMySQLDate() {
		return getMySQLDate(new Date());
	}

	/**
	 * ��ȡָ�����ڵ�MySQL��ʽ�ַ�������ʽ"yyyy-MM-dd"
	 * 
	 * @param date
	 *            ���ڶ���
	 * @return String
	 */
	public static String getMySQLDate(Date date) {
		return DateToStringByFormat(date, dateMySQLPattern);
	}

	/**
	 * ��ȡ��ǰ����ʱ���MySQL��ʽ�ַ�������ʽ"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return String
	 */
	public static String getMySQLDateTime() {
		return getMySQLDateTime(new Date());
	}

	/**
	 * ��ȡָ������ʱ���MySQL��ʽ�ַ�������ʽ"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            ���ڶ���
	 * @return String
	 */
	public static String getMySQLDateTime(Date date) {
		return DateToStringByFormat(date, datetimeMySQLPattern);
	}

	/**
	 * MySQL��ʽ�����ַ���"yyyy-MM-dd"ת��ΪDate����
	 * 
	 * @param dateStr
	 *            �����ַ���
	 * @return Date
	 */
	public static Date strMySQLDateToDate(String dateStr) {
		return StringToDateByFormat(dateStr, dateMySQLPattern);
	}

	/**
	 * MySQL��ʽ����ʱ���ַ���"yyyy-MM-dd HH:mm:ss"ת��ΪDate����
	 * 
	 * @param dateStr
	 *            ����ʱ���ַ���
	 * @return Date
	 */
	public static Date strMySQLDateTimeToDate(String dateStr) {
		return StringToDateByFormat(dateStr, datetimeMySQLPattern);
	}
}