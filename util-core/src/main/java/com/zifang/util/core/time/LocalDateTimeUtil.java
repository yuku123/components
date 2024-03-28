package com.zifang.util.core.time;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalDateTimeUtil {

    /**
     * LocalDateTime转毫秒时间戳
     */
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        try {
            ZoneId zoneId = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zoneId).toInstant();
            return instant.toEpochMilli();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间戳转LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        try {
            Instant instant = Instant.ofEpochMilli(timestamp);
            ZoneId zone = ZoneId.systemDefault();
            return LocalDateTime.ofInstant(instant, zone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        try {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        try {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = localDateTime.atZone(zoneId);
            return Date.from(zdt.toInstant());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param executeDate 1-31 or 周1-周日
     * @param executeTime 10:10:10
     * @param cycle       0-天 1-周 2-月
     */
    public static String buildCronString(String executeDate, String executeTime, Integer cycle) {

        // Cron表达式模板
        String cronTemplate = "%s %s %s %s %s %s";

        // 默认值
        String month = "*";
        String week = "*";
        String day = "*";
        String hour = "*";
        String min = "*";
        String sec = "*";

        switch (cycle) {
            case 0:
                week = "?";
                break;
            case 1:
                week = executeDate;
                day = "?";
                break;
            case 2:
                day = executeDate;
                week = "?";
                break;
        }

        hour = executeTime.split(":")[0];
        min = executeTime.split(":")[1];
        sec = executeTime.split(":").length > 2 ? executeTime.split(":")[2] : "00";

        return String.format(cronTemplate, sec, min, hour, day, month, week);
    }

    /**
     * @desc: 根据当前日期获得上周的日期区间（上周周一和周日日期)
     * @param: [localDateTime]
     * @return: List<LocalDateTime>
     * @author: ctt
     * @date: 2022/1/14 下午4:02
     */
    public static List<LocalDateTime> getLastWeekTimeInterval(LocalDateTime localDateTime) {
        List<LocalDateTime> list = new ArrayList<>();
        LocalDateTime todayOfLastWeek = localDateTime.minusDays(7);
        LocalDateTime monday = todayOfLastWeek.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = todayOfLastWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        list.add(monday);
        list.add(sunday);
        return list;
    }

    /**
     * @desc: 根据当前日期获得本周的日期区间（周一和周日日期)
     * @param: [localDateTime]
     * @return: List<LocalDateTime>
     * @author: ctt
     * @date: 2022/1/14 下午4:02
     */
    public static List<LocalDateTime> getWeekTimeInterval(LocalDateTime localDateTime) {
        List<LocalDateTime> list = new ArrayList<>();
        LocalDateTime monday = localDateTime.with(DayOfWeek.MONDAY);
        LocalDateTime tuesday = localDateTime.with(DayOfWeek.TUESDAY);
        LocalDateTime wednesday = localDateTime.with(DayOfWeek.WEDNESDAY);
        LocalDateTime thursday = localDateTime.with(DayOfWeek.THURSDAY);
        LocalDateTime friday = localDateTime.with(DayOfWeek.FRIDAY);
        LocalDateTime saturday = localDateTime.with(DayOfWeek.SATURDAY);
        LocalDateTime sunday = localDateTime.with(DayOfWeek.SUNDAY);
        list.add(monday);
        list.add(tuesday);
        list.add(wednesday);
        list.add(thursday);
        list.add(friday);
        list.add(saturday);
        list.add(sunday);
        return list;
    }

    /**
     * 根据当前日期获得上月的日期区间
     */
    public static List<LocalDateTime> getLastMonthTimeInterval(LocalDateTime localDateTime) {
        List<LocalDateTime> list = new ArrayList<>();
        LocalDateTime firstDay = null;
        LocalDateTime lastDay = null;
        if (localDateTime.getMonthValue() == 1) {
            firstDay = LocalDateTime.of(localDateTime.getYear() - 1, 12, 1, 0, 0, 0);
            lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        } else {
            firstDay = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue() - 1, 1, 0, 0, 0);
            lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        }
        list.add(firstDay);
        list.add(lastDay);
        return list;
    }
}
