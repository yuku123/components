package com.zifang.util.monitor.thread.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author lijing
 */
public final class DateUtils {

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static final String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";

    /**
     * 私有构造函数，不允许实例化此类。
     */
    private DateUtils() {
    }

    /**
     * 默认日期格式。
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 一天总毫秒数
     */
    public static final long dayTotalMilliseconds = 24 * 60 * 60 * 1000L;

    /**
     * 一天总秒数
     */
    public static final int dayTotalSeconds = 24 * 60 * 60;

    /**
     * 返回日期的字符串类型，比如2014-06-04。
     *
     * @param dateTimestamp 系统毫秒数。
     * @return 指定毫秒时间戳对应的日期。
     */
    public static String getDateString(long dateTimestamp) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return df.format(dateTimestamp);
    }

    /**
     * 返回今天的日期，格式是2014-06-04。
     *
     * @return 今天的日期。
     */
    public static String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return df.format(TimeUtil.getMillisTimestamp());
    }


    /**
     * 返回今天的日期，格式是2014-06-04 13:23:22。
     * @return 今天的日期。
     */
    public static String getDateAndTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        return df.format(TimeUtil.getMillisTimestamp());
    }

    /**
     * 返回下一天日期的字符串类型，比如输入为2014-06-04的时间戳，返回2014-06-05
     *
     * @param dateTimestamp 系统毫秒数
     * @return
     */
    public static String getNextDateString(long dateTimestamp) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return df.format(dateTimestamp + dayTotalMilliseconds);
    }

    /**
     * 根据时间戳，返回指定格式的日期时间字符串
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String getDateTimeString(long timestamp, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(timestamp);
    }

    /**
     * 日期转成字符串
     *
     * @param date
     * @param temp
     * @return
     */
    public static String dateToStr(Date date, String temp) {
        SimpleDateFormat format = new SimpleDateFormat(temp);
        String dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 字符串转成日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date strToDate(String dateStr, String formatStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转成日期后的毫秒时间戳
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static long strToDateMillisTimestamp(String dateStr, String formatStr) {
        Date date = strToDate(dateStr, formatStr);
        return date != null ? date.getTime() : 0;
    }

    /**
     * 字符串转成日期后的标准时间戳
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static int strToDateTimestamp(String dateStr, String formatStr) {
        Date date = strToDate(dateStr, formatStr);
        return date != null ? (int) (date.getTime() / 1000L) : 0;
    }

    /**
     * 计算两个日期date1-date2相差的天数
     * @param date1
     * @param date2
     * @return
     * @throws Exception 
     */
    public static long calcDay(String date1,String date2,String format) throws Exception{
	    SimpleDateFormat sdf=new SimpleDateFormat(format);  
	    Calendar cal = Calendar.getInstance();    
	    cal.setTime(sdf.parse(date1));    
	    long time1 = cal.getTimeInMillis();                 
	    cal.setTime(sdf.parse(date2));    
	    long time2 = cal.getTimeInMillis();         
	    return (time1-time2)/(1000*3600*24);
    }

    /**
     * 比较两个Date类型的日期大小
     * @param sDate开始时间
     * @param eDate结束时间
     * @return result返回结果(0--相同  1--前者大  2--后者大)
     * */
    public static int compareDate(Date sDate, Date eDate){
        Calendar sC = Calendar.getInstance();
        sC.setTime(sDate);
        Calendar eC = Calendar.getInstance();
        eC.setTime(eDate);
        return sC.compareTo(eC);
    }

    /**
     * 比较两个String类型的日期大小
     * @param sDate
     * @param eDate
     * @param formatStr
     * @return  result返回结果(0--相同  1--前者大  -1--后者大)
     */
    public static int compareDate(String sDate, String eDate, String formatStr) {
        Date startDate = strToDate(sDate,formatStr);
        Date endDate = strToDate(eDate, formatStr);
        return compareDate(startDate, endDate);
    }
}
