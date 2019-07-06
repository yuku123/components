package com.zifang.util.bigdata.spark.context;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class SQLContextLoader implements Serializable{

	private static final long serialVersionUID = -3047711976103490894L;

	public SQLContext getContext(String master, String app) {
		SparkSession session = SparkSessionLoader.getInstance(master, app).getSession();
		return session.sqlContext();
	}
}
