package com.zifang.util.bigdata.spark.udf;

import org.apache.spark.sql.api.java.UDF2;

public class ToAnother implements UDF2<Object, Object,String> {

	 /**
	 * (Z-WOO, S+Z-WSN, S+Z-NSN, Z-MIS, Z-NSS, Z-SET, S-MIS, S+Z-SSN, S-SET, Z-CNS)
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String call(Object t1, Object t2) throws Exception {
		// TODO Auto-generated method stub
		String v1 = String.valueOf(t1).replace(".", "");
		String v2 = String.valueOf(t2).replace(".", "");
		if(v2.contains(v1)) {
			return v1;
		}else {
			return ("others");
		}
	}
}
