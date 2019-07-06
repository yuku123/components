package com.zifang.demo.temp.tool.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @version 1.0
 */
public class DateUtils {

	/**
	 * MySQL日期格式
	 */
	private static final String dateMySQLPattern = "yyyy-MM-dd";

	/**
	 * MySQL日期时间格式
	 */
	private static final String datetimeMySQLPattern = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期对象依据格式转换为日期时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @param formatStr
	 *            格式字符串
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
	 * 日期时间字符串依据格式转换为日期对象
	 * 
	 * @param dateStr
	 *            日期时间字符串
	 * @param formatStr
	 *            格式字符串
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
	 * 获取当前日期的MySQL格式字符串，格式"yyyy-MM-dd"
	 * 
	 * @return String
	 */
	public static String getMySQLDate() {
		return getMySQLDate(new Date());
	}

	/**
	 * 获取指定日期的MySQL格式字符串，格式"yyyy-MM-dd"
	 * 
	 * @param date
	 *            日期对象
	 * @return String
	 */
	public static String getMySQLDate(Date date) {
		return DateToStringByFormat(date, dateMySQLPattern);
	}

	/**
	 * 获取当前日期时间的MySQL格式字符串，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return String
	 */
	public static String getMySQLDateTime() {
		return getMySQLDateTime(new Date());
	}

	/**
	 * 获取指定日期时间的MySQL格式字符串，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 *            日期对象
	 * @return String
	 */
	public static String getMySQLDateTime(Date date) {
		return DateToStringByFormat(date, datetimeMySQLPattern);
	}

	/**
	 * MySQL格式日期字符串"yyyy-MM-dd"转换为Date对象
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @return Date
	 */
	public static Date strMySQLDateToDate(String dateStr) {
		return StringToDateByFormat(dateStr, dateMySQLPattern);
	}

	/**
	 * MySQL格式日期时间字符串"yyyy-MM-dd HH:mm:ss"转换为Date对象
	 * 
	 * @param dateStr
	 *            日期时间字符串
	 * @return Date
	 */
	public static Date strMySQLDateTimeToDate(String dateStr) {
		return StringToDateByFormat(dateStr, datetimeMySQLPattern);
	}
}