package com.zifang.util.bigdata.spark.udf;

import org.apache.spark.sql.api.java.UDF2;
import org.joda.time.*;


/**
 * 标准时间进行数轴转换操作
 *
 * 时间种类可以选取year,month,week,day,quarter
 *
 * 返回值为距离1980年的数值大小
 * */
public class TransformToPoint implements UDF2<String, String, Integer> {

	private static DateTime start = DateTime.parse("1980-01-01T00:00:00");
	@Override
	public Integer call(String time, String type) throws Exception {
		DateTime end = DateTime.parse(time);
		int count = 0;
		if("year".equals(type)) {
			count = Years.yearsBetween(start, end).getYears();
		} else if("month".equals(type)) {
			count = Months.monthsBetween(start, end).getMonths();
		} else if("week".equals(type)) {
			count = Weeks.weeksBetween(start, end).getWeeks();
		} else if ("day".equals(type)) {
			count = Days.daysBetween(start, end).getDays();
		} else if ("quarter".equals(type)){
			//至今为止的quarter数　＋　今年的quarter数量
			Integer yearCountQuart = Years.yearsBetween(start, end).getYears() * 4;
			Integer innerYearCountQuart = getQuarterByMonth(end.getMonthOfYear());
			//累加
			count = yearCountQuart + innerYearCountQuart;
		}
		return count;
	}
	//根据月份获得季度值
	private int getQuarterByMonth(int month) {
		if (month >= 1 && month <= 12) {
			return ((month + 2) / 3);
		} else {
			return -1;
		}
	}

}
