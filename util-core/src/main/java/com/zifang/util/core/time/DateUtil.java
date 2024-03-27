package com.zifang.util.core.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间处理工具类
 */
public class DateUtil {

    private static final String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 根据指定格式获取当前时间
     *
     * @param format
     * @return String
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * 获取当前时间，格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        return getCurrentTime(DateFormatUtil.DATE_FORMAT2);
    }

    /**
     * 获取指定格式的当前时间：为空时格式为yyyy-mm-dd HH:mm:ss
     */
    public static Date getCurrentDate(String format) {
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
        String dateS = getCurrentTime(format);
        Date date = null;
        try {
            date = sdf.parse(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
     */
    public static Date getCurrentDate() {
        return getCurrentDate(DateFormatUtil.DATE_FORMAT2);
    }

    /**
     * 给指定日期加入年份，为空时默认当前时间
     *
     * @param year   年份  正数相加、负数相减
     * @param date   为空时，默认为当前时间
     * @param format 默认格式为：yyyy-MM-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 30, 2013
     */
    public static String addYearToDate(int year, Date date, String format) {
        Calendar calender = getCalendar(date, format);
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

        calender.add(Calendar.YEAR, year);

        return sdf.format(calender.getTime());
    }

    /**
     * 给指定日期加入年份，为空时默认当前时间
     *
     * @param year   年份  正数相加、负数相减
     * @param date   为空时，默认为当前时间
     * @param format 默认格式为：yyyy-MM-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 30, 2013
     */
    public static String addYearToDate(int year, String date, String format) {
        Date newDate = new Date();
        if (null != date && !"".equals(date)) {
            newDate = string2Date(date, format);
        }

        return addYearToDate(year, newDate, format);
    }

    /**
     * 给指定日期增加月份 为空时默认当前时间
     *
     * @param month  增加月份  正数相加、负数相减
     * @param date   指定时间
     * @param format 指定格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 30, 2013
     */
    public static String addMothToDate(int month, Date date, String format) {
        Calendar calender = getCalendar(date, format);
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

        calender.add(Calendar.MONTH, month);

        return sdf.format(calender.getTime());
    }

    /**
     * 给指定日期增加月份 为空时默认当前时间
     *
     * @param month  增加月份  正数相加、负数相减
     * @param date   指定时间
     * @param format 指定格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 30, 2013
     */
    public static String addMothToDate(int month, String date, String format) {
        Date newDate = new Date();
        if (null != date && !"".equals(date)) {
            newDate = string2Date(date, format);
        }

        return addMothToDate(month, newDate, format);
    }

    /**
     * 给指定日期增加天数，为空时默认当前时间
     *
     * @param day    增加天数 正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addDayToDate(int day, Date date, String format) {
        Calendar calendar = getCalendar(date, format);
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

        calendar.add(Calendar.DATE, day);

        return sdf.format(calendar.getTime());
    }

    /**
     * 给指定日期增加天数，为空时默认当前时间
     *
     * @param day    增加天数 正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addDayToDate(int day, String date, String format) {
        Date newDate = new Date();
        if (null != date && !"".equals(date)) {
            newDate = string2Date(date, format);
        }

        return addDayToDate(day, newDate, format);
    }

    /**
     * 给指定日期增加小时，为空时默认当前时间
     *
     * @param hour   增加小时  正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addHourToDate(int hour, Date date, String format) {
        Calendar calendar = getCalendar(date, format);
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

        calendar.add(Calendar.HOUR, hour);

        return sdf.format(calendar.getTime());
    }

    /**
     * 给指定日期增加小时，为空时默认当前时间
     *
     * @param hour   增加小时  正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addHourToDate(int hour, String date, String format) {
        Date newDate = new Date();
        if (null != date && !"".equals(date)) {
            newDate = string2Date(date, format);
        }

        return addHourToDate(hour, newDate, format);
    }

    /**
     * 给指定的日期增加分钟，为空时默认当前时间
     *
     * @param minute 增加分钟  正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addMinuteToDate(int minute, Date date, String format) {
        Calendar calendar = getCalendar(date, format);
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

        calendar.add(Calendar.MINUTE, minute);

        return sdf.format(calendar.getTime());
    }

    /**
     * 给指定的日期增加分钟，为空时默认当前时间
     *
     * @param minute 增加分钟  正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addMinuteToDate(int minute, String date, String format) {
        Date newDate = new Date();
        if (null != date && !"".equals(date)) {
            newDate = string2Date(date, format);
        }

        return addMinuteToDate(minute, newDate, format);
    }

    /**
     * 给指定日期增加秒，为空时默认当前时间
     *
     * @param second 增加秒 正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addSecondToDate(int second, Date date, String format) {
        Calendar calendar = getCalendar(date, format);
        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

        calendar.add(Calendar.SECOND, second);

        return sdf.format(calendar.getTime());
    }

    /**
     * 给指定日期增加秒，为空时默认当前时间
     *
     * @param second 增加秒 正数相加、负数相减
     * @param date   指定日期
     * @param format 日期格式 为空默认 yyyy-mm-dd HH:mm:ss
     * @return String
     * @throws Exception
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String addSecondToDate(int second, String date, String format) {
        Date newDate = new Date();
        if (null != date && !"".equals(date)) {
            newDate = string2Date(date, format);
        }

        return addSecondToDate(second, newDate, format);
    }

    /**
     * 获取指定格式指定时间的日历
     *
     * @param date   时间
     * @param format 格式
     * @return Calendar
     * @author chenssy
     * @date Dec 30, 2013
     */
    public static Calendar getCalendar(Date date, String format) {
        if (date == null) {
            date = getCurrentDate(format);
        }

        Calendar calender = Calendar.getInstance();
        calender.setTime(date);

        return calender;
    }

    /**
     * 字符串转换为日期，日期格式为
     *
     * @param value
     * @return
     * @author : chenssy
     * @date : 2019年5月31日 下午5:20:22
     */
    public static Date string2Date(String value) {
        if (value == null || "".equals(value)) {
            return null;
        }

        SimpleDateFormat sdf = DateFormatUtil.getFormat(DateFormatUtil.DATE_FORMAT2);
        Date date = null;

        try {
            value = DateFormatUtil.formatDate(value, DateFormatUtil.DATE_FORMAT2);
            date = sdf.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串(格式符合规范)转换成Date
     *
     * @param value  需要转换的字符串
     * @param format 日期格式
     * @return Date
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static Date string2Date(String value, String format) {
        if (value == null || "".equals(value)) {
            return null;
        }

        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
        Date date = null;

        try {
            value = DateFormatUtil.formatDate(value, format);
            date = sdf.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将日期格式转换成String
     *
     * @param value  需要转换的日期
     * @param format 日期格式
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String date2String(Date value, String format) {
        if (value == null) {
            return null;
        }

        SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
        return sdf.format(value);
    }

    /**
     * 日期转换为字符串
     *
     * @param value
     * @return
     * @author : chenssy
     * @date : 2019年5月31日 下午5:21:38
     */
    public static String date2String(Date value) {
        if (value == null) {
            return null;
        }

        SimpleDateFormat sdf = DateFormatUtil.getFormat(DateFormatUtil.DATE_FORMAT2);
        return sdf.format(value);
    }

    /**
     * 获取指定日期的年份
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentYear(Date value) {
        String date = date2String(value, DateFormatUtil.DATE_YEAR);
        return Integer.valueOf(date);
    }

    /**
     * 获取指定日期的年份
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentYear(String value) {
        Date date = string2Date(value, DateFormatUtil.DATE_YEAR);
        Calendar calendar = getCalendar(date, DateFormatUtil.DATE_YEAR);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取指定日期的月份
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentMonth(Date value) {
        String date = date2String(value, DateFormatUtil.DATE_MONTH);
        return Integer.valueOf(date);
    }

    /**
     * 获取指定日期的月份
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentMonth(String value) {
        Date date = string2Date(value, DateFormatUtil.DATE_MONTH);
        Calendar calendar = getCalendar(date, DateFormatUtil.DATE_MONTH);

        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取指定日期的天份
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentDay(Date value) {
        String date = date2String(value, DateFormatUtil.DATE_DAY);
        return Integer.valueOf(date);
    }

    /**
     * 获取指定日期的天份
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentDay(String value) {
        Date date = string2Date(value, DateFormatUtil.DATE_DAY);
        Calendar calendar = getCalendar(date, DateFormatUtil.DATE_DAY);

        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当前日期为星期几
     *
     * @param value 日期
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String getCurrentWeek(Date value) {
        Calendar calendar = getCalendar(value, DateFormatUtil.DATE_FORMAT1);
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 0 : calendar.get(Calendar.DAY_OF_WEEK) - 1;

        return weeks[weekIndex];
    }

    /**
     * 获取当前日期为星期几
     *
     * @param value 日期
     * @return String
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static String getCurrentWeek(String value) {
        Date date = string2Date(value, DateFormatUtil.DATE_FORMAT1);
        return getCurrentWeek(date);
    }

    /**
     * 获取指定日期的小时
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentHour(Date value) {
        String date = date2String(value, DateFormatUtil.DATE_HOUR);
        return Integer.valueOf(date);
    }

    /**
     * 获取指定日期的小时
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentHour(String value) {
        Date date = string2Date(value, DateFormatUtil.DATE_HOUR);
        Calendar calendar = getCalendar(date, DateFormatUtil.DATE_HOUR);

        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取指定日期的分钟
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentMinute(Date value) {
        String date = date2String(value, DateFormatUtil.DATE_MINUTE);
        return Integer.valueOf(date);
    }

    /**
     * 获取指定日期的分钟
     *
     * @param value 日期
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int getCurrentMinute(String value) {
        Date date = string2Date(value, DateFormatUtil.DATE_MINUTE);
        Calendar calendar = getCalendar(date, DateFormatUtil.DATE_MINUTE);

        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 比较两个日期相隔多少天(月、年) <br>
     * 例：<br>
     * &nbsp;compareDate("2009-09-12", null, 0);//比较天 <br>
     * &nbsp;compareDate("2009-09-12", null, 1);//比较月 <br>
     * &nbsp;compareDate("2009-09-12", null, 2);//比较年 <br>
     *
     * @param startDay 需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
     * @param endDay   被比较的时间  为空(null)则为当前时间
     * @param stype    返回值类型   0为多少天，1为多少个月，2为多少年
     * @return int
     * @author chenssy
     * @date Dec 31, 2013
     */
    public static int compareDate(String startDay, String endDay, int stype) {
        int n = 0;
        startDay = DateFormatUtil.formatDate(startDay, "yyyy-MM-dd");
        endDay = DateFormatUtil.formatDate(endDay, "yyyy-MM-dd");

        String formatStyle = "yyyy-MM-dd";
        if (1 == stype) {
            formatStyle = "yyyy-MM";
        } else if (2 == stype) {
            formatStyle = "yyyy";
        }

        endDay = endDay == null ? getCurrentTime("yyyy-MM-dd") : endDay;

        DateFormat df = new SimpleDateFormat(formatStyle);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(startDay));
            c2.setTime(df.parse(endDay));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!c1.after(c2)) {                   // 循环对比，直到相等，n 就是所要的结果     
            n++;
            if (stype == 1) {
                c1.add(Calendar.MONTH, 1);          // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1);           // 比较天数，日期+1
            }
        }
        n = n - 1;
        if (stype == 2) {
            n = n / 365;
        }
        return n;
    }

    /**
     * 比较两个时间相差多少小时(分钟、秒)
     *
     * @param startTime 需要比较的时间 不能为空，且必须符合正确格式：2012-12-12 12:12:
     * @param endTime   需要被比较的时间 若为空则默认当前时间
     * @param type      1：小时   2：分钟   3：秒
     * @return int
     * @author chenssy
     * @date Jan 2, 2014
     */
    public static int compareTime(String startTime, String endTime, int type) {
        //endTime是否为空，为空默认当前时间
        if (endTime == null || "".equals(endTime)) {
            endTime = getCurrentTime();
        }

        SimpleDateFormat sdf = DateFormatUtil.getFormat("");
        int value = 0;
        try {
            Date begin = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long between = (end.getTime() - begin.getTime()) / 1000;  //除以1000转换成豪秒
            if (type == 1) {   //小时
                value = (int) (between % (24 * 36000) / 3600);
            } else if (type == 2) {
                value = (int) (between % 3600 / 60);
            } else if (type == 3) {
                value = (int) (between % 60 / 60);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 比较两个日期的大小。<br>
     * 若date1 > date2 则返回 1<br>
     * 若date1 = date2 则返回 0<br>
     * 若date1 < date2 则返回-1
     *
     * @param date1
     * @param date2
     * @param format 待转换的格式
     * @return 比较结果
     * @autor:chenssy
     * @date:2014年9月9日
     */
    public static int compare(String date1, String date2, String format) {
        DateFormat df = DateFormatUtil.getFormat(format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取指定月份的第一天
     *
     * @param date
     * @return
     * @author : chenssy
     * @date : 2019年5月31日 下午5:31:10
     */
    public static String getMonthFirstDate(String date) {
        date = DateFormatUtil.formatDate(date);
        return DateFormatUtil.formatDate(date, "yyyy-MM") + "-01";
    }

    /**
     * 获取指定月份的最后一天
     *
     * @param strdate
     * @return
     * @author : chenssy
     * @date : 2019年5月31日 下午5:32:09
     */
    public static String getMonthLastDate(String date) {
        Date strDate = DateUtil.string2Date(getMonthFirstDate(date));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return DateFormatUtil.formatDate(calendar.getTime());
    }

    /**
     * 获取所在星期的第一天
     *
     * @param date
     * @return
     * @author : chenssy
     * @date : 2019年6月1日 下午12:38:53
     */
    @SuppressWarnings("static-access")
    public static Date getWeekFirstDate(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        int today = now.get(Calendar.DAY_OF_WEEK);
        int first_day_of_week = now.get(Calendar.DATE) + 2 - today; // 星期一
        now.set(now.DATE, first_day_of_week);
        return now.getTime();
    }

    /**
     * 获取所在星期的最后一天
     *
     * @param date
     * @return
     */
    public static Date geWeektLastDate(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        int today = now.get(Calendar.DAY_OF_WEEK);
        int first_day_of_week = now.get(Calendar.DATE) + 2 - today; // 星期一
        int last_day_of_week = first_day_of_week + 6; // 星期日
        now.set(Calendar.DATE, last_day_of_week);
        return now.getTime();
    }


    //注意SimpleDateFormat不是线程安全的
    private static ThreadLocal<SimpleDateFormat> ThreadDateTime = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> ThreadDate = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> ThreadTime = new ThreadLocal<SimpleDateFormat>();

    private static SimpleDateFormat DateTimeInstance() {
        SimpleDateFormat df = ThreadDateTime.get();
        if (df == null) {
            df = new SimpleDateFormat("Opslab.DATETIME_FORMAT");
            ThreadDateTime.set(df);
        }
        return df;
    }

    private static SimpleDateFormat DateInstance() {
        SimpleDateFormat df = ThreadDate.get();
        if (df == null) {
            df = new SimpleDateFormat("Opslab.DATE_FORMAT");
            ThreadDate.set(df);
        }
        return df;
    }

    private static SimpleDateFormat TimeInstance() {
        SimpleDateFormat df = ThreadTime.get();
        if (df == null) {
            df = new SimpleDateFormat("Opslab.TIME_FORMAT");
            ThreadTime.set(df);
        }
        return df;
    }


    /**
     * 获取当前日期时间
     *
     * @return 返回当前时间的字符串值
     */
    public static String currentDateTime() {
        return DateTimeInstance().format(new Date());
    }

    /**
     * 将指定的时间格式化成出返回
     *
     * @param date
     * @return
     */
    public static String dateTime(Date date) {
        return DateTimeInstance().format(date);
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param datestr
     * @return
     * @throws ParseException
     */
    public static Date dateTime(String datestr) throws ParseException {
        return DateTimeInstance().parse(datestr);
    }

    /**
     * 获取当前的日期
     *
     * @return
     */
    public static String currentDate() {
        return DateInstance().format(new Date());
    }

    /**
     * 将指定的时间格式化成出返回
     *
     * @param date
     * @return
     */
    public static String date(Date date) {
        return DateInstance().format(date);
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date date(String dateStr) throws ParseException {
        return DateInstance().parse(dateStr);
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String currentTime() {
        return TimeInstance().format(new Date());
    }

    /**
     * 讲指定的时间格式化成出返回
     *
     * @param date
     * @return
     */
    public static String time(Date date) {
        return TimeInstance().format(date);
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date time(String dateStr) throws ParseException {
        return TimeInstance().parse(dateStr);
    }


    /**
     * 在当前时间的基础上加或减去year年
     */
    public static Date year(int year) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.YEAR, year);
        return Cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几年
     *
     * @param date
     * @param year
     * @return
     */
    public static Date year(Date date, int year) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.YEAR, year);
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几月
     *
     * @param month
     * @return
     */
    public static Date month(int month) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.MONTH, month);
        return Cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几月
     *
     * @param date
     * @param month
     * @return
     */
    public static Date month(Date date, int month) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.MONTH, month);
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几天
     *
     * @param day
     * @return
     */
    public static Date day(int day) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.DAY_OF_YEAR, day);
        return Cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几天
     *
     * @param date
     * @param day
     * @return
     */
    public static Date day(Date date, int day) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.DAY_OF_YEAR, day);
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几小时-支持浮点数
     *
     * @param hour
     * @return
     */
    public static Date hour(float hour) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.MINUTE, (int) (hour * 60));
        return Cal.getTime();
    }

    /**
     * 在制定的时间上加或减去几小时-支持浮点数
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date hour(Date date, float hour) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.MINUTE, (int) (hour * 60));
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几分钟
     *
     * @param minute
     * @return
     */
    public static Date minute(int minute) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.MINUTE, minute);
        return Cal.getTime();
    }

    /**
     * 在制定的时间上加或减去几分钟
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date minute(Date date, int minute) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.MINUTE, minute);
        return Cal.getTime();
    }


    /**
     * 判断字符串是否为日期字符串
     *
     * @param date 日期字符串
     * @return true or false
     */
    public static boolean isDate(String date) {
        try {
            DateTimeInstance().parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 时间date1和date2的时间差-单位秒
     *
     * @param date1
     * @param date2
     * @return 秒
     */
    public static long subtract(Date date1, Date date2) {
        long cha = (date2.getTime() - date1.getTime()) / 1000;
        return cha;
    }

    /**
     * 时间date1和date2的时间差-单位秒
     *
     * @param date1
     * @param date2
     * @return 秒
     */
    public static long subtract(String date1, String date2) {
        long rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = cha;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }


    /**
     * 时间date1和date2的时间差 -单位分钟
     *
     * @param date1
     * @param date2
     * @return 分钟
     */
    public static int subtractMinute(String date1, String date2) {
        int rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = (int) cha / (60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 时间date1和date2的时间差-单位分钟
     *
     * @param date1
     * @param date2
     * @return 分钟
     */
    public static int subtractMinute(Date date1, Date date2) {
        long cha = date2.getTime() - date1.getTime();
        return (int) cha / (1000 * 60);
    }

    /**
     * 时间date1和date2的时间差-单位小时
     *
     * @param date1
     * @param date2
     * @return 小时
     */
    public static int subtractHour(Date date1, Date date2) {
        long cha = (date2.getTime() - date1.getTime()) / 1000;
        return (int) cha / (60 * 60);
    }

    /**
     * 时间date1和date2的时间差-单位小时
     *
     * @param date1
     * @param date2
     * @return 小时
     */
    public static int subtractHour(String date1, String date2) {
        int rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = (int) cha / (60 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }


    /**
     * 时间date1和date2的时间差-单位天
     *
     * @param date1
     * @param date2
     * @return 天
     */
    public static int subtractDay(String date1, String date2) {
        int rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            rs = (int) sss / (60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 时间date1和date2的时间差-单位天
     *
     * @param date1
     * @param date2
     * @return 天
     */
    public static int subtractDay(Date date1, Date date2) {
        long cha = date2.getTime() - date1.getTime();
        return (int) cha / (1000 * 60 * 60 * 24);
    }

    /**
     * 时间date1和date2的时间差-单位月
     *
     * @param date1
     * @param date2
     * @return 月
     */
    public static int subtractMonth(String date1, String date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(DateInstance().parse(date1));
            c2.setTime(DateInstance().parse(date2));
            int year1 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int year2 = c2.get(Calendar.YEAR);
            int month2 = c2.get(Calendar.MONTH);
            if (year1 == year2) {
                result = month2 - month1;
            } else {
                result = 12 * (year2 - year1) + month2 - month1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位月
     *
     * @param date1
     * @param date2
     * @return 月
     */
    public static int subtractMonth(Date date1, Date date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH);
        if (year1 == year2) {
            result = month2 - month1;
        } else {
            result = 12 * (year2 - year1) + month2 - month1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位年
     *
     * @param date1
     * @param date2
     * @return 年
     */
    public static int subtractYear(String date1, String date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(DateInstance().parse(date1));
            c2.setTime(DateInstance().parse(date2));
            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            result = year2 - year1;
        } catch (ParseException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位年
     *
     * @param date1
     * @param date2
     * @return 年
     */
    public static int subtractYear(Date date1, Date date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        result = year2 - year1;
        return result;
    }

    /**
     * 获取俩个时间的查结果用时秒表示
     *
     * @param date1
     * @param date2
     * @return 几小时:几分钟:几秒钟
     * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
     */
    public static String subtractTime(String date1, String date2) {
        String result = "";
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            int hh = (int) sss / (60 * 60);
            int mm = (int) (sss - hh * 60 * 60) / (60);
            int ss = (int) (sss - hh * 60 * 60 - mm * 60);
            result = hh + ":" + mm + ":" + ss;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取俩个时间的查结果用时秒表示
     *
     * @param date1
     * @param date2
     * @return 几天-几小时:几分钟:几秒钟
     * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
     */
    public static String subtractDate(String date1, String date2) {
        String result = "";
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            int dd = (int) sss / (60 * 60 * 24);
            int hh = (int) (sss - dd * 60 * 60 * 24) / (60 * 60);
            int mm = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60) / (60);
            int ss = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60 - mm * 60);
            result = dd + "-" + hh + ":" + mm + ":" + ss;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取俩个时间之前的相隔的天数
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static int subDay(Date startTime, Date endTime) {
        int days = 0;
        Calendar can1 = Calendar.getInstance();
        can1.setTime(startTime);
        Calendar can2 = Calendar.getInstance();
        can2.setTime(endTime);
        int year1 = can1.get(Calendar.YEAR);
        int year2 = can2.get(Calendar.YEAR);

        Calendar can = null;
        if (can1.before(can2)) {
            days -= can1.get(Calendar.DAY_OF_YEAR);
            days += can2.get(Calendar.DAY_OF_YEAR);
            can = can1;
        } else {
            days -= can2.get(Calendar.DAY_OF_YEAR);
            days += can1.get(Calendar.DAY_OF_YEAR);
            can = can2;
        }
        for (int i = 0; i < Math.abs(year2 - year1); i++) {
            days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
            can.add(Calendar.YEAR, 1);
        }

        return days;
    }

    /**
     * 获取俩个时间之前的相隔的天数
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static int subDay(String startTime, String endTime) {
        int days = 0;
        try {
            Date date1 = DateInstance().parse(DateInstance().format(DateTimeInstance().parse(startTime)));
            Date date2 = DateInstance().parse(DateInstance().format(DateTimeInstance().parse(endTime)));
            Calendar can1 = Calendar.getInstance();
            can1.setTime(date1);
            Calendar can2 = Calendar.getInstance();
            can2.setTime(date2);
            int year1 = can1.get(Calendar.YEAR);
            int year2 = can2.get(Calendar.YEAR);

            Calendar can = null;
            if (can1.before(can2)) {
                days -= can1.get(Calendar.DAY_OF_YEAR);
                days += can2.get(Calendar.DAY_OF_YEAR);
                can = can1;
            } else {
                days -= can2.get(Calendar.DAY_OF_YEAR);
                days += can1.get(Calendar.DAY_OF_YEAR);
                can = can2;
            }
            for (int i = 0; i < Math.abs(year2 - year1); i++) {
                days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
                can.add(Calendar.YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
     *
     * @param startDate
     * @param endDate
     * @param timeBurst 只就按该时间段内的08:00-18:00时长
     * @return 计算后的秒数
     * @throws ParseException
     * @summary 格式错误返回0
     */
    public static long subtimeBurst(String startDate, String endDate, String timeBurst)
            throws ParseException {
        Date start = DateTimeInstance().parse(startDate);
        Date end = DateTimeInstance().parse(endDate);
        return subtimeBurst(start, end, timeBurst);
    }

    /**
     * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
     *
     * @param startDate
     * @param endDate
     * @param timeBurst 只就按该时间段内的08:00-18:00时长
     * @return 计算后的秒数
     * @throws ParseException
     */
    public static long subtimeBurst(Date startDate, Date endDate, String timeBurst)
            throws ParseException {
        long second = 0;
        Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
        Matcher m = p.matcher(timeBurst);
        boolean falg = false;
        if (startDate.after(endDate)) {
            Date temp = startDate;
            startDate = endDate;
            endDate = temp;
            falg = true;
        }
        if (m.matches()) {
            String[] a = timeBurst.split("-");
            int day = subDay(startDate, endDate);
            if (day > 0) {
                long firstMintues = 0;
                long lastMintues = 0;
                long daySecond = 0;
                String strDayStart = DateInstance().format(startDate) + " " + a[0] + ":00";
                String strDayEnd = DateInstance().format(startDate) + " " + a[1] + ":00";
                Date dayStart = DateTimeInstance().parse(strDayStart);
                Date dayEnd = DateTimeInstance().parse(strDayEnd);
                daySecond = subtract(dayStart, dayEnd);
                if ((startDate.after(dayStart) || startDate.equals(dayStart))
                        && startDate.before(dayEnd)) {
                    firstMintues = (dayEnd.getTime() - startDate.getTime()) / 1000;
                } else if (startDate.before(dayStart)) {
                    firstMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                }
                dayStart = DateTimeInstance().parse(DateInstance().format(endDate) + " " + a[0] + ":00");
                dayEnd = DateTimeInstance().parse(DateInstance().format(endDate) + " " + a[1] + ":00");
                if (endDate.after(dayStart) && (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
                    lastMintues = (endDate.getTime() - dayStart.getTime()) / 1000;
                } else if (endDate.after(dayEnd)) {
                    lastMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                }
                //第一天的秒数 + 最好一天的秒数 + 天数*全天的秒数
                second = firstMintues + lastMintues;
                second += (day - 1) * daySecond;
            } else {
                String strDayStart = DateInstance().format(startDate) + " " + a[0] + ":00";
                String strDayEnd = DateInstance().format(startDate) + " " + a[1] + ":00";
                Date dayStart = DateTimeInstance().parse(strDayStart);
                Date dayEnd = DateTimeInstance().parse(strDayEnd);
                if ((startDate.after(dayStart) || startDate.equals(dayStart))
                        && startDate.before(dayEnd) && endDate.after(dayStart)
                        && (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
                    second = (endDate.getTime() - startDate.getTime()) / 1000;
                } else {
                    if (startDate.before(dayStart)) {
                        if (endDate.before(dayEnd)) {
                            second = (endDate.getTime() - dayStart.getTime()) / 1000;
                        } else {
                            second = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                        }
                    }
                    if (startDate.after(dayStart)) {
                        if (endDate.before(dayEnd)) {
                            second = (endDate.getTime() - startDate.getTime()) / 1000;
                        } else {
                            second = (dayEnd.getTime() - startDate.getTime()) / 1000;
                        }
                    }
                }
                if ((startDate.before(dayStart) && endDate.before(dayStart))
                        || startDate.after(dayEnd) && endDate.after(dayEnd)) {
                    second = 0;
                }
            }
        } else {
            second = (endDate.getTime() - startDate.getTime()) / 1000;
        }
        if (falg) {
            second = Long.parseLong("-" + second);
        }
        return second;
    }

    /**
     * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
     *
     * @param date
     * @param second
     * @param timeBurst
     * @return 计算后的时间
     * @Suumary 指定的格式错误后返回原数据
     */
    public static Date calculate(String date, int second, String timeBurst) {
        Date start = null;
        try {
            start = DateTimeInstance().parse(date);
            return calculate(start, second, timeBurst);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
     *
     * @param date
     * @param second
     * @param timeBurst
     * @return 计算后的时间
     * @Suumary 指定的格式错误后返回原数据
     */
    public static Date calculate(Date date, int second, String timeBurst) {
        Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
        Matcher m = p.matcher(timeBurst);
        Calendar cal = Calendar.getInstance();
        if (m.matches()) {
            String[] a = timeBurst.split("-");
            try {
                Date dayStart = DateTimeInstance().parse(DateInstance().format(date) + " " + a[0] + ":00");
                Date dayEnd = DateTimeInstance().parse(DateInstance().format(date) + " " + a[1] + ":00");
                int DaySecond = (int) subtract(dayStart, dayEnd);
                int toDaySecond = (int) subtract(dayStart, dayEnd);
                if (second >= 0) {
                    if ((date.after(dayStart) || date.equals(dayStart))
                            && (date.before(dayEnd) || date.equals(dayEnd))) {
                        cal.setTime(date);
                        toDaySecond = (int) subtract(date, dayEnd);
                    }
                    if (date.before(dayStart)) {
                        cal.setTime(dayStart);
                        toDaySecond = (int) subtract(dayStart, dayEnd);
                    }
                    if (date.after(dayEnd)) {
                        cal.setTime(day(dayStart, 1));
                        toDaySecond = 0;
                    }

                    if (second > toDaySecond) {
                        int day = (second - toDaySecond) / DaySecond;
                        int remainder = (second - toDaySecond) % DaySecond;
                        cal.setTime(day(dayStart, 1));
                        cal.add(Calendar.DAY_OF_YEAR, day);
                        cal.add(Calendar.SECOND, remainder);
                    } else {
                        cal.add(Calendar.SECOND, second);
                    }

                } else {
                    if ((date.after(dayStart) || date.equals(dayStart))
                            && (date.before(dayEnd) || date.equals(dayEnd))) {
                        cal.setTime(date);
                        toDaySecond = (int) subtract(date, dayStart);
                    }
                    if (date.before(dayStart)) {
                        cal.setTime(day(dayEnd, -1));
                        toDaySecond = 0;
                    }
                    if (date.after(dayEnd)) {
                        cal.setTime(dayEnd);
                        toDaySecond = (int) subtract(dayStart, dayEnd);
                    }
                    if (Math.abs(second) > Math.abs(toDaySecond)) {
                        int day = (Math.abs(second) - Math.abs(toDaySecond)) / DaySecond;
                        int remainder = (Math.abs(second) - Math.abs(toDaySecond)) % DaySecond;
                        cal.setTime(day(dayEnd, -1));
                        cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf("-" + day));
                        cal.add(Calendar.SECOND, Integer.valueOf("-" + remainder));
                    } else {
                        cal.add(Calendar.SECOND, second);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            cal.setTime(date);
        }
        return cal.getTime();
    }

    /**
     * 判断是否在某个时间段内
     *
     * @param startTime
     * @param endTime
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean between(String startTime, String endTime, Date date)
            throws ParseException {
        return between(dateTime(startTime), dateTime(endTime), date);
    }

    /**
     * 判断在某个时间内
     *
     * @param startTime
     * @param endTime
     * @param date
     * @return
     */
    public static boolean between(Date startTime, Date endTime, Date date) {
        return date.after(startTime) && date.before(endTime);
    }
}
