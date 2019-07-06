package com.zifang.util.bigdata.spark.util;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

public class SparkDatasetUtil {

	public static List<String> selectVariableByType(Dataset<Row> dictionary, String type) {
		ArrayList<String> result = new ArrayList<String>();

		Dataset<Row> continuousName = dictionary
				.filter(dictionary.col("type").equalTo(type))
				.select(dictionary.col("Field_Name"));
		List<String> continuouslist = continuousName.as(Encoders.STRING()).collectAsList();
		ArrayList<String> arrayList = new ArrayList<String>(continuouslist);
		for(String columnName : arrayList) {
			result.add(columnName.toLowerCase());
		}
		return result;
	}
}
