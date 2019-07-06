package com.zifang.util.bigdata.spark.udf;

import org.apache.spark.sql.api.java.UDF3;

public class TimeD implements UDF3<Double,String,String,String> {
	
	private static final long serialVersionUID = 1L;
	@Override
	public String call(Double t1, String t2, String t3) throws Exception {
		// TODO Auto-generated method stub
		return t1+t2+t3;
	}

}
