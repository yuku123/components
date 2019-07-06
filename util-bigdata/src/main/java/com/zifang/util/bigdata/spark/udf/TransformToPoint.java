package com.zifang.util.bigdata.spark.udf;

import org.apache.spark.sql.api.java.UDF2;

public class TransformToPoint implements UDF2<String, String,Integer> {

	@Override
	public Integer call(String time, String type) throws Exception {
		return TimeHandle.alternation(time,type);
	}

}
