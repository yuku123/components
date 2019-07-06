package com.zifang.util.bigdata.spark;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.recommendation.Rating;
import scala.Tuple2;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommonJNBYFunctions implements Serializable{

	public JavaPairRDD<String, Double> getCombinedIdCount(JavaRDD<String> lines, Integer pIndex, Integer...sIndexes){
		JavaPairRDD<String, Double> cIdCount = lines.mapToPair(new GetCombinedCount(pIndex, sIndexes));
		JavaPairRDD<String, Double> cIdSum = cIdCount.reduceByKey(new ReduceByKey());
		return cIdSum;
	}
	
	class GetCombinedCount implements PairFunction<String, String, Double> {
		Integer pIndex;
		Integer[] sindexes;
		
		
		public GetCombinedCount(Integer pIndex, Integer[] sindexes) {
			super();
			this.pIndex = pIndex;
			this.sindexes = sindexes;
		}


		@Override
		public Tuple2<String, Double> call(String s) throws Exception {
			// TODO Auto-generated method stub
			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
			StringBuffer sb = new StringBuffer();
			for(int i =0; i< sindexes.length; i++) {
				sb.append("-");
				sb.append(words[sindexes[i]]);
			}
			String cid = sb.substring(1);
			return new Tuple2<String, Double>(words[pIndex]+"$"+cid, 1.0);
		}
		
	}
	public JavaPairRDD<String, Double> getPairCount(JavaRDD<String> lines, Integer pIndex, Integer sIndex) {
		JavaPairRDD<String, Double> pairCount = lines.mapToPair(new GetPairCount(pIndex, sIndex));
		JavaPairRDD<String, Double> pairCountSum = pairCount.reduceByKey(new ReduceByKey());
		return pairCountSum;
	}

	class GenerateRating implements Function<Tuple2<String,Double>, Rating> {

		@Override
		public Rating call(Tuple2<String, Double> t) throws Exception {
			Integer unionIdHC = StringUtils.split(t._1, "$")[0].hashCode();
			Integer sku = StringUtils.split(t._1, "$")[1].hashCode();
			Double rating = t._2;
			return new Rating(unionIdHC, sku, rating);
		}
		
	}
	
	class GetPairCount implements PairFunction<String, String, Double> {
		int pIndex, sIndex;
		@Override
		public Tuple2<String, Double> call(String t) throws Exception {
			String[] values = StringUtils.split(t, ",");
			return new Tuple2<String, Double>(values[pIndex]+"$"+values[sIndex], 1.0);
		}
		public int getpIndex() {
			return pIndex;
		}
		public void setpIndex(int pIndex) {
			this.pIndex = pIndex;
		}
		public int getsIndex() {
			return sIndex;
		}
		public void setsIndex(int sIndex) {
			this.sIndex = sIndex;
		}
		public GetPairCount(int pIndex, int sIndex) {
			super();
			this.pIndex = pIndex;
			this.sIndex = sIndex;
		}
	}
	class ReduceByKey implements Function2<Double, Double, Double> {

		@Override
		public Double call(Double v1, Double v2) throws Exception {
			// TODO Auto-generated method stub
			return v1+v2;
		}
	}
	public JavaRDD<Rating> getRating(JavaPairRDD<String, Double> pairCountSum) {
		// TODO Auto-generated method stub
		JavaRDD<Rating> ratings = pairCountSum.map(new GenerateRating());
		return ratings;
	}
	
	public JavaPairRDD<Integer, String> getValueIndexMap(JavaPairRDD<String, Double> pairCount, Integer index){
		JavaPairRDD<Integer, String> valueIndexMap = pairCount.mapToPair(new GenerateIndexMap(index));
		return valueIndexMap;
	}
	
	class GenerateIndexMap implements PairFunction<Tuple2<String, Double>, Integer, String> {
		private Integer index;

		public GenerateIndexMap(Integer index) {
			super();
			this.index = index;
		}

		@Override
		public Tuple2<Integer, String> call(Tuple2<String, Double> t) throws Exception {
			// TODO Auto-generated method stub
			return new Tuple2<Integer, String>(StringUtils.split(t._1,"$")[index].hashCode(), StringUtils.split(t._1,"$")[index]);
		}
		
	}
	@Deprecated
	public JavaPairRDD<String, Double> getMutualPairCount(JavaRDD<String> lines) {
		JavaPairRDD<String, String> mutualPairCount = lines.mapToPair(new GetMutualPairCount());
		JavaPairRDD<String, Iterable<String>> pairCountSum = mutualPairCount.groupByKey();//
		JavaPairRDD<String, Double> mutualCountSum = pairCountSum.flatMapToPair(new EstimateMutualCount());
		JavaPairRDD<String, Double> result = mutualCountSum.reduceByKey(new ReduceByKey());
		return result;
	}
	@Deprecated
	class GetMutualPairCount implements PairFunction<String, String, String> {

		@Override
		public Tuple2<String, String> call(String s) throws Exception {
			String[] values = StringUtils.split(s, ",");
			return new Tuple2<String, String>(values[2], values[3]);
		}
		
	}
	@Deprecated
	class EstimateMutualCount implements PairFlatMapFunction<Tuple2<String,Iterable<String>>, String, Double> {

		@Override
		public Iterator<Tuple2<String, Double>> call(Tuple2<String, Iterable<String>> t) throws Exception {
			// TODO Auto-generated method stub
			Set<String> skuset = new HashSet<String>();
			Iterator<String> it = t._2.iterator();
			while(it.hasNext())
				skuset.add(it.next());
			List<String> skulist = new ArrayList<>();
			skulist.addAll(skuset);
			List<Tuple2<String, Double>> flist = new ArrayList<Tuple2<String,Double>>();
			for(int i=0; i< skulist.size(); i++) {
				for(int j=i+1; j < skulist.size(); j++) {
					flist.add(new Tuple2<String, Double>(skulist.get(i)+"$"+skulist.get(j), 1.0));
					flist.add(new Tuple2<String, Double>(skulist.get(j)+"$"+skulist.get(i), 1.0));
				}
			}
			return flist.iterator();
		}
		
	}
	public JavaPairRDD<Integer, double[]> getSKUVectorMap(JavaRDD<Tuple2<Object, double[]>> javaRDD) {
		// TODO Auto-generated method stub
		JavaPairRDD<Integer, double[]> skuMap = javaRDD.mapToPair(new SKUVectorMap());
		return skuMap;
	}
	
	class SKUVectorMap implements PairFunction<Tuple2<Object, double[]>, Integer, double[]> {

		@Override
		public Tuple2<Integer, double[]> call(Tuple2<Object, double[]> t) throws Exception {
			// TODO Auto-generated method stub
			return new Tuple2<Integer, double[]>((Integer)t._1, t._2);
		}
		
	}
	public JavaPairRDD<String, String> getSKUTypeClassMap(JavaRDD<String> lines, int pIndex, int sIndex) {
		// TODO Auto-generated method stub
		JavaPairRDD<String, String> skuTypeClassMap = lines.mapToPair(new CreatePairMap(pIndex, sIndex));
		return skuTypeClassMap;
	}
	
	class CreatePairMap implements PairFunction<String, String, String> {

		private Integer pIndex,sIndex;
		
		
		public CreatePairMap(Integer pIndex, Integer sIndex) {
			super();
			this.pIndex = pIndex;
			this.sIndex = sIndex;
		}


		@Override
		public Tuple2<String, String> call(String s) throws Exception {
			String[] values = StringUtils.split(s, ",");
			return new Tuple2<String, String>(values[pIndex], values[sIndex]);
		}
		
	}
	public JavaRDD<String> getFilteredRDD(JavaRDD<String> lines, final String[] products, final Integer index) {
		// TODO Auto-generated method stub
		JavaRDD<String> flines = lines.filter(new FilterRows(products, index));
		return flines;
	}
	class FilterRows implements Function<String, Boolean> {
		private List<String> products;
		private Integer index;
		
		public FilterRows(String[] products, Integer index) {
			super();
			this.products = Arrays.asList(products);
			this.index = index;
		}

		@Override
		public Boolean call(String s) throws Exception {
			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
			if(products.contains(words[index]))
				return true;
			else
				return false;
		}
		
	}
	public JavaPairRDD<String, List<String>> getPairListMap(JavaRDD<String> lines, int i, int j) {
		// TODO Auto-generated method stub
		JavaPairRDD<String, List<String>> pairListMap = lines.mapToPair(new PairListMap( i, j));
		JavaPairRDD<String, List<String>> pairAggListMap = pairListMap.reduceByKey(new ReduceByKeyList());
		return pairAggListMap;
	}
	
	class ReduceByKeyList implements Function2<List<String>, List<String>, List<String>> {

		@Override
		public List<String> call(List<String> v1, List<String> v2) throws Exception {
			v1.addAll(v2);
			Set<String> set = new HashSet<String>(v1);
			return new ArrayList<String>(set);
		}
		
	}
	class PairListMap implements PairFunction<String, String, List<String>> {
		private Integer pIndex, sIndex;
		
		
		public PairListMap(Integer pIndex, Integer sIndex) {
			super();
			this.pIndex = pIndex;
			this.sIndex = sIndex;
		}


		@Override
		public Tuple2<String, List<String>> call(String s) throws Exception {
			// TODO Auto-generated method stub
			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
			List<String> skuList = new ArrayList<String>();
			skuList.add(words[sIndex]);
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDate ld = LocalDate.parse( StringUtils.split(words[pIndex-1], ".")[0], format );
			return new Tuple2<String, List<String>>(words[pIndex]+"_"+ld.getMonthValue(), skuList);
		}
		
	}
	public JavaPairRDD<String, List<String>> getCombinedIdListMap(JavaRDD<String> lines, int i, Integer[] sIndexes) {
		// TODO Auto-generated method stub
		JavaPairRDD<String, List<String>> pairListMap = lines.mapToPair(new CombinedIdListMap( i, sIndexes));
		JavaPairRDD<String, List<String>> pairAggListMap = pairListMap.reduceByKey(new ReduceByKeyList());
		return pairAggListMap;
	}
	
	class CombinedIdListMap implements PairFunction<String, String, List<String>> {
		private Integer pIndex;
		private Integer[] sIndexes;
		
		public CombinedIdListMap(Integer pIndex, Integer[] sIndexes) {
			super();
			this.pIndex = pIndex;
			this.sIndexes = sIndexes;
		}

		@Override
		public Tuple2<String, List<String>> call(String s) throws Exception {
			// TODO Auto-generated method stub
			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
			StringBuffer sb = new StringBuffer();
			List<String> skuList = new ArrayList<String>();
			
			for(int i =0; i< sIndexes.length; i++) {
				sb.append("-");
				sb.append(words[sIndexes[i]]);
			}
			skuList.add(sb.substring(1));
			System.out.println(sb.substring(1));
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDate ld = LocalDate.parse( StringUtils.split(words[pIndex-1], ".")[0], format );
			return new Tuple2<String, List<String>>(words[pIndex]+"_"+ld.getMonthValue(),skuList);
		}
	}
	public JavaRDD<String> getRDD(JavaRDD<String> lines, int i) {
		JavaRDD<String> rlines = lines.map(new GRDD(i));
		return rlines;
	}
	
	
	class GRDD implements Function<String, String> {
		private int i;

		public GRDD(int i) {
			super();
			this.i = i;
		}


		@Override
		public String call(String s) throws Exception {
			// TODO Auto-generated method stub
			String[] words = StringUtils.splitPreserveAllTokens(s, ",");
			return words[i];
		}
	}
}
