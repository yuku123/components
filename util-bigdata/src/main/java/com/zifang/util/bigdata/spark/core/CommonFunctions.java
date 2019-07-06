package com.zifang.util.bigdata.spark.core;

import java.io.Serializable;

public class CommonFunctions implements Serializable {
//
//	private long serialVersionUID = 1L;
//
//	private void readObject(final java.io.ObjectInputStream stream)
//			throws IOException, ClassNotFoundException{
////		stream.defaultReadObject();
//		serialVersionUID = stream.readLong();;
//	}
//
//	private void readObjectNoData()
//		     throws ObjectStreamException{
//
//	}
//	private void writeObject(final java.io.ObjectOutputStream stream)
//			throws IOException{
////		stream.defaultWriteObject();
//		stream.writeLong(serialVersionUID);
//	}
//
//	public JavaRDD<String> getJavaRDDFromFile(JavaSparkContext ctx, String fPath){
//		JavaRDD<String> rddDataset = ctx.textFile(fPath).flatMap(
//				new FlatMapFunction<String, String>() {
//					@Override
//					public Iterator<String> call(String s) {
//						return Arrays.asList(AppConstant.NEW_LINE.split(s)).iterator();
//					}
//				}
//				);
//		return rddDataset;
//	}
//
//	public JavaRDD<String> getJavaRDDFromRow(JavaRDD<Row> rows){
//		JavaRDD<String> lines = rows.map(new CreateLine());
//		return lines;
//	}
//
//	class CreateLine implements Function<Row, String> {
//
//		@Override
//		public String call(Row row) throws Exception {
//			StringBuffer sb = new StringBuffer();
//			for(int i=0; i< row.length(); i++) {
//				sb.append(",");
//				sb.append(row.get(i));
//			}
//			return sb.substring(1);
//		}
//
//	}
//	@Deprecated
//	public  Map<Integer,String> getColINameMap(JavaRDD<String> DDlines){
//		// Get the column index and columnName
//		JavaPairRDD<Integer,String> iColPairRDD = DDlines.mapToPair(
//				new PairFunction<String, Integer, String>() {
//					int index = -1;
//					@Override
//					public Tuple2<Integer, String> call(String s) {
//						Tuple2<Integer, String> retValue = null;
//						String[] columns = s.split(",");
//						if(!columns[0].equalsIgnoreCase(AppConstant.FIELD_NAME)||s.isEmpty()){
//							retValue = new Tuple2<Integer, String>(index,columns[0]);
//						}
//						index++;
//						return retValue==null?new Tuple2<Integer, String>(-1, ""):retValue;
//					}
//				}
//				).filter(
//						new Function<Tuple2<Integer,String>, Boolean>() {
//							@Override
//							public Boolean call(Tuple2<Integer,String> t){
//								return t._1!=-1;
//							}
//						}
//						).coalesce(1);
//
//		Map<Integer, String> iColMap = new HashMap<Integer, String>();
//		for(Tuple2<Integer, String> t:iColPairRDD.collect()){
//			iColMap.put(t._1, t._2);
//		}
//		return iColMap;
//	}
//	@Deprecated
//	public  Map<String,Integer> getColNIndexMap(JavaRDD<String> DDlines){
//		// Get the column index and columnName
//		JavaPairRDD<String,Integer> iColPairRDD = DDlines .mapToPair(
//				new PairFunction<String, String,Integer>() {
//					private  final long serialVersionUID = 1L;
//					int index = -1;
//					@Override
//					public Tuple2<String,Integer> call(String s) {
//						Tuple2<String,Integer> retValue = null;
//						String[] columns = StringUtils.splitPreserveAllTokens( s, ",");
//						if(!columns[0].equalsIgnoreCase(AppConstant.FIELD_NAME)){
//							retValue = new Tuple2<String,Integer>(columns[0],index);
//						}
//						index++;
//						return retValue==null?new Tuple2<String,Integer>("",-1):retValue;
//					}
//				}
//				).filter(
//						new Function<Tuple2<String,Integer>, Boolean>() {
//							@Override
//							public Boolean call(Tuple2<String,Integer> t){
//								return !t._1.isEmpty();
//							}
//						}
//						).coalesce(1);
//		Map<String, Integer> iColMap = new HashMap<String, Integer>();
//		for(Tuple2<String, Integer> t:iColPairRDD.collect()){
//			iColMap.put(t._1, t._2);
//		}
//		return iColMap;
//	}
//	@Deprecated
//	public  Map<Integer,String> getColIDataTypeMap(JavaRDD<String> DDlines){
//		// Get the column index and columnName
//		JavaPairRDD<Integer,String> iColPairRDD = DDlines.mapToPair(
//				new PairFunction<String, Integer, String>() {
//					private  final long serialVersionUID = 1L;
//					int index = -1;
//					@Override
//					public Tuple2<Integer, String> call(String s) {
//						Tuple2<Integer, String> retValue = null;
//						String[] columns = StringUtils.splitPreserveAllTokens( s, ",");
//						if(!columns[0].equalsIgnoreCase(AppConstant.FIELD_NAME)){
//							retValue = new Tuple2<Integer, String>(index,columns[2]);
//						}
//						index++;
//						return retValue==null?new Tuple2<Integer, String>(-1, ""):retValue;
//					}
//				}
//				).filter(
//						new Function<Tuple2<Integer,String>, Boolean>() {
//							@Override
//							public Boolean call(Tuple2<Integer,String> t){
//								return t._1!=-1;
//							}
//						}
//						).coalesce(1);
//		Map<Integer, String> iColMap = new HashMap<Integer, String>();
//		for(Tuple2<Integer, String> t:iColPairRDD.collect()){
//			iColMap.put(t._1, t._2);
//		}
//		return iColMap;
//	}
//	@Deprecated
//	public  Map<String,String> getColNDataTypeMap(JavaRDD<String> DDlines){
//		// Get the column index and columnName
//		JavaPairRDD<String,String> iColPairRDD = DDlines .mapToPair(
//				new PairFunction<String, String, String>() {
//					private  final long serialVersionUID = 1L;
//					@Override
//					public Tuple2<String, String> call(String s) {
//						Tuple2<String, String> retValue = null;
//						String[] columns = StringUtils.splitPreserveAllTokens( s, ",");
//						if(!columns[0].equalsIgnoreCase(AppConstant.FIELD_NAME)){
//							retValue = new Tuple2<String, String>(columns[0],columns[2]);
//						}
//						return retValue==null?new Tuple2<String, String>("-1", ""):retValue;
//					}
//				}
//				).filter(
//						new Function<Tuple2<String,String>, Boolean>() {
//							@Override
//							public Boolean call(Tuple2<String,String> t){
//								return t._1!="-1";
//							}
//						}
//						).coalesce(1);
//		Map<String, String> iColMap = new HashMap<String, String>();
//		for(Tuple2<String, String> t:iColPairRDD.collect()){
//			iColMap.put(t._1, t._2);
//		}
//		return iColMap;
//	}
//
//	public JavaRDD<String> getColIPTMap(JavaRDD<String> lines, final Map<Integer, String> colIDataTypeMap,
//                                        final Map<Integer, String> colINameMap, final Double[] percent){
//		JavaPairRDD<Integer, Double> pairRDD = lines.flatMapToPair(new GetColumnValues(colIDataTypeMap));
//		JavaPairRDD<Integer, Double> pairFRDD = pairRDD.filter(new FilterPairRDD());
//		JavaPairRDD<Integer, Iterable<Double>> pairFRDD_1= pairFRDD.groupByKey();
//		JavaRDD<String> colNPer = pairFRDD_1.map(
//				new EstimatePercentile(percent, colINameMap) );
//
//		/*Map<Integer, Double[]> broadcastMap = new HashMap<>();
//		broadcastMap.putAll(pairRDD.collectAsMap());
//
//		Broadcast<Map<Integer, Iterable<Double>>> broadcastVar_2 = ctx.broadcast(broadcastMap);
//		Map<Integer, Double[]> iPTMap = new HashMap<Integer, Double[]>();
//		;
//		for(Tuple2<Integer, Iterable<Double>> t: broadcastVar_2.getValue())) {
//			JavaRDD<Double> rdd = ctx.parallelize(
//					Arrays.asList(Iterables.toArray(t._2, Double.class)));
//			Double[] tiles = getPercentiles(rdd, new Double[]{0.05,0.95}, 10);
//			iPTMap.put(t._1, tiles);
//		}*/
//		return colNPer;
//	}
//
//	class EstimatePercentile implements Function<Tuple2<Integer, Iterable<Double>>, String> {
//
//		private Double[] percent;
//		private Map<Integer, String> colINameMap;
//
//		@Override
//		public String call(Tuple2<Integer, Iterable<Double>> v1) throws Exception {
//			Double[] rvalues = new Double[percent.length];
//			Iterator<Double> it = v1._2.iterator();
//			List<Double> values = new ArrayList<Double>();
//			while(it.hasNext())
//				values.add(it.next());
//			Collections.sort(values);
//			int size = values.size();
//			StringBuffer sb = new StringBuffer();
//			sb.append(colINameMap.get(v1._1));
//			for (int i = 0; i < percent.length; i++) {
//				double percentile = percent[i];
//				int id = (int)(size * percentile);
//				rvalues[i] = values.get(id);
//				sb.append(",");
//				sb.append(rvalues[i]);
//			}
//			return sb.toString();
//		}
//
//		public EstimatePercentile(Double[] percent, Map<Integer, String> colINameMap) {
//			super();
//			this.percent = percent;
//			this.colINameMap = colINameMap;
//		}
//
//		public Double[] getPercent() {
//			return percent;
//		}
//
//		public void setPercent(Double[] percent) {
//			this.percent = percent;
//		}
//
//		public Map<Integer, String> getColINameMap() {
//			return colINameMap;
//		}
//
//		public void setColINameMap(Map<Integer, String> colINameMap) {
//			this.colINameMap = colINameMap;
//		}
//
//		public EstimatePercentile() {
//			super();
//		}
//
//	}
//	class FilterPairRDD implements Function<Tuple2<Integer,Double>, Boolean> {
//
//		@Override
//		public Boolean call(Tuple2<Integer, Double> v1) throws Exception {
//
//			return v1 != null;
//		}
//
//	}
//	class GetColumnValues implements PairFlatMapFunction<String, Integer, Double> {
//
//		private Map<Integer, String> colIDataTypeMap;
//
//		@Override
//		public Iterator<Tuple2<Integer, Double>> call(String t) throws Exception {
//				String[] words = StringUtils.splitPreserveAllTokens(t, ",");
//				System.out.println("GetColumnValues "+"---------------> "+t.toString()+" "+words.length+" "+colIDataTypeMap.keySet().size());
//				if(words.length < colIDataTypeMap.keySet().size())
//					return null;
//				/*
//				 *  try implementing better ways, this is not memory optimized
//				 */
//
//				ArrayList<Tuple2<Integer, Double>> list = new ArrayList<Tuple2<Integer, Double>>();
//				for(int i=0; i< words.length; i++) {
//					if(colIDataTypeMap.get(i).equalsIgnoreCase(AppConstant.NUMERIC))
//						list.add(new Tuple2<Integer, Double>(Integer.valueOf(i), (words[i].isEmpty()||!NumberUtils.isNumber(words[i]))?0:Double.valueOf(words[i])));
//					else
//						continue;
//				}
//				return list.iterator();
//		}
//
//		public GetColumnValues(Map<Integer, String> colIDataTypeMap) {
//			super();
//			this.colIDataTypeMap = colIDataTypeMap;
//		}
//
//		public GetColumnValues() {
//			super();
//			// TODO Auto-generated constructor stub
//		}
//
//		public Map<Integer, String> getColIDataTypeMap() {
//			return colIDataTypeMap;
//		}
//
//		public void setColIDataTypeMap(Map<Integer, String> colIDataTypeMap) {
//			this.colIDataTypeMap = colIDataTypeMap;
//		}
//	}
//
//
//	public Double[] getPercentiles(JavaRDD<Double> rdd, Double[] percentiles, int numPartitions) {
//		Double[] values = new Double[percentiles.length];
//		JavaRDD<Double> sorted = rdd.sortBy(new Function<Double, Double>() {
//
//			@Override
//			public Double call(Double v1) throws Exception {
//				if(v1 instanceof Double)
//					return v1;
//				else
//					return 0.0;
//			}
//		}, true, numPartitions);
//		JavaPairRDD<Long, Double> indexed = sorted.zipWithIndex().mapToPair(
//				new PairFunction<Tuple2<Double,Long>, Long, Double>() {
//
//					@Override
//					public Tuple2<Long, Double> call(Tuple2<Double, Long> t) throws Exception {
//						return t.swap();
//					}
//				});
//		Long rddSize = rdd.count();
//		for (int i = 0; i < percentiles.length; i++) {
//			double percentile = percentiles[i];
//			long id = (long) (rddSize * percentile);
//			values[i] = indexed.lookup(id).get(0);
//		}
//		return values;
//	}
//
//	public JavaRDD<String> replaceOutliers(JavaRDD<String> lines, final Map<Integer, Double[]> iPTileMap, final int columns) {
//		JavaRDD<String> rlines = lines.map(new UpdateOutliers(columns, iPTileMap));
//		JavaRDD<String> flines = rlines.filter(new FilterRDD());
//		return flines;
//	}
//	class FilterRDD implements Function<String, Boolean> {
//
//		@Override
//		public Boolean call(String v1) throws Exception {
//			return v1 != null;
//		}
//
//	}
//	class UpdateOutliers implements Function<String, String> {
//
//		private int columns;
//		private Map<Integer, Double[]> map;
//
//		public int getColumns() {
//			return columns;
//		}
//
//		public void setColumns(int columns) {
//			this.columns = columns;
//		}
//
//		public Map<Integer, Double[]> getMap() {
//			return map;
//		}
//
//		public void setMap(Map<Integer, Double[]> map) {
//			this.map = map;
//		}
//
//		public UpdateOutliers(int columns, Map<Integer, Double[]> map) {
//			super();
//			this.columns = columns;
//			this.map = map;
//		}
//
//		@Override
//		public String call(String s) throws Exception {
//
//			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
//			if(words.length < columns)
//				return null;
//			StringBuffer sb = new StringBuffer();
//			for(int i=0; i< words.length; i++) {
//				if(map.keySet().contains(i) && NumberUtils.isNumber(words[i])) {
//					if(Double.valueOf(words[i]) < map.get(i)[0]) {
//						sb.append(",");
//						sb.append(map.get(i)[0]);
//					} else if (Double.valueOf(words[i]) > map.get(i)[1]) {
//						sb.append(",");
//						sb.append(map.get(i)[1]);
//					} else {
//						sb.append(",");
//						// change the fix
//						sb.append(words[i]);//.isEmpty()?Double.valueOf(12):words[i]);
//					}
//				} else {
//					sb.append(",");
//					sb.append(words[i]);
//				}
//			}
//			return sb.substring(1);
//		}
//	}
//
//	public JavaRDD<Row> convertToROW(JavaRDD<String> lines, final Map<Integer, String> map) {
//		JavaRDD<Row> rows = lines.map(new CreateRow(map));
//		JavaRDD<Row> frows = rows.filter(new FilterRow());
//		return frows;
//	}
//
//	class FilterRow implements Function<Row, Boolean> {
//		@Override
//		public Boolean call(Row v1) throws Exception {
//			return v1 != null;
//		}
//	}
//	class CreateRow implements Function<String, Row> {
//		private Map<Integer, String> map;
//		@Override
//		public Row call(String line) throws Exception {
//			String[] words = StringUtils.splitPreserveAllTokens(line, ",");
//			if(words.length < map.keySet().size())
//				return null;
//			ArrayList<Object> list = new ArrayList<Object>();
//
//			for(int i =0; i< words.length; i++) {
//				if(!map.containsKey(i))
//					continue;
//				switch (map.get(i).toLowerCase()){
//				case HiveConstant.GENERAL:
//					list.add(new String(words[i]));
//					break;
//				case HiveConstant.CHARACTER:
//					list.add(new String(words[i]));
//					break;
//				case HiveConstant.DEPENDENT:
//					list.add(new String(words[i]));
//					break;
//				case HiveConstant.NUMERIC:
//					//change the fix
//					if(null != words[i] && !words[i].trim().isEmpty() )
//						list.add(new Double(words[i]));
//					else
//						list.add(Double.valueOf(0));
//					break;
//				case HiveConstant.DISCRETE:
//					list.add(new Integer(words[i]));
//					break;
//				case HiveConstant.STRINGL:
//					list.add(new String(words[i]));
//					break;
//				}
//			}
//			return RowFactory.create(list.toArray());
//		}
//
//		public Map<Integer, String> getMap() {
//			return map;
//		}
//
//		public void setMap(Map<Integer, String> map) {
//			this.map = map;
//		}
//
//		public CreateRow(Map<Integer, String> map) {
//			super();
//			this.map = map;
//		}
//
//		public CreateRow() {
//			super();
//		}
//
//	}
//	public JavaRDD<Row> convertToROW(JavaPairRDD<String, Double> pairRDD) {
//		JavaRDD<Row> rows = pairRDD.map(new GetRow());
//		return rows;
//	}
//
//	class GetRow implements Function<Tuple2<String,Double>, Row> {
//
//		@Override
//		public Row call(Tuple2<String, Double> v1) throws Exception {
//			ArrayList<Object> list = new ArrayList<Object>();
//			list.add(new String(v1._1));
//			list.add(new Double(v1._2));
//			return RowFactory.create(list.toArray());
//		}
//
//	}
//	public StructType datasetSchema(Map<Integer, String> map, Map<Integer, String> map2) {
//		StructType struct = new StructType();
//		SortedSet<Integer> keys = new TreeSet<Integer>(map.keySet());
//		for (Integer key : keys) {
//			switch (map.get(key).toLowerCase()) {
//			case HiveConstant.GENERAL:
//				struct = struct.add(map2.get(key), DataTypes.StringType);
//				break;
//			case HiveConstant.CHARACTER:
//				struct = struct.add(map2.get(key), DataTypes.StringType);
//				break;
//			case HiveConstant.NUMERIC:
//				struct = struct.add(map2.get(key), DataTypes.DoubleType);
//				break;
//			case HiveConstant.DEPENDENT:
//				struct = struct.add(map2.get(key), DataTypes.StringType);
//				break;
//			case HiveConstant.DISCRETE:
//				struct = struct.add(map2.get(key), DataTypes.IntegerType);
//				break;
//			case HiveConstant.STRINGL:
//				struct = struct.add(map2.get(key), DataTypes.StringType);
//				break;
//			}
//		}
//		return struct;
//	}
//	@Deprecated
//	public Long getCount(JavaRDD<String> lines) {
//		return lines.count();
//	}
//
//	public Dataset<Row> getColMissPairRDD(SQLContext sqlContext, JavaRDD<String> lines, final Map<Integer, String> colINameMap,
//                                          final Integer tcount, final String missPTable){
//		try {
//			JavaPairRDD<Integer, Double> MissingCountLength = lines.flatMapToPair(new GetColMissValue());
//			JavaPairRDD<Integer, Double> MissingCountSum = MissingCountLength.reduceByKey(new Sum());
//			JavaPairRDD<String, Double> colMissPairRDD = MissingCountSum.mapToPair(new Percentage(colINameMap, tcount));
//			JavaRDD<Row> rows = convertToROW(colMissPairRDD).persist(StorageLevel.MEMORY_AND_DISK_SER());
//			Map<Integer, String> map1 = new HashMap<Integer, String>();
//			map1.put(1,HiveConstant.CHARACTER);
//			map1.put(2,HiveConstant.NUMERIC);
//			Map<Integer, String> map2 = new HashMap<Integer, String>();
//			map2.put(1,HiveConstant.COLUMN);
//			map2.put(2,HiveConstant.PERCENTAGE);
//			final StructType schema = datasetSchema(map1, map2);
//			Dataset<Row> nrows = sqlContext.createDataFrame(rows, schema);
//			return nrows;
//		} catch(Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//	class GetColMissValue implements PairFlatMapFunction<String, Integer, Double> {
//		@Override
//		public Iterator<Tuple2<Integer, Double>> call(String s) throws Exception {
//			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
//			List<Tuple2<Integer, Double>> list = new ArrayList<Tuple2<Integer, Double>>();
//			for(int i=0; i< words.length; i++){
//				list.add(new Tuple2<Integer, Double>(i, (words[i].trim().isEmpty()||words[i].equalsIgnoreCase("null")?1.0:0)));
//			}
//			return list.iterator();
//
//		}
//
//	}
//	class Sum implements Function2<Double, Double, Double> {
//		@Override
//		public Double call(Double v1, Double v2) throws Exception {
//			return v1+v2;
//		}
//	}
//	class Percentage implements PairFunction<Tuple2<Integer, Double>, String, Double> {
//
//		private Map<Integer, String> colINameMap;
//		private int tcount;
//
//		@Override
//		public Tuple2<String, Double> call(Tuple2<Integer, Double> t) throws Exception {
//			return new Tuple2<String, Double>(colINameMap.get(t._1), t._2/tcount);
//		}
//		public Map<Integer, String> getColINameMap() {
//			return colINameMap;
//		}
//		public void setColINameMap(Map<Integer, String> colINameMap) {
//			this.colINameMap = colINameMap;
//		}
//		public int getTcount() {
//			return tcount;
//		}
//		public void setTcount(int tcount) {
//			this.tcount = tcount;
//		}
//		public Percentage() {
//			super();
//		}
//		public Percentage(Map<Integer, String> colINameMap, int tcount) {
//			super();
//			this.colINameMap = colINameMap;
//			this.tcount = tcount;
//		}
//	}
//	@Override
//	public String toString() {
//		return "CommonFunctions [serialVersionUID=" + serialVersionUID + "]";
//	}
//	public JavaRDD<String> replaceMissing(JavaRDD<String> lines, Map<Integer, BasicMetric> filcolINameMap) {
//		JavaRDD<String> rlines = lines.map(new Update(filcolINameMap));
//		return rlines;
//	}
//
//	class Update implements Function<String, String> {
//
//		private Map<Integer, BasicMetric>  map;
//		@Override
//		public String call(String s) throws Exception {
//			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
//			StringBuffer sb = new StringBuffer();
//			for(int i=0; i< words.length; i++){
//				if(map.keySet().contains(i)) {
//					if(null == words[i] || words[i].trim().isEmpty() || words[i].equalsIgnoreCase("null")){
//						sb.append(",");
//						BasicMetric metric = map.get(i);
//						if(null != metric.getMean())
//							sb.append(String.valueOf(metric.getMean()));
//						else if(null != metric.getMode())
//							sb.append(String.valueOf(metric.getMode()));
//						else
//							sb.append(String.valueOf(metric.getMin()));
//					}else {
//						sb.append(",");
//						sb.append(words[i]);
//					}
//				} else {
//					sb.append(",");
//					sb.append(words[i]);
//				}
//			}
//			return sb.substring(1);
//		}
//
//		public Update(Map<Integer, BasicMetric> map) {
//			super();
//			this.map = map;
//		}
//
//	}
//
//	public JavaRDD<String> filterColumn(JavaRDD<String> rlines) {
//		JavaRDD<String> flines = rlines.filter(new filter());
//		return flines;
//	}
//
//	class filter implements Function<String, Boolean> {
//
//		@Override
//		public Boolean call(String s) throws Exception {
//			if(null == s)
//				return false;
//			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
//			double count = 0;
//			for(int i=0; i< words.length; i++) {
//				if(words[i].trim().isEmpty()|| words[i].equalsIgnoreCase("null"))
//					count++;
//			}
//			if (count/words.length > 0.5)
//				return false;
//			else
//				return true;
//		}
//	}
}












