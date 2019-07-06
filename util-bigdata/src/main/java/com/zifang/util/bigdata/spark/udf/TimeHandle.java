package com.zifang.util.bigdata.spark.udf;

import org.joda.time.*;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeHandle {
	
	public static String DAY = "day";
	public static String YEAR = "year";
	public static String MONTH = "month";
	public static String QUARTER = "quarter";
	public static String WEEK = "week";
	
	public static String origin = "1980-01-01T00:00:00";
	
	public static DateTime movePoint(String timestamp, String moveType) {
		DateTime point = null;
		if(timestamp.length() < 12) {
			point = DateTime.parse(timestamp);
		}else {
			DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			point = DateTime.parse(timestamp,format);
		}

		if(YEAR.equals(moveType)) {
			if(point.getDayOfYear()>1) {
				point = point.plusYears(1);
				point = point.minusDays(point.getDayOfYear()-1);
			}
		} else if(MONTH.equals(moveType)) {
			if(point.getMonthOfYear()>1) {
				point = point.plusMonths(1);
				point = point.minusDays(point.getDayOfMonth()-1);
			}
		} else if(WEEK.equals(moveType)) {
			if(point.getDayOfWeek()!=7) {
				point = point.plusWeeks(1);
				point = point.minusDays(point.getDayOfWeek());
			}
		} else if (DAY.equals(moveType)) {
			if(point.getMonthOfYear()>1) {
				point = point.plusMonths(1);
				point = point.minusDays(point.getDayOfMonth()-1);
			}
		} else if (QUARTER.equals(moveType)){
//			Integer quarter = getQuarterByMonth(point.getMonthOfYear());
//			//下一个季度初始月份是
//			Integer nextQuarterMonth = 3*(quarter-1)+1;
//			//向初始月份挪过去的月份值是
//			Integer move = nextQuarterMonth - quarter;
//			//对point进行操作
//			point = point.plusMonths(move);
//			point = point.minusDays(point.getDayOfMonth()-1);
		}
		return point;
	}


	//根据月份获得季度值
	public static int getQuarterByMonth(int month) {
		if (month >= 1 && month <= 12) {
			return ((month + 2) / 3);
		} else {
			return -1;
		}
	}
	
	public static Integer alternation(DateTime timestamp, String type) {
		DateTime start = DateTime.parse(origin);
		DateTime end = timestamp;
		
		int count = 0;
		if(YEAR.equals(type)) {
			count = Years.yearsBetween(start, end).getYears();
		} else if(MONTH.equals(type)) {
			count = Months.monthsBetween(start, end).getMonths();
		} else if(WEEK.equals(type)) {
			count = Weeks.weeksBetween(start, end).getWeeks();
		} else if (DAY.equals(type)) {
			count = Days.daysBetween(start, end).getDays();
		} else if (QUARTER.equals(type)){
			//至今为止的quarter数　＋　今年的quarter数量
			Integer yearCountQuart = Years.yearsBetween(start, end).getYears() * 4;
			Integer innerYearCountQuart = getQuarterByMonth(end.getMonthOfYear());
			//累加
			count = yearCountQuart + innerYearCountQuart;
		}
		return count;
	}
	
	public static Integer alternation(String timestamp,String type) {
		return alternation(movePoint(timestamp,type),type);
	}
	
	
	public static void main(String[] args) {
//		DateTime start = DateTime.parse("2018-08-01");
//		DateTime end = DateTime.parse("2018-08-09");
//		int weeks = Weeks.weeksBetween(start, end).getWeeks();
//        int days = Days.daysBetween(start, end).getDays();
//        int months = Months.monthsBetween(start, end).getMonths();
//        int years = Years.yearsBetween(start, end).getYears();
//        System.out.println(years+"\t"+months+"\t"+days+"\t"+weeks+"\n");
		
		System.out.println(alternation("2016-01-31 11:11:11", "quarter"));;
		for(int i = 0;i<25;i++){
			String time= DateTime.parse("2015-12-12").plusMonths(i).toString("yyyy-MM");
			System.out.println(time+":"+alternation(DateTime.parse("2015-12-12").plusMonths(i),"month"));
		}
	}

}
