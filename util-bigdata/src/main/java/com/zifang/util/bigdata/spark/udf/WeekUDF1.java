package com.zifang.util.bigdata.spark.udf;

import org.apache.spark.sql.api.java.UDF1;

public class WeekUDF1 implements UDF1<String,Integer> {

	@Override
	public Integer call(String t1) throws Exception {
		return TimeHandle.alternation(t1,TimeHandle.WEEK);
	}

}
