//package com.zifang.util.core.beans.converter;
//
//import org.apache.log4j.log;
//
//import java.nio.ByteBuffer;
//import java.nio.CharBuffer;
//import java.nio.charset.Charset;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//
///**
// * 转换工具类
// *
// * 若待转换值为null或者出现异常，则使用默认值
// */
//public class ConvertMethodWrapper {
//
//	private static log log = log.getlog(ConvertMethodWrapper.class);
//
//	//----byte,short,int,long,float,double,char,string---------//
//
//	//------------------------------------------Byte---------------------------------------------
//
//	/**
//	 * byte -> byte
//	 * */
//	public Byte to(Byte value,Byte defaultValue){
//		log.info("call to(Byte value,Byte defaultValue)");
//		if(value == null){
//			return defaultValue;
//		}
//		return value;
//	}
//
//	/**
//	 * byte -> short
//	 * */
//	public Short to(Byte value,Short defaultValue){
//		log.info("call to(byte value,short defaultValue)");
//		if(value == null){
//			return defaultValue;
//		}
//		return Short.valueOf(value);
//	}
//
//	/**
//	 * byte -> int
//	 * */
//	public Integer to(Byte value, Integer defaultValue){
//		log.info("call to(Byte value,Integer defaultValue)");
//		if(value == null){
//			return defaultValue;
//		}
//		return Integer.valueOf(value);
//	}
//
//	/**
//	 * byte -> long
//	 * */
//	public Long to(Byte value, Long defaultValue){
//		log.info("call to(Byte value,Long defaultValue)");
//		if(value == null){
//			return defaultValue;
//		}
//		return Long.valueOf(value);
//	}
//
//	/**
//	 * byte -> float
//	 * */
//	public Float to(Byte value,Float defaultValue){
//		log.info("call to(Byte value,Float defaultValue)");
//		if(value == null){
//			return defaultValue;
//		}
//		return Float.valueOf(value);
//	}
//
//	/**
//	 * byte -> double
//	 * */
//	public Double to(Byte value,Double defaultValue){
//		log.info("call to(Byte value,Double defaultValue)");
//		if(value == null){
//			return defaultValue;
//		}
//		return Double.valueOf(value);
//	}
//
//	/**
//	 * char[] -> byte[]
//	 * */
//	public byte[] to(char[] chars) {
//		Charset cs = Charset.forName("UTF-8");
//		CharBuffer cb = CharBuffer.allocate(chars.length);
//		cb.put(chars);
//		cb.flip();
//		ByteBuffer bb = cs.encode(cb);
//		return bb.array();
//	}
//
//	public static char[] to(byte[] bytes) {
//		Charset cs = Charset.forName("UTF-8");
//		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
//		bb.put(bytes);
//		bb.flip();
//		CharBuffer cb = cs.decode(bb);
//		return cb.array();
//	}
//
//	public static byte[] to(char c) {
//		byte[] b = new byte[2];
//		b[0] = (byte) ((c & 0xFF00) >> 8);
//		b[1] = (byte) (c & 0xFF);
//		return b;
//	}
//
//	public char to(byte[] b, char defaultValue) {
//		char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
//		return c;
//	}
//
//	public String to(byte value,String defaultValue){
//		return String.valueOf(value);
//	}
//	//------------------------------------------Byte[]---------------------------------------------
//	//------------------------------------------Short---------------------------------------------
//	//------------------------------------------Short[]---------------------------------------------
//	//------------------------------------------int---------------------------------------------
//	//------------------------------------------long---------------------------------------------
//	//------------------------------------------float---------------------------------------------
//	//------------------------------------------double---------------------------------------------
//	//------------------------------------------char---------------------------------------------
//	//------------------------------------------String---------------------------------------------
//
//	//------------------------------------------String---------------------------------------------
//	//------------------------------------------String---------------------------------------------
//	//------------------------------------------String---------------------------------------------
//	//------------------------------------------String---------------------------------------------
//	//------------------------------------------String---------------------------------------------
//	/**
//	 * 字符串转换为 Integer
//	 */
//	public static Integer to(String str, Integer defaultValue) {
//		try {
//			return Integer.valueOf(str);
//		} catch (NumberFormatException localException) {
//			log.error("to(String str, Integer defaultValue) fail" );
//			localException.printStackTrace();
//			return defaultValue;
//		}
//	}
//
//	/**
//	 * 字符串转换为 Long
//	 */
//	public static Long to(String str, Long defaultValue) {
//		try {
//			return Long.valueOf(str);
//		} catch (NumberFormatException localException) {
//			log.error("to(String str, Long defaultValue) fail" );
//			localException.printStackTrace();
//			return defaultValue;
//		}
//	}
//
//	/**
//	 * 字符串转换为float
//	 */
//	public static Float to(String str, Float defaultValue) {
//		try {
//			return Float.parseFloat(str);
//		} catch (NumberFormatException localException) {
//			log.error("to(String str, float defaultValue) fail" );
//			localException.printStackTrace();
//			return defaultValue;
//		}
//	}
//
//	/**
//	 * String转换为Double
//	 */
//	public static Double to(String str, Double defaultValue) {
//		try {
//			return Double.parseDouble(str);
//		} catch (NumberFormatException localException) {
//			log.error("to(String str, Double defaultValue) fail" );
//			localException.printStackTrace();
//			return defaultValue;
//		}
//	}
//
//	/**
//	 * 字符串转换日期
//	 *
//	 * @param str
//	 * 						待转换的字符串
//	 * @param defaultValue
//	 * 						默认日期
//	 * @return
//	 */
//	public static java.util.Date to (String str,java.util.Date defaultValue) {
//		return to(str, "yyyy-MM-dd HH:mm:ss", defaultValue);
//	}
//
//	/**
//	 * 字符串转换为指定格式的日期
//	 */
//	public static java.util.Date to(String str, String format,java.util.Date defaultValue) {
//		SimpleDateFormat fmt = new SimpleDateFormat(format);
//		try {
//			defaultValue = fmt.parse(str);
//		} catch (Exception localException) {
//		}
//		return defaultValue;
//	}
//
//	/**
//	 * 日期转换为字符串
//	 */
//	public static String to(java.util.Date date, String defaultValue) {
//		return dateToStr(date, "yyyy-MM-dd HH:mm:ss", defaultValue);
//	}
//
//	/**
//	 * 日期转换为指定格式的字符串
//	 */
//	public static String dateToStr(java.util.Date date, String format, String defaultValue) {
//		SimpleDateFormat sdf = new SimpleDateFormat(format);
//		try {
//			defaultValue = sdf.format(date);
//		} catch (Exception localException) {
//		}
//		return defaultValue;
//	}
//
//	/**
//	 * 如果字符串为空则使用默认字符串
//	 */
//	public static String to(String str, String defaultValue) {
//		if ((str != null) && (!(str.isEmpty())))
//			defaultValue = str;
//		return defaultValue;
//	}
//
//	/**
//	 * util date 转换为 sqldate
//	 */
//	public static java.sql.Date to(java.util.Date date,java.sql.Date de) {
//		return new java.sql.Date(date.getTime());
//	}
//
//	/**
//	 * sql date 转换为 util date
//	 */
//	public static java.util.Date to(java.sql.Date date) {
//		return new java.util.Date(date.getTime());
//	}
//
//	/**
//	 * date 转换为 timestamp
//	 */
//	public static Timestamp to(java.util.Date date,Timestamp defaultValue) {
//		return new Timestamp(date.getTime());
//	}
//
//	/**
//	 * timestamp 转换为date
//	 */
//	public static java.util.Date to(Timestamp date,java.util.Date defaultValue) {
//		return new java.util.Date(date.getTime());
//	}
//}
